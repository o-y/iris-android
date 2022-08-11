package wtf.zv.android.iris.ui.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import wtf.zv.android.iris.ui.components.elements.GeoLocationButton
import wtf.zv.android.iris.ui.components.elements.DisableDaemonButton
import wtf.zv.android.iris.ui.components.elements.PowerButton
import wtf.zv.android.iris.ui.models.ConfigSwitchesViewModel

@Composable
fun IrisControls(
    configSwitchesViewModel: ConfigSwitchesViewModel
){
    val defaultPadding = Modifier.padding(start = 10.dp, end = 10.dp)
    val disableDaemon by configSwitchesViewModel.disableDaemon.observeAsState(initial = false)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(10.dp)
    ) {
        PowerButton(
            defaultPadding,
            onClick = {

            }
        )

        DisableDaemonButton(
            modifier = defaultPadding,
            state = disableDaemon,
            onClick = {
                configSwitchesViewModel.setConfigState(
                    ConfigSwitchesViewModel.InternalConfigState.DISABLE_DAEMON,
                    !disableDaemon
                )
            }
        )

        GeoLocationButton(
            defaultPadding,
            onClick = {}
        )
    }
}

