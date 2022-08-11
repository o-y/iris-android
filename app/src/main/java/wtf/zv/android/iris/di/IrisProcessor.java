package wtf.zv.android.iris.di;

import android.content.Context;
import javax.inject.Singleton;
import dagger.BindsInstance;
import dagger.Component;
import wtf.zv.android.iris.base.IrisClient;
import wtf.zv.android.iris.base.system.notification.module.NotificationModule;
import wtf.zv.android.iris.data.serializer.SerializerModule;
import wtf.zv.android.iris.helpers.sink.module.SinkModule;
import wtf.zv.android.iris.helpers.snowflake.SnowflakeModule;
import wtf.zv.android.iris.rpc.module.RpcModule;

@Singleton
@Component(modules = {
        RpcModule.class,
        NotificationModule.class,
        SinkModule.class,
        SerializerModule.class,
        SnowflakeModule.class
})
public interface IrisProcessor {
    IrisClient irisClient();

    /**
     * Creates an instance of {@link IrisProcessor} - prefer {@link DaggerIrisProcessor#factory()}.
     */
    @Deprecated
    static IrisProcessor create() {
        return IrisProcessor.create();
    }

    @Component.Factory
    interface Factory {
        IrisProcessor irisProcessor(@BindsInstance Context context);
    }
}
