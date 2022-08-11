package wtf.zv.android.iris.rpc.internal;

import com.google.protobuf.Empty;

import javax.inject.Inject;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wtf.zv.iris.proto.Config;
import wtf.zv.iris.proto.Post;
import wtf.zv.iris.proto.ReactorIrisClientServiceGrpc;
import wtf.zv.iris.proto.Status;

public final class RpcProviders {

    private final ReactorIrisClientServiceGrpc.ReactorIrisClientServiceStub irisClientServiceStub;

    @Inject
    RpcProviders(ReactorIrisClientServiceGrpc.ReactorIrisClientServiceStub irisClientServiceStub){
        this.irisClientServiceStub = irisClientServiceStub;
    }

    public Flux<Post> createPostProvider(){
        return irisClientServiceStub.provideNewPosts(Empty.newBuilder().build());
    }

    public Flux<Config> createConfigStreamProvider(){
        return irisClientServiceStub.provideConfigStream(Empty.newBuilder().build());
    }

    public Mono<Config> createConfigProvider(){
        return irisClientServiceStub.provideConfig(Empty.newBuilder().build());
    }

    public Mono<Status> createConfigSupplier(Mono<Config> config){
        return irisClientServiceStub.updateConfig(config);
    }
}
