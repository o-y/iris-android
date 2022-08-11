package wtf.zv.android.iris.rpc.internal.observers;

/**
 * Stream Subscription observer
 *
 * @param <T> - T
 */
public interface StreamSubscriptionObserver<T> {
    void onNext(T next);

    default void onError(Throwable t) {};

    default void onConnect() {};

    default void onReconnect() {};

    default void onDisconnect(Throwable t) {};
}


