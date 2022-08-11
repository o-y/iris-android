package wtf.zv.android.iris.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import wtf.zv.android.iris.BaseApplication;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    Context provideContext(){
        return BaseApplication.getAppContext();
    }
}
