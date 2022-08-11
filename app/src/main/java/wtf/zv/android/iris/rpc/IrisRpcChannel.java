package wtf.zv.android.iris.rpc;

import javax.inject.Inject;
import javax.inject.Singleton;

import reactor.core.publisher.Mono;
import wtf.zv.android.iris.rpc.internal.RpcProviders;
import wtf.zv.android.iris.rpc.internal.observers.RpcObserver;
import wtf.zv.android.iris.rpc.internal.observers.SingleSubscriptionObserver;
import wtf.zv.android.iris.rpc.internal.observers.StreamSubscriptionObserver;
import wtf.zv.android.iris.rpc.internal.observers.RpcStreamObserver;
import wtf.zv.iris.proto.Config;
import wtf.zv.iris.proto.Post;
import wtf.zv.iris.proto.Status;

@Singleton
public class IrisRpcChannel {
    private final RpcProviders rpcProviders;
    private final RpcStreamObserver.RpcStreamObserverFactory<Post> postStreamObserver;
    private final RpcStreamObserver.RpcStreamObserverFactory<Config> configObserver;

    @Inject
    IrisRpcChannel(
            RpcProviders rpcProviders,
            RpcStreamObserver.RpcStreamObserverFactory<Post> postStreamObserver,
            RpcStreamObserver.RpcStreamObserverFactory<Config> configObserver
    ) {
        this.rpcProviders = rpcProviders;
        this.postStreamObserver = postStreamObserver;
        this.configObserver = configObserver;
    }

    public void createPostStreamObserver(StreamSubscriptionObserver<Post> postStreamSubscriptionObserver){
        postStreamObserver.create(rpcProviders::createPostProvider, postStreamSubscriptionObserver)
                .subscribe();
    }

    public void createConfigObserver(StreamSubscriptionObserver<Config> singleSubscriptionObserver){
        configObserver.create(rpcProviders::createConfigStreamProvider, singleSubscriptionObserver)
                .subscribe();
    }

    public Mono<Config> requestLatestConfig(){
        // This makes a request to the RPC server. Potentially pass a {@code useCache} prop, which
        // if false wouldn't make a HTTP request.
        return rpcProviders.createConfigProvider();
    }

    public Mono<Status> updateServerConfig(Mono<Config> config){
        return rpcProviders.createConfigSupplier(config);
    }
}

