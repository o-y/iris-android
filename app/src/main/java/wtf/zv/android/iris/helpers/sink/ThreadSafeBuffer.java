package wtf.zv.android.iris.helpers.sink;

import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;
import javax.inject.Singleton;

import reactor.core.publisher.Sinks;

@Singleton
public final class ThreadSafeBuffer<T> {

    private final AtomicReference<T> reference = new AtomicReference<>();

    private Sinks.Many<T> replayBuffer;

    @Inject
    public ThreadSafeBuffer(){
        replayBuffer = createNewReplayBuffer();
    }

    public Sinks.Many<T> getBuffer() {
        return replayBuffer;
    }

    public void requestFreshBuffer() {
        this.replayBuffer = createNewReplayBuffer();

        T ref = reference.get();
        if (ref != null){
            this.replayBuffer.tryEmitNext(ref);
        }
    }

    private Sinks.Many<T> createNewReplayBuffer() {
        // Initially a multicast-replayBuffer was used here, this however didn't cache the most
        // recent post, which meant a new subscriber would get a "GO_AWAY_SIG" error from the GRPC host
        // as there were no recent posts. By using a replay mechanism we can cache an arbitrary of posts
        // for use when there are no subscribers, meaning when a subscription does occur we can send n
        // posts in the initial response, this however is not applicable to new clients, meaning we need
        // to cache the most recent post, this does mean we essentially have a wasted subscription who's
        // only job it is, is too cache, meaning this logic could be refactored into the GRPC service,
        // but that would introduce ugly side-effects (the GRPC interface should be idempotent).

        Sinks.Many<T> replayBuffer = Sinks.many().replay().latest();
        replayBuffer.asFlux().subscribe(reference::set);
        return replayBuffer;
    }
}
