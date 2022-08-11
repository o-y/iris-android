package wtf.zv.android.iris.rpc.internal.observers;

/**
 * Single Subscription observer
 *
 * @param <T> - T
 */
public interface SingleSubscriptionObserver<T> {
    void onData(T data);

    default void onError(Throwable t) {};

    default void onConnect() {};

    default void onDisconnect(Throwable t) {};
}


