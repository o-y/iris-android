package wtf.zv.android.iris.ui.components.topbar

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import poppinsFontFamily
import wtf.zv.android.iris.base.service.state.ServiceState
import wtf.zv.android.iris.ui.models.StatusViewModel
import wtf.zv.android.iris.ui.theme.blue
import wtf.zv.android.iris.ui.theme.orange
import wtf.zv.android.iris.ui.theme.red
import java.util.*
import kotlin.concurrent.schedule

@ExperimentalAnimationApi
@Composable
fun IrisStatusTopBar(
    statusViewModel: StatusViewModel
) {
    val infiniteTransition = rememberInfiniteTransition()
    val opacityTransition by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val irisRpcEnabled by statusViewModel.serviceState.observeAsState(initial = ServiceState.DISCONNECTED)

    val density = LocalDensity.current;

//    var display by remember { mutableStateOf(false) }
//    Timer("animateVisibility", false).schedule(1000) {
//        display = true;
//    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .height(110.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { with(density) { -40.dp.roundToPx() } }
                ) + fadeIn(initialAlpha = 0.3f)
            ) {
                Text(
                    text = "Iris",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 50.sp
                )
            }

            AnimatedVisibility(
                visible = true,
                enter = slideInHorizontally(
                    initialOffsetX = { with(density) { 40.dp.roundToPx() } }
                ) + fadeIn(initialAlpha = 0.3f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .wrapContentSize(Alignment.Center)
                        .clip(CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .size(13.dp)
                            .background(
                                serviceStateToColour(irisRpcEnabled).copy(alpha = opacityTransition))
                    )
                }
            }
        }
    }
}

fun serviceStateToColour(serviceState: ServiceState): Color {
    return when (serviceState) {
        ServiceState.CONNECTED -> blue
        ServiceState.DISCONNECTED -> red
        else -> orange
    }
}