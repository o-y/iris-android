package wtf.zv.android.iris.helpers.sink.module;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import wtf.zv.android.iris.base.service.state.ServiceState;
import wtf.zv.android.iris.base.service.state.internal.ThreadSafeServiceStateBuffer;
import wtf.zv.android.iris.helpers.sink.ThreadSafeBuffer;
import wtf.zv.android.iris.base.config.internal.ThreadSafeConfigBuffer;
import wtf.zv.iris.proto.Config;

@Module
@InstallIn(SingletonComponent.class)
public abstract class SinkModule {
    @Binds
    @Singleton
    @ThreadSafeConfigBuffer
    abstract ThreadSafeBuffer<Config> configBuffer(ThreadSafeBuffer<Config> threadSafeBuffer);

    @Binds
    @Singleton
    @ThreadSafeServiceStateBuffer
    abstract ThreadSafeBuffer<ServiceState> serviceStateBuffer(ThreadSafeBuffer<ServiceState> threadSafeBuffer);
}