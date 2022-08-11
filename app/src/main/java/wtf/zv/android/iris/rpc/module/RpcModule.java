package wtf.zv.android.iris.rpc.module;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import wtf.zv.iris.proto.ReactorIrisClientServiceGrpc;

@Module
@InstallIn(SingletonComponent.class)
public class RpcModule {
    @Provides
    @Singleton
    ReactorIrisClientServiceGrpc.ReactorIrisClientServiceStub provideChannelStub() {
        ManagedChannel rpcChannel = ManagedChannelBuilder.forAddress("zv.wtf", 2345)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .build();
        return ReactorIrisClientServiceGrpc.newReactorStub(rpcChannel);
    }
}
