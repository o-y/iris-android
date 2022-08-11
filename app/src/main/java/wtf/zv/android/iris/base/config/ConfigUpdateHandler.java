package wtf.zv.android.iris.base.config;

import javax.inject.Inject;

import wtf.zv.android.iris.base.service.state.ServiceState;
import wtf.zv.android.iris.base.service.state.internal.ThreadSafeServiceStateBuffer;
import wtf.zv.android.iris.base.system.notification.IrisNotificationManager;
import wtf.zv.android.iris.base.system.wallpaper.api.IrisWallpaperManager;
import wtf.zv.android.iris.base.system.wallpaper.api.WallpaperCacheHelper;
import wtf.zv.android.iris.base.system.wallpaper.internal.WallpaperCache;
import wtf.zv.android.iris.helpers.sink.ThreadSafeBuffer;
import wtf.zv.iris.proto.Config;

public class ConfigUpdateHandler {
    private final ConfigStateStore configStateStore;
    private final IrisWallpaperManager irisWallpaperManager;
    private final WallpaperCache wallpaperCache;
    private final IrisNotificationManager irisNotificationManager;
    private final WallpaperCacheHelper wallpaperCacheHelper;
    private final ThreadSafeBuffer<ServiceState> serviceStateBuffer;

    @Inject
    public ConfigUpdateHandler(
            ConfigStateStore configStateStore,
            IrisWallpaperManager irisWallpaperManager,
            WallpaperCache wallpaperCache,
            IrisNotificationManager irisNotificationManager,
            WallpaperCacheHelper wallpaperCacheHelper,
            @ThreadSafeServiceStateBuffer ThreadSafeBuffer<ServiceState> serviceStateBuffer
    ){
        this.configStateStore = configStateStore;
        this.irisWallpaperManager = irisWallpaperManager;
        this.wallpaperCache = wallpaperCache;
        this.irisNotificationManager = irisNotificationManager;
        this.wallpaperCacheHelper = wallpaperCacheHelper;
        this.serviceStateBuffer = serviceStateBuffer;
    }

    public Config onConfig(Config config){
        Config oldConfig = configStateStore.getStore().getConfig();
        configStateStore.updateStore(config);

        if (oldConfig.equals(config)){
            return config;
        }

        if (config.getDisableDaemon()){
            wallpaperCacheHelper.cacheCurrentAndroidSurfaces().subscribe(
                    ignored -> {
                        wallpaperCache.getInitialLockscreenWallpaper().ifPresent(irisWallpaperManager::updateLockscreen);
                        wallpaperCache.getInitialHomescreenWallpaper().ifPresent(irisWallpaperManager::updateHomescreen);
                    }
            );

            irisNotificationManager.clearPostNotification();
            serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.DISABLE_DAEMON);
            return config;
        }

        if (!config.getDisplayNotifications()){
            irisNotificationManager.clearPostNotification();
        }

        if (!config.getUpdateLockscreenWallpaper()){
            wallpaperCacheHelper.cacheCurrentLockSurface().subscribe(
                    ignored -> wallpaperCache.getInitialLockscreenWallpaper().ifPresent(irisWallpaperManager::updateLockscreen)
            );
        }

        if (!config.getUpdateHomeWallpaper()){
            wallpaperCacheHelper.cacheCurrentHomeSurface().subscribe(
                    ignored -> wallpaperCache.getInitialHomescreenWallpaper().ifPresent(irisWallpaperManager::updateHomescreen)
            );
        }

        if (!config.getDisplayNotifications() && !config.getUpdateLockscreenWallpaper() && !config.getUpdateHomeWallpaper()){
            serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.DISABLED);
        } else {
            serviceStateBuffer.getBuffer().tryEmitNext(ServiceState.CONNECTED);
        }

        if (oldConfig.getDisableDaemon()){
            if (config.getUpdateLockscreenWallpaper()){
                wallpaperCache.getCachedLockscreenWallpaper().ifPresent(irisWallpaperManager::updateLockscreen);
            }

            if (config.getUpdateHomeWallpaper()){
                wallpaperCache.getCachedHomescreenWallpaper().ifPresent(irisWallpaperManager::updateHomescreen);
            }

            return config;
        }

        if (!oldConfig.getUpdateHomeWallpaper() && config.getUpdateHomeWallpaper()){
            wallpaperCache.getCachedHomescreenWallpaper().ifPresent(irisWallpaperManager::updateHomescreen);
        }

        if (!oldConfig.getUpdateLockscreenWallpaper() && config.getUpdateLockscreenWallpaper()){
            wallpaperCache.getCachedLockscreenWallpaper().ifPresent(irisWallpaperManager::updateLockscreen);
        }

        return config;
    }
}
