package wtf.zv.android.iris.base.config.internal;

import wtf.zv.iris.proto.Config;

public final class ConfigUtility {
    public static boolean isDisabled(Config config){
        return config.getDisableDaemon() || (!config.getUpdateHomeWallpaper()
                && !config.getUpdateLockscreenWallpaper()
                && !config.getDisplayNotifications());
    }
}
