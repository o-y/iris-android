package wtf.zv.android.iris.base.system.notification;

public interface NotificationConstants {
    // Channel used for new post notifications
    String NEW_POST_NOTIFICATION_CHANNEL_ID = "wtf.zv.android.iris.base.system.notification#NEW_POST_NOTIFICATION_CHANNEL_ID";
    String NEW_POST_NOTIFICATION_CHANNEL_NAME = "Iris Beyond - New Post notifier";
    // This is the ID associated with the new post notifications. By keeping it static it means there
    // will only ever be one notification.
    int NEW_POST_NOTIFICATION_ID = 64 << 4;

    // Channel used for the persistent service notification - can not be dismissed.
    String SERVICE_NOTIFICATION_CHANNEL_ID = "wtf.zv.android.iris.base.system.notification#SERVICE_NOTIFICATION_CHANNEL_ID";
    String SERVICE_NOTIFICATION_CHANNEL_NAME = "Iris Beyond - Persistent Service notifier";
    int SERVICE_NOTIFICATION_ID = 64 << 8;
}
