package wtf.zv.android.iris.base.system.wallpaper.internal;

import android.graphics.Bitmap;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@SuppressWarnings("OptionalUsedAsFieldOrParameterType") // This is a constraint of Reactor.
public class WallpaperCache {

    @Nullable private Bitmap initialLockscreenWallpaper;
    @Nullable private Bitmap initialHomescreenWallpaper;

    @Nullable private Bitmap cachedLockscreenWallpaper;
    @Nullable private Bitmap cachedHomescreenWallpaper;

    @Inject
    WallpaperCache(){}

    public Optional<Bitmap> getInitialLockscreenWallpaper() {
        return Optional.ofNullable(initialLockscreenWallpaper);
    }

    public WallpaperCache setInitialLockscreenWallpaper(Optional<Bitmap> initialLockscreenWallpaper) {
        initialLockscreenWallpaper.ifPresent(bitmap -> this.initialLockscreenWallpaper = bitmap);
        return this;
    }

    public Optional<Bitmap> getInitialHomescreenWallpaper() {
        return Optional.ofNullable(initialHomescreenWallpaper);
    }

    public WallpaperCache setInitialHomescreenWallpaper(Optional<Bitmap> initialHomescreenWallpaper) {
        initialHomescreenWallpaper.ifPresent(bitmap -> this.initialHomescreenWallpaper = bitmap);
        return this;
    }

    public Optional<Bitmap> getCachedLockscreenWallpaper() {
        return Optional.ofNullable(cachedLockscreenWallpaper);
    }

    public WallpaperCache setCachedLockscreenWallpaper(Optional<Bitmap> cachedLockscreenWallpaper) {
        cachedLockscreenWallpaper.ifPresent(bitmap -> this.cachedLockscreenWallpaper = bitmap);
        return this;
    }

    public Optional<Bitmap> getCachedHomescreenWallpaper() {
        return Optional.ofNullable(cachedHomescreenWallpaper);
    }

    public WallpaperCache setCachedHomescreenWallpaper(Optional<Bitmap> cachedHomescreenWallpaper) {
        cachedHomescreenWallpaper.ifPresent(bitmap -> this.cachedHomescreenWallpaper = bitmap);
        return this;
    }
}
