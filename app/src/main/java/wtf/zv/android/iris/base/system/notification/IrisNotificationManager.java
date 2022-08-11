package wtf.zv.android.iris.base.system.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.net.Uri;

import androidx.palette.graphics.Palette;

import javax.inject.Inject;

import wtf.zv.android.iris.R;
import wtf.zv.android.iris.base.config.ConfigStateStore;
import wtf.zv.android.iris.base.system.notification.internal.NotificationUtility;
import wtf.zv.android.iris.base.system.notification.module.annotations.NewPostChannel;
import wtf.zv.android.iris.helpers.bitmap.BitmapUtility;
import wtf.zv.iris.proto.android.RetainedPost;

public class IrisNotificationManager {
    private final NotificationUtility notificationUtility;
    private final Context context;
    private final ConfigStateStore configStateStore;
    private final NotificationChannel newPostNotificationChannel;

    @Inject
    public IrisNotificationManager(
            Context context,
            NotificationUtility notificationUtility,
            ConfigStateStore configStateStore,
            @NewPostChannel NotificationChannel newPostNotificationChannel
    ){
        this.context = context;
        this.notificationUtility = notificationUtility;
        this.configStateStore = configStateStore;
        this.newPostNotificationChannel = newPostNotificationChannel;
    }


    @SuppressLint("NewApi")
    public void updateNewPostNotification(RetainedPost constructedPost, Bitmap postBitmap){
        Notification.Style bigPictureStyle = new Notification.BigPictureStyle().bigPicture(postBitmap);
        Palette bitmapPalette = BitmapUtility.createPaletteSync(postBitmap);

        PendingIntent openPostIntent = notificationUtility.createOpenBrowserIntent(Uri.parse(constructedPost.getPostUrl()));
        PendingIntent openSourceIntent = notificationUtility.createOpenBrowserIntent(Uri.parse(constructedPost.getImageUrl()));

        int lightVibrantColor = bitmapPalette.getLightVibrantColor(Color.rgb(39, 174, 96));

        newPostNotificationChannel.setLightColor(lightVibrantColor);
        newPostNotificationChannel.setAllowBubbles(true);

        if (configStateStore.getStore().getConfig().getUpdateLockscreenWallpaper()){
            newPostNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        } else {
            newPostNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(newPostNotificationChannel);

        Notification notification =
                new Notification.Builder(context, NotificationConstants.NEW_POST_NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(constructedPost.getTitle())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setOngoing(false)
                        .setColorized(true)
                        .setColor(lightVibrantColor)
                        .setStyle(new Notification.DecoratedMediaCustomViewStyle().setShowActionsInCompactView())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(postBitmap)
                        .setStyle(bigPictureStyle)
                        // Clicking on the post opens the post URL
                        .setContentIntent(openPostIntent)
                        // Clicking on the View Source button opens the image URL.
                        .addAction(new Notification.Action.Builder(
                                Icon.createWithResource(context, R.drawable.iris_logo_variant),
                                "View source", openSourceIntent).build())
                        .build();

        notificationManager.notify(NotificationConstants.NEW_POST_NOTIFICATION_ID, notification);
    }

    public void clearPostNotification(){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationConstants.NEW_POST_NOTIFICATION_ID);
    }
}
