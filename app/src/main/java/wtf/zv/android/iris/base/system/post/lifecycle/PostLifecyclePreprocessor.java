package wtf.zv.android.iris.base.system.post.lifecycle;

import javax.inject.Inject;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import wtf.zv.android.iris.base.config.ConfigStateStore;
import wtf.zv.android.iris.base.config.ConfigUpdateHandler;
import wtf.zv.android.iris.base.system.notification.IrisNotificationManager;
import wtf.zv.android.iris.base.system.post.handlers.PostHandler;
import wtf.zv.android.iris.base.system.post.helpers.PostExtractor;
import wtf.zv.android.iris.base.system.wallpaper.api.IrisWallpaperManager;
import wtf.zv.android.iris.base.system.wallpaper.internal.WallpaperCache;
import wtf.zv.android.iris.base.system.wallpaper.internal.WallpaperLifecycleManager;
import wtf.zv.android.iris.base.config.internal.ConfigUtility;
import wtf.zv.android.iris.rpc.IrisRpcChannel;
import wtf.zv.iris.proto.Config;
import wtf.zv.iris.proto.Post;
import wtf.zv.iris.proto.android.RetainedPost;

public final class PostLifecyclePreprocessor {

    private final ConfigStateStore configStateStore;
    private final IrisWallpaperManager irisWallpaperManager;
    private final WallpaperCache wallpaperCache;
    private final PostHandler postHandler;
    private final IrisRpcChannel irisRpcChannel;
    private final ConfigUpdateHandler configUpdateHandler;
    private final WallpaperLifecycleManager wallpaperLifecycleManager;
    private final IrisNotificationManager irisNotificationManager;

    @Inject
    public PostLifecyclePreprocessor(
            ConfigStateStore configStateStore,
            IrisWallpaperManager irisWallpaperManager,
            WallpaperCache wallpaperCache,
            PostHandler postHandler,
            IrisRpcChannel irisRpcChannel,
            ConfigUpdateHandler configUpdateHandler,
            WallpaperLifecycleManager wallpaperLifecycleManager,
            IrisNotificationManager irisNotificationManager
    ){
        this.configStateStore = configStateStore;
        this.irisWallpaperManager = irisWallpaperManager;
        this.wallpaperCache = wallpaperCache;
        this.postHandler = postHandler;
        this.irisRpcChannel = irisRpcChannel;
        this.configUpdateHandler = configUpdateHandler;
        this.wallpaperLifecycleManager = wallpaperLifecycleManager;
        this.irisNotificationManager = irisNotificationManager;
    }

    public void onPost(Post post){
        Config configStoreCache = configStateStore.getStore().getConfig();

        if (ConfigUtility.isDisabled(configStoreCache)){
            android.util.Log.i("slyo", "Ignoring post as the daemon or all configs are disabled");
            return;
        }

        RetainedPost retainedPost = PostExtractor.transformPost(post);
        PostExtractor.extractBitmapFromPost(retainedPost)
                .ifPresent(bitmap -> postHandler.handleNewValidPost(retainedPost, bitmap));
    }

    public Mono<Tuple2<WallpaperCache, WallpaperCache>> onStartUp(){
        android.util.Log.i("slyo", "Caching initial Android surfaces");
        return irisWallpaperManager.getWallpaperCacheHelper().cacheInitialAndroidSurfaces();
    }

    public void onReconnect(){
        ConfigStateStore.CurrentConfig store = configStateStore.getStore();

        if ((System.nanoTime() - store.getReceivedAt()) > 1e-9 * 5){
            // The config was received from the server in the last 5 seconds, therefore we do not
            // need to request it again
            wallpaperLifecycleManager.onReconnect(store.getConfig());
        } else {
            // The config is not fresh, therefore we should make a gRPC request to the config
            // backend and then call the reconnect logic.
            irisRpcChannel
                    .requestLatestConfig()
                    .map(configUpdateHandler::onConfig)
                    .subscribe(wallpaperLifecycleManager::onReconnect);
        }
    }

    public void onDisconnect(){
        irisWallpaperManager
                .getWallpaperCacheHelper()
                .cacheCurrentAndroidSurfaces()
                .subscribe(unused -> {
                    wallpaperCache.getInitialHomescreenWallpaper().ifPresent(irisWallpaperManager::updateHomescreen);
                    wallpaperCache.getInitialLockscreenWallpaper().ifPresent(irisWallpaperManager::updateLockscreen);
                    irisNotificationManager.clearPostNotification();
                });
    }
}
