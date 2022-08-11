package wtf.zv.android.iris.ui.components.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material.icons.rounded.Room
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import wtf.zv.android.iris.ui.theme.*

@Composable
fun CircularButton(
    baseColour: Color,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
    onClick: () -> Unit,
    state: Boolean
){
    var modifierPartial = modifier
        .padding(10.dp)
        .wrapContentSize(Alignment.Center)
        .clip(CircleShape);

    if (state){
        modifierPartial = modifierPartial.border(1.5.dp, baseColour, CircleShape)
    }

    Column(
        modifier = modifierPartial
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(60.dp)
                .background(baseColour.copy(alpha = 0.12f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false),
                    onClick = onClick
                ),
            content = content
        )
    }
}

@Composable
fun IconCircularButton(
    baseColour: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    state: Boolean = false
){
    CircularButton(
        baseColour = baseColour.copy(alpha = 0.5f),
        modifier = modifier,
        onClick = onClick,
        state = state,
        content = {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = icon,
                contentDescription = "Icon",
                tint = baseColour.copy(alpha = 0.6f)
            )
        }
    )
}

@Composable
fun PowerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: Boolean = false
) {
    IconCircularButton(
        baseColour = blue,
        icon = Icons.Rounded.PowerSettingsNew,
        modifier = modifier,
        onClick = onClick,
        state = state
    )
}

@Composable
fun DisableDaemonButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: Boolean = false
) {
    IconCircularButton(
        baseColour = red,
        icon = Icons.Rounded.PriorityHigh,
        modifier = modifier,
        onClick = onClick,
        state = state
    )
}

@Composable
fun GeoLocationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    state: Boolean = false
) {
    IconCircularButton(
        baseColour = green,
        icon = Icons.Rounded.Room,
        modifier = modifier,
        onClick = onClick,
        state = state
    )
}

@Preview
@Composable
private fun DisableDaemonPreviewEnabled(){
    PowerButton(
        onClick = {},
        state = true
    );
}

@Preview
@Composable
private fun DisableDaemonPreviewDisabled(){
    PowerButton(
        onClick = {},
        state = false
    );
}