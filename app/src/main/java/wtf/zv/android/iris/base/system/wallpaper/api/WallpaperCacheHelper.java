package wtf.zv.android.iris.base.system.wallpaper.api;

import javax.inject.Inject;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import wtf.zv.android.iris.base.system.wallpaper.internal.WallpaperCache;
import wtf.zv.android.iris.base.system.wallpaper.internal.ScreenType;

public class WallpaperCacheHelper {
    private final WallpaperCache wallpaperCache;
    private final IrisWallpaperManager irisWallpaperManager;

    @Inject
    public WallpaperCacheHelper(
            WallpaperCache wallpaperCache,
            IrisWallpaperManager irisWallpaperManager
    ){
        this.wallpaperCache = wallpaperCache;
        this.irisWallpaperManager = irisWallpaperManager;
    }

    public Mono<WallpaperCache> cacheCurrentLockSurface(){
        return Mono
                .just(ScreenType.LOCK)
                .map(irisWallpaperManager::getSurfaceOf)
                .map(wallpaperCache::setCachedLockscreenWallpaper);
    }

    public Mono<WallpaperCache> cacheCurrentHomeSurface(){
        return Mono
                .just(ScreenType.HOME)
                .map(irisWallpaperManager::getSurfaceOf)
                .map(wallpaperCache::setCachedHomescreenWallpaper);
    }

    public Mono<Tuple2<WallpaperCache, WallpaperCache>> cacheCurrentAndroidSurfaces(){
        Mono<WallpaperCache> lockscreenCache = cacheCurrentLockSurface();
        Mono<WallpaperCache> homescreenCache = cacheCurrentHomeSurface();
        return Mono.zip(lockscreenCache, homescreenCache);
    }

    public Mono<Tuple2<WallpaperCache, WallpaperCache>> cacheInitialAndroidSurfaces(){
        Mono<WallpaperCache> lockscreenCache = Mono
                .just(ScreenType.LOCK)
                .map(irisWallpaperManager::getSurfaceOf)
                .map(wallpaperCache::setInitialLockscreenWallpaper);

        Mono<WallpaperCache> homescreenCache = Mono
                .just(ScreenType.HOME)
                .map(irisWallpaperManager::getSurfaceOf)
                .map(wallpaperCache::setInitialHomescreenWallpaper);

        return Mono.zip(lockscreenCache, homescreenCache).doFinally( unused -> {
            // Sometimes the lockscreen wallpaper will be null (not sure why), in which case we'll
            // derive from the homescreen.
            // TODO(slyo): Add logic which detects if the homescreen wallpaper is also null, if this
            //  is the case (which it shouldn't ever be) decide what to do (eg. crash, default to,
            //  etc).

            if (!wallpaperCache.getCachedLockscreenWallpaper().isPresent()){
//                android.util.Log.i("slyo-critical-2", "Lockscreen wallpaper was null - setting to homescreen wallpaper which is: " + wallpaperCache.getInitialHomescreenWallpaper());
                wallpaperCache.setInitialLockscreenWallpaper(wallpaperCache.getInitialHomescreenWallpaper());
            }
        });
    }
}
