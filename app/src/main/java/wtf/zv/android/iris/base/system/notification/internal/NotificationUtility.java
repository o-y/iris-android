package wtf.zv.android.iris.base.system.notification.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import javax.inject.Inject;

public class NotificationUtility {

    private final Context context;

    @Inject
    NotificationUtility(
            Context context
    ){
        this.context = context;
    }

    public PendingIntent createOpenBrowserIntent(Uri uri){
        return PendingIntent.getActivity(
                context,0, new Intent(Intent.ACTION_VIEW).setData(uri), PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
