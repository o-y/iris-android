package wtf.zv.android.iris.rpc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import wtf.zv.android.iris.base.config.ConfigStateStore;
import wtf.zv.android.iris.base.config.ConfigUpdateHandler;
import wtf.zv.android.iris.base.service.state.ServiceState;
import wtf.zv.android.iris.base.service.state.internal.ThreadSafeServiceStateBuffer;
import wtf.zv.android.iris.base.system.post.lifecycle.PostLifecyclePreprocessor;
import wtf.zv.android.iris.helpers.sink.ThreadSafeBuffer;
import wtf.zv.android.iris.rpc.internal.observers.StreamSubscriptionObserver;
import wtf.zv.iris.proto.Post;

public class IrisRpcManager {
    private final IrisRpcChannel irisRpcChannel;
    private final ConfigUpdateHandler configUpdateHandler;
    private final PostLifecyclePreprocessor postLifeCycleManager;
    private final ThreadSafeBuffer<ServiceState> serviceStateBuffer;

    private boolean initialConnection = true;

    @Inject
    IrisRpcManager(
            IrisRpcChannel irisRpcChannel,
            ConfigUpdateHandler configUpdateHandler,
            PostLifecyclePreprocessor postLifeCycleManager,
            @ThreadSafeServiceStateBuffer ThreadSafeBuffer<ServiceState> serviceStateBuffer
    ) {
        this.irisRpcChannel = irisRpcChannel;
        this.configUpdateHandler = configUpdateHandler;
        this.postLifeCycleManager = postLifeCycleManager;
        this.serviceStateBuffer = serviceStateBuffer;
    }

    public void start(){
        onInitialStartup();
    }

    private void onInitialStartup(){
        android.util.Log.i("slyo", "Running initial start up!");
        serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.DISCONNECTED);

        postLifeCycleManager
                .onStartUp()
                .subscribe(
                        // The Config stream does not handle GRPC LifeCycle events. That is handled
                        // by the PostLifeCycleManager class.
                        unused -> {
                            irisRpcChannel.createConfigObserver(config -> {
                                android.util.Log.i("slyo", "[RPC:CONFIG]: Got config. isInitialConnection = " + initialConnection);
                                configUpdateHandler.onConfig(config);
                                if (initialConnection){
                                    serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.CONNECTED);
                                    irisRpcChannel.createPostStreamObserver(postStreamSubscriptionObserver);
                                    initialConnection = false;
                                }
                            });
                        }
                );
    }

    private final StreamSubscriptionObserver<Post> postStreamSubscriptionObserver = new StreamSubscriptionObserver<Post>() {
        @Override
        public void onNext(Post post) {
            postLifeCycleManager.onPost(post);
        }

        @Override
        public void onConnect() {
            serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.CONNECTED);
            android.util.Log.i("slyo", "[RPC:POST]: Connected to endpoint");
        }

        @Override
        public void onError(Throwable t) {
            android.util.Log.i("slyo", "[RPC:POST]: Connection error: " + t.getMessage());
        }

        @Override
        public void onReconnect() {
            serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.CONNECTED);
            android.util.Log.i("slyo", "[RPC:POST]: Reconnected to backend");
            postLifeCycleManager.onReconnect();
        }

        @Override
        public void onDisconnect(Throwable t) {
            serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.DISCONNECTED);
            android.util.Log.i("slyo", "[RPC:POST]: Disconnected: " + t.getMessage());
            postLifeCycleManager.onDisconnect();
        }
    };
}
