package wtf.zv.android.iris.base.service.state;

public class FormatServiceState {
    public static String format(ServiceState serviceState){
        switch (serviceState){
            case DISCONNECTED:
                return "Iris service disconnected";
            case CONNECTED:
                return "Iris service connected";
            case DISABLED:
                return "Iris service disabled";
            case DISABLE_DAEMON:
                return "Iris service daemon disabled mode";
        }

        return "Unknown status";
    }

    private FormatServiceState() {}
}
