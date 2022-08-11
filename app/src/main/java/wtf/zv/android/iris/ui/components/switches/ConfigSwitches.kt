package wtf.zv.android.iris.ui.components.switches

import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import wtf.zv.android.iris.base.service.state.ServiceState
import wtf.zv.android.iris.ui.components.elements.SwitchIrisCard
import wtf.zv.android.iris.ui.models.StatusViewModel
import wtf.zv.android.iris.ui.models.ConfigSwitchesViewModel
import wtf.zv.android.iris.ui.theme.green

@ExperimentalMaterialApi
@Composable
fun ConfigSwitches(
    modifier: Modifier = Modifier,
    configSwitchesViewModel: ConfigSwitchesViewModel,
    statusViewModel: StatusViewModel
) {
    val displayNotifications by configSwitchesViewModel.displayNotifications.observeAsState(initial = false)
    val updateHomeScreenWallpaper by configSwitchesViewModel.updateHomeScreenWallpaper.observeAsState(initial = false)
    val updateLockscreenWallpaper by configSwitchesViewModel.updateLockscreenWallpaper.observeAsState(initial = false)
    val serviceState by statusViewModel.serviceState.observeAsState(initial = ServiceState.DISCONNECTED)

    SwitchIrisCard(
        modifier = modifier.padding(top = 30.dp),

        heading = "Display notification",
        subheading = "Displays new post notifications",

        icon = Icons.Rounded.NotificationsNone,
        iconColor = green,

        checked = displayNotifications,
        loading = !isServiceConnected(serviceState),

        onClick = {
            android.util.Log.i("slyo", "Clicked toggle notification status")
            configSwitchesViewModel.setConfigState(
                ConfigSwitchesViewModel.InternalConfigState.NOTIFICATION, !displayNotifications
            )
        }
    )

    SwitchIrisCard(
        modifier = modifier.padding(top = 30.dp),

        heading = "Update wallpaper",
        subheading = "Update wallpaper on new posts",

        icon = Icons.Rounded.Wallpaper,
        iconColor = green,

        checked = updateHomeScreenWallpaper,
        loading = !isServiceConnected(serviceState),

        onClick = {
            configSwitchesViewModel.setConfigState(
                ConfigSwitchesViewModel.InternalConfigState.WALLPAPER, !updateHomeScreenWallpaper
            )
        }
    )

    SwitchIrisCard(
        modifier = modifier.padding(top = 30.dp),

        heading = "Update lockscreen",
        subheading = "Update lockscreen on new posts",

        icon = Icons.Rounded.Lock,
        iconColor = green,

        checked = updateLockscreenWallpaper,
        loading = !isServiceConnected(serviceState),

        onClick = {
            configSwitchesViewModel.setConfigState(
                ConfigSwitchesViewModel.InternalConfigState.LOCKSCREEN, !updateLockscreenWallpaper
            )
        }
    )
}

fun isServiceConnected(serviceState: ServiceState): Boolean {
    return when (serviceState) {
        ServiceState.DISCONNECTED -> false
        else -> true
    }
}