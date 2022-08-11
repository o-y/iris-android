package wtf.zv.android.iris.base.system.notification.module;

import android.app.NotificationChannel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import wtf.zv.android.iris.base.system.notification.NotificationConstants;
import wtf.zv.android.iris.base.system.notification.module.annotations.NewPostChannel;
import wtf.zv.android.iris.base.system.notification.module.annotations.PersistentServiceChannel;

@Module
@InstallIn(SingletonComponent.class)
public class NotificationModule {
    @Provides
    @NewPostChannel
    @Singleton
    NotificationChannel provideNewPostChannel(){
        return new NotificationChannel(
                NotificationConstants.NEW_POST_NOTIFICATION_CHANNEL_ID,
                NotificationConstants.NEW_POST_NOTIFICATION_CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_HIGH
        );
    }

    @Provides
    @PersistentServiceChannel
    @Singleton
    NotificationChannel providePersistentServiceChannel(){
        return new NotificationChannel(
                NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_ID,
                NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_NAME,
                android.app.NotificationManager.IMPORTANCE_HIGH
        );
    }
}
