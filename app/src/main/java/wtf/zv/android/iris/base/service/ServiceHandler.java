package wtf.zv.android.iris.base.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import javax.inject.Inject;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import wtf.zv.android.iris.base.config.ConfigStateStore;
import wtf.zv.android.iris.rpc.IrisRpcManager;

public class ServiceHandler extends Handler {

    private final IrisRpcManager irisRpcManager;

    @AssistedInject
    public ServiceHandler(
            @Assisted Looper looper,
            IrisRpcManager irisRpcManager
    ) {
        super(looper);
        this.irisRpcManager = irisRpcManager;
    }

    @Override
    public void handleMessage(Message msg) {
        android.util.Log.i("slyo", "Initiating service and gRPC client");
        irisRpcManager.start();
    }

    @AssistedFactory
    public interface ServiceHandlerFactory {
        ServiceHandler create(Looper looper);
    }
}
