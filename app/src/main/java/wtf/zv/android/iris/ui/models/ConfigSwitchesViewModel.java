package wtf.zv.android.iris.ui.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;
import reactor.core.publisher.Mono;
import wtf.zv.android.iris.base.config.internal.ConfigMutator;
import wtf.zv.android.iris.helpers.sink.ThreadSafeBuffer;
import wtf.zv.android.iris.base.config.internal.ThreadSafeConfigBuffer;
import wtf.zv.android.iris.ui.util.CachedMutableLiveData;
import wtf.zv.iris.proto.Config;
import wtf.zv.iris.proto.Status;

@HiltViewModel
public class ConfigSwitchesViewModel extends ViewModel {
    private final ConfigMutator configMutator;
    private final CachedMutableLiveData<Boolean> updateHomeScreenWallpaper = new CachedMutableLiveData<Boolean>(false);
    private final CachedMutableLiveData<Boolean> updateLockscreenWallpaper = new CachedMutableLiveData<Boolean>(false);
    private final CachedMutableLiveData<Boolean> displayNotifications = new CachedMutableLiveData<Boolean>(false);
    private final CachedMutableLiveData<Boolean> disableDaemon = new CachedMutableLiveData<Boolean>(false);

    private ThreadSafeBuffer<Config> configBuffer;

    @Override
    protected void onCleared() {
        configBuffer = null;
    }

    @Inject
    public ConfigSwitchesViewModel(
            SavedStateHandle handle,
            ConfigMutator configMutator,
            @ThreadSafeConfigBuffer ThreadSafeBuffer<Config> configBuffer
    ) {
        this.configBuffer = configBuffer;
        this.configMutator = configMutator;
        configBuffer.getBuffer().asFlux().subscribe(config -> {
            // In reality this lambda actually causes leaks on config changes, this should probably be looked in to...
            updateHomeScreenWallpaper.postValue(config.getUpdateHomeWallpaper());
            updateLockscreenWallpaper.postValue(config.getUpdateLockscreenWallpaper());
            disableDaemon.postValue(config.getDisableDaemon());
            displayNotifications.postValue(config.getDisplayNotifications());
        });
    }

    public MutableLiveData<Boolean> getUpdateHomeScreenWallpaper() {
        return updateHomeScreenWallpaper.getMutableLiveData();
    }

    public MutableLiveData<Boolean> getUpdateLockscreenWallpaper() {
        return updateLockscreenWallpaper.getMutableLiveData();
    }

    public MutableLiveData<Boolean> getDisplayNotifications() {
        return displayNotifications.getMutableLiveData();
    }

    public MutableLiveData<Boolean> getDisableDaemon() {
        return disableDaemon.getMutableLiveData();
    }

    public Mono<Status> setConfigState(InternalConfigState setting, boolean state){
        switch (setting){
            case WALLPAPER:
                return configMutator.setUpdateWallpaper(state);
            case LOCKSCREEN:
                return configMutator.setUpdateLockscreen(state);
            case NOTIFICATION:
                return configMutator.setShowNotifications(state);
            case DISABLE_DAEMON:
                return configMutator.setDisableDaemon(state);
        }

        throw new RuntimeException("Unknown config setting: " + setting + " - state: " + state);
    }

    public enum InternalConfigState {
        WALLPAPER, LOCKSCREEN, NOTIFICATION, DISABLE_DAEMON
    }
}
