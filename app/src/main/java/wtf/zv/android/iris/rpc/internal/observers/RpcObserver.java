package wtf.zv.android.iris.rpc.internal.observers;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

public final class RpcObserver<T> {
    private static final int RETRY_RATE_SECONDS = 10;

    private final Supplier<Mono<T>> supplier;
    private final SingleSubscriptionObserver<T> singleSubscriptionObserver;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final AtomicReference<Disposable> subscriptionRef = new AtomicReference<>();

    private SubscriptionState state = SubscriptionState.UNKNOWN;
    private ScheduledFuture<?> scheduledFuture;

    @AssistedInject
    RpcObserver(
            @Assisted Supplier<Mono<T>> supplier,
            @Assisted SingleSubscriptionObserver<T> singleSubscriptionObserver
    ) {
        this.supplier = supplier;
        this.singleSubscriptionObserver = singleSubscriptionObserver;
    }

    public void subscribe(){
        scheduledFuture = scheduler.scheduleAtFixedRate(this::reconnectStream, RETRY_RATE_SECONDS, RETRY_RATE_SECONDS, TimeUnit.SECONDS);
        initiateSubscription();
    }

    private void initiateSubscription(){
        Mono<T> response = supplier.get();
        Disposable subscription = response
                .doOnError(this::onError)
                .onErrorStop()
                .doOnNext(this::onWarmUp)
                .subscribe(this::onData);

        subscriptionRef.set(subscription);
    }

    private void onData(T data){
        singleSubscriptionObserver.onData(data);
        if (subscriptionRef.get() != null){
            subscriptionRef.get().dispose();
        }
        scheduledFuture.cancel(true);
        singleSubscriptionObserver.onDisconnect(new Throwable("Subscription ended - received data"));
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
            singleSubscriptionObserver.onConnect();
        }
    }

    private void onError(Throwable throwable){
        singleSubscriptionObserver.onError(throwable);
        if (throwable.getMessage() != null && throwable.getMessage().contains("UNAVAILABLE")){
            if (state != SubscriptionState.DISCONNECTED){
                state = SubscriptionState.DISCONNECTED;
                singleSubscriptionObserver.onDisconnect(throwable);
            }
        }
    }

    @AssistedFactory
    public interface RpcObserverFactory<T> {
        RpcObserver<T> create(
                Supplier<Mono<T>> supplier,
                SingleSubscriptionObserver<T> singleSubscriptionObserver
        );
    }
}