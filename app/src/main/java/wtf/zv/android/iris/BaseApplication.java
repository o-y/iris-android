package wtf.zv.android.iris;

import android.app.Application;
import android.content.Context;

import dagger.hilt.android.HiltAndroidApp;
import wtf.zv.android.iris.base.IrisClient;
import wtf.zv.android.iris.di.DaggerIrisProcessor;

@HiltAndroidApp
public class BaseApplication extends Application {
    private static Context applicationContext;

    @Override
    public void onCreate() {
        BaseApplication.applicationContext = getApplicationContext();
        createIrisClient().onCreate();
        super.onCreate();
    }

    public static Context getAppContext() {
        return BaseApplication.applicationContext;
    }

    private IrisClient createIrisClient() {
        return DaggerIrisProcessor.factory().irisProcessor(getApplicationContext()).irisClient();
    }
}
