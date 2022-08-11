package wtf.zv.android.iris.base.system.post.handlers;

import android.graphics.Bitmap;

import javax.inject.Inject;

import wtf.zv.android.iris.base.config.ConfigStateStore;
import wtf.zv.android.iris.base.system.notification.IrisNotificationManager;
import wtf.zv.android.iris.base.system.wallpaper.api.IrisWallpaperManager;
import wtf.zv.iris.proto.Config;
import wtf.zv.iris.proto.android.RetainedPost;

public final class PostHandler {

    private final ConfigStateStore configStateStore;
    private final IrisWallpaperManager wallpaperManager;
    private final IrisNotificationManager irisNotificationManager;
    private final PostRetentionService postRetentionService;

    @Inject
    PostHandler(
            ConfigStateStore configStateStore,
            IrisWallpaperManager wallpaperManager,
            IrisNotificationManager irisNotificationManager,
            PostRetentionService postRetentionService
    ) {
        this.configStateStore = configStateStore;
        this.wallpaperManager = wallpaperManager;
        this.irisNotificationManager = irisNotificationManager;
        this.postRetentionService = postRetentionService;
    }

    public void handleNewValidPost(RetainedPost post, Bitmap postBitmap){
        Config config = configStateStore.getStore().getConfig();

        postRetentionService.retainPost(post);

        if (config.getUpdateHomeWallpaper()){
            wallpaperManager.updateHomescreen(postBitmap);
        }

        if (config.getUpdateLockscreenWallpaper()){
            wallpaperManager.updateLockscreen(postBitmap);
        }

        if (config.getDisplayNotifications()){
            irisNotificationManager.updateNewPostNotification(post, postBitmap);
        }

        if (!config.equals(configStateStore.getStore().getConfig())){
            // TODO(slyo): The config has changed whilst some work has happened, find out what and
            //  if applicable - reverse it. At this point we know the Config did allow for either:
            //  - updating the homescreen
            //  - updating the lockscreen
            //  - showing notifications
            android.util.Log.i("slyo", "*** Panic - config mutation during work");
        }
    }
}
