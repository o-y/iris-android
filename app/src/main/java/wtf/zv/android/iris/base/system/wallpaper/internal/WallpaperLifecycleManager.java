package wtf.zv.android.iris.base.system.wallpaper.internal;

import javax.inject.Inject;

import wtf.zv.android.iris.base.system.wallpaper.api.IrisWallpaperManager;
import wtf.zv.iris.proto.Config;

public final class WallpaperLifecycleManager {

    private final WallpaperCache wallpaperCache;
    private final IrisWallpaperManager irisWallpaperManager;

    @Inject
    WallpaperLifecycleManager(
            WallpaperCache wallpaperCache,
            IrisWallpaperManager irisWallpaperManager
    ) {
        this.wallpaperCache = wallpaperCache;
        this.irisWallpaperManager = irisWallpaperManager;
    }

    public void onReconnect(Config config) {
        if (config.getDisableDaemon()){
            return;
        }

        if (config.getUpdateLockscreenWallpaper()){
            wallpaperCache.getCachedLockscreenWallpaper().ifPresent(
                    irisWallpaperManager::updateLockscreen
            );
        }

        if (config.getUpdateHomeWallpaper()){
            wallpaperCache.getCachedHomescreenWallpaper().ifPresent(
                    irisWallpaperManager::updateHomescreen
            );
        }

        if (config.getDisplayNotifications()){
            // Fetch most recent RetentionPost from the cache
            // -> NotificationManager#showEpherealNotification
        }
    }
}
