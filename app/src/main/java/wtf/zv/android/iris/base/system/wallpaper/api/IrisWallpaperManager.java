package wtf.zv.android.iris.base.system.wallpaper.api;

import android.Manifest;
import android.app.NotificationManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;
import dagger.hilt.android.qualifiers.ApplicationContext;
import wtf.zv.android.iris.BaseApplication;
import wtf.zv.android.iris.base.system.wallpaper.internal.WallpaperCache;
import wtf.zv.android.iris.base.system.wallpaper.internal.ScreenType;

public final class IrisWallpaperManager {
    private final Context context;
    private final WallpaperCacheHelper wallpaperCacheHelper;

    @Inject
    IrisWallpaperManager(
            Context context,
            WallpaperCache wallpaperCache
    ) {
        this.context = context;
        this.wallpaperCacheHelper = new WallpaperCacheHelper(wallpaperCache, this);
    }

    public boolean updateHomescreen(Bitmap bitmap) {
        return updateWallpaper(ScreenType.HOME, bitmap);
    }

    public boolean updateLockscreen(Bitmap bitmap) {
        return updateWallpaper(ScreenType.LOCK, bitmap);
    }

    public Optional<Bitmap> getLockscreenSurface() {
        Optional<Bitmap> lockscreenSurface = getSurfaceOf(ScreenType.LOCK);

        if (lockscreenSurface.isPresent()){
            return lockscreenSurface;
        } else {
            return getSurfaceOf(ScreenType.HOME);
        }
    }

    public Optional<Bitmap> getWallpaperSurface() {
        return getSurfaceOf(ScreenType.HOME);
    }

    public WallpaperCacheHelper getWallpaperCacheHelper() {
        return wallpaperCacheHelper;
    }

    private boolean areLockscreenAndWallpaperSurfacesIdentical(){
        if (!getLockscreenSurface().isPresent() && !getWallpaperSurface().isPresent()){
            // Implement logic to detect if live wallpaper
            return true;
        } else if (!getLockscreenSurface().isPresent() || !getWallpaperSurface().isPresent()){
            return false;
        }

        return getLockscreenSurface().get().sameAs(getWallpaperSurface().get());
    }

    private boolean updateWallpaper(ScreenType surface, Bitmap wallpaper){
        WallpaperManager wallpaperManager = (WallpaperManager) context.getSystemService(Context.WALLPAPER_SERVICE);

        try {
            wallpaperManager.setBitmap(
                    wallpaper,
                    null,
                    true,
                    surface == ScreenType.LOCK ? WallpaperManager.FLAG_LOCK : WallpaperManager.FLAG_SYSTEM);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    protected Optional<Bitmap> getSurfaceOf(ScreenType screenType){
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            throw new RuntimeException("Missing permission READ_EXTERNAL_STORAGE");
        }

        ParcelFileDescriptor pfd = wallpaperManager.getWallpaperFile(
                screenType == ScreenType.LOCK ? WallpaperManager.FLAG_LOCK : WallpaperManager.FLAG_SYSTEM
        );

        if (pfd == null){
            return Optional.empty();
        }

        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
        try { pfd.close(); } catch (Exception ignored){}

        return Optional.ofNullable(bitmap);
    }
}
