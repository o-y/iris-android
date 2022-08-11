package wtf.zv.android.iris.base.config.internal;

import javax.inject.Inject;
import reactor.core.publisher.Mono;
import wtf.zv.android.iris.base.config.ConfigStateStore;
import wtf.zv.iris.proto.Config;
import wtf.zv.iris.proto.ReactorIrisClientServiceGrpc;
import wtf.zv.iris.proto.Status;

public class ConfigMutator {
    private final ReactorIrisClientServiceGrpc.ReactorIrisClientServiceStub reactorIrisClientServiceStub;
    private final ConfigStateStore configStateStore;

    @Inject
    ConfigMutator(
            ReactorIrisClientServiceGrpc.ReactorIrisClientServiceStub reactorIrisClientServiceStub,
            ConfigStateStore configStateStore
    ) {
        this.reactorIrisClientServiceStub = reactorIrisClientServiceStub;
        this.configStateStore = configStateStore;
    }

    public Mono<Status> setUpdateWallpaper(boolean state){
        return setState(getCurrentConfig().setUpdateHomeWallpaper(state));
    }

    public Mono<Status> setUpdateLockscreen(boolean state){
        return setState(getCurrentConfig().setUpdateLockscreenWallpaper(state));
    }

    public Mono<Status> setShowNotifications(boolean state){
        return setState(getCurrentConfig().setDisplayNotifications(state));
    }

    public Mono<Status> setDisableDaemon(boolean state){
        return setState(getCurrentConfig().setDisableDaemon(state));
    }

    private Mono<Status> setState(Config.Builder config){
        reactorIrisClientServiceStub.updateConfig(config.build()).subscribe();
        return Mono.just(Status.getDefaultInstance());
    }

    private Config.Builder getCurrentConfig(){
        return configStateStore.getStore().getConfig().toBuilder();
    }
}
