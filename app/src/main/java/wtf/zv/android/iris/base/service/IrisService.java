package wtf.zv.android.iris.base.service;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.HandlerThread;
import android.os.IBinder;

import androidx.annotation.Nullable;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import wtf.zv.android.iris.MainActivity;
import wtf.zv.android.iris.R;
import wtf.zv.android.iris.base.service.state.FormatServiceState;
import wtf.zv.android.iris.base.service.state.ServiceState;
import wtf.zv.android.iris.base.service.state.internal.ThreadSafeServiceStateBuffer;
import wtf.zv.android.iris.base.system.notification.NotificationConstants;
import wtf.zv.android.iris.base.system.post.lifecycle.PostLifecyclePreprocessor;
import wtf.zv.android.iris.helpers.sink.ThreadSafeBuffer;
import wtf.zv.android.iris.rpc.IrisRpcManager;

@AndroidEntryPoint
public class IrisService extends Service {

    @Inject ServiceHandler.ServiceHandlerFactory serviceHandlerFactory;
    @Inject PostLifecyclePreprocessor postLifecyclePreprocessor;
    @Inject @ThreadSafeServiceStateBuffer ThreadSafeBuffer<ServiceState> serviceStateBuffer;

    private ServiceHandler serviceHandler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Notification notification = buildNotification(FormatServiceState.format(ServiceState.DISCONNECTED));
        startForeground(NotificationConstants.SERVICE_NOTIFICATION_ID, notification);

        HandlerThread thread = new HandlerThread("ServiceStartArguments", THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceHandler = serviceHandlerFactory.create(thread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceStateBuffer
                .getBuffer()
                .asFlux()
                .map(FormatServiceState::format)
                .map(this::updateNotificationText)
                .subscribe();

        serviceHandler.sendMessage(serviceHandler.obtainMessage());
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        android.util.Log.i("slyo", "Service being destroyed");
        postLifecyclePreprocessor.onDisconnect();
    }

    private Notification buildNotification(String notificationText){
        NotificationChannel notificationChannel = new NotificationChannel(
                NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_ID,
                NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_MIN);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(notificationChannel);

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

        return new Notification.Builder(this, NotificationConstants.SERVICE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(notificationText)
                .setColorized(true)
                .setColor(Color.WHITE)
                .setStyle(new Notification.DecoratedMediaCustomViewStyle().setShowActionsInCompactView())
                .setContentIntent(intent)
                .build();
    }

    private Notification updateNotificationText(String notificationText){
        Notification notification = buildNotification(notificationText);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NotificationConstants.SERVICE_NOTIFICATION_ID, notification);

        return notification;
    }
}
