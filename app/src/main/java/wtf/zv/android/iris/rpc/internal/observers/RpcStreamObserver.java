package wtf.zv.android.iris.rpc.internal.observers;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

public final class RpcStreamObserver<T> {
    private static final int RETRY_RATE_SECONDS = 10;

    private final Supplier<Flux<T>> supplier;
    private final StreamSubscriptionObserver<T> streamSubscriptionObserver;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final AtomicReference<Disposable> subscriptionRef = new AtomicReference<>();

    private SubscriptionState state = SubscriptionState.UNKNOWN;
    private boolean initialConnectionState = false;

    @AssistedInject
    RpcStreamObserver(
            @Assisted Supplier<Flux<T>> supplier,
            @Assisted StreamSubscriptionObserver<T> streamSubscriptionObserver
    ) {
        this.supplier = supplier;
        this.streamSubscriptionObserver = streamSubscriptionObserver;
    }

    public void subscribe(){
        scheduler.scheduleAtFixedRate(this::reconnectStream, RETRY_RATE_SECONDS, RETRY_RATE_SECONDS, TimeUnit.SECONDS);
        initiateSubscription();
    }

    private void initiateSubscription(){
        Flux<T> response = supplier.get();

        Disposable subscription = response
                .doOnError(this::onError)
                .onErrorStop()
                .doOnNext(this::onWarmUp)
                .subscribe(streamSubscriptionObserver::onNext);
        subscriptionRef.set(subscription);
    }

    private void reconnectStream(){
        if (isDisposed()){
            initiateSubscription();
        }
    }

    private boolean isDisposed(){
        return subscriptionRef.get() != null && subscriptionRef.get().isDisposed();
    }

    private void onWarmUp(T data){
        if (state != SubscriptionState.CONNECTED){
            state = SubscriptionState.CONNECTED;
            if (initialConnectionState){
                streamSubscriptionObserver.onReconnect();
            } else {
                streamSubscriptionObserver.onConnect();
                initialConnectionState = true;
            }
        }
    }

    private void onError(Throwable throwable){
        streamSubscriptionObserver.onError(throwable);
        if (throwable.getMessage() != null && throwable.getMessage().contains("UNAVAILABLE")){
            if (state != SubscriptionState.DISCONNECTED){
                state = SubscriptionState.DISCONNECTED;
                streamSubscriptionObserver.onDisconnect(throwable);
            }
        }
    }

    @AssistedFactory
    public interface RpcStreamObserverFactory<T> {
        RpcStreamObserver<T> create(
                Supplier<Flux<T>> supplier,
                StreamSubscriptionObserver<T> streamSubscriptionObserver
        );
    }
}