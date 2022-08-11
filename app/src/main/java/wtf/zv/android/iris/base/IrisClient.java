package wtf.zv.android.iris.base;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import wtf.zv.android.iris.BaseApplication;
import wtf.zv.android.iris.base.service.IrisService;
import wtf.zv.android.iris.rpc.IrisRpcManager;

@Singleton
public class IrisClient {
    private final IrisRpcManager irisRpcManager;
    private final Context context;

    @Inject
    IrisClient(
            IrisRpcManager irisRpcManager,
            Context context
    ) {
        this.irisRpcManager = irisRpcManager;
        this.context = context;
    }

    public void onCreate() {
        Intent irisService = new Intent(context, IrisService.class);
        context.startService(irisService);
    }
}
