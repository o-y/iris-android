package wtf.zv.android.iris.ui.components.elements

import androidx.annotation.Nullable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import poppinsFontFamily
import wtf.zv.android.iris.ui.theme.green
import wtf.zv.android.iris.ui.theme.grey
import wtf.zv.android.iris.ui.theme.lightGrey
import wtf.zv.android.iris.ui.theme.orange

@ExperimentalMaterialApi
@Composable
fun BaseIrisCard(
    modifier: Modifier = Modifier,

    heading: String,
    subheading: String,
    icon: ImageVector,
    iconColor: Color,

    content: @Composable RowScope.() -> Unit,
    @Nullable onClick: ((Boolean) -> Unit)? = null
) {
    OnClickHelperUtil(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier.scale(0.85f)
                ) {
                    IconCircularButton(
                        baseColour = iconColor,
                        icon = icon
                    )
                }
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 20.dp)
                ) {
                    Column {
                        Text(
                            text = heading,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.W500,
                            color = Color.Black,
                            fontSize = 16.5.sp
                        )

                        Text(
                            text = subheading,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black.copy(alpha = 0.5f),
                            fontSize = 12.sp
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterVertically)
                    ) {
                        content()
                    }
                }
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
private fun OnClickHelperUtil(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    @Nullable onClick: ((Boolean) -> Unit)? = null
){
    if (onClick == null){
        Card(
            elevation = 3.dp,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            content = content
        )
    } else {
        Card(
            elevation = 3.dp,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            onClick = { onClick.invoke(true) },
            content = content
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun SwitchIrisCard(
    modifier: Modifier = Modifier,

    heading: String,
    subheading: String,
    icon: ImageVector,
    iconColor: Color,

    checked: Boolean = false,
    loading: Boolean = false,

    onClick: ((Boolean) -> Unit)?
){
    BaseIrisCard(
        modifier = modifier,
        heading = heading,
        icon = icon,
        iconColor = iconColor,
        subheading = subheading,

        content = {
            if (loading){
                CircularProgressIndicator()
            } else {
                Switch(
                    checked = checked,
                    onCheckedChange = onClick
                )
            }
        }
    )
}

@ExperimentalMaterialApi
@Composable
fun ActionIrisCard(
    modifier: Modifier = Modifier,

    heading: String,
    subheading: String,
    icon: ImageVector,
    iconColor: Color,

    onClick: ((Boolean) -> Unit)?
){
    BaseIrisCard(
        modifier = modifier,
        heading = heading,
        subheading = subheading,
        icon = icon,
        iconColor = iconColor,

        onClick = onClick,

        content = {
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = "Icon",
                tint = grey
            )
        }
    )
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun DemoSwitchIrisCard() {
    ActionIrisCard(
        heading = "View previous posts",
        subheading = "405 cached posts",

        icon = Icons.Rounded.NotificationsNone,
        iconColor = green,

        onClick = {}
    )
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun DemoActionIrisCard() {
    ActionIrisCard(
        heading = "Display notification",
        subheading = "Shows a notification on new posts",

        icon = Icons.Rounded.History,
        iconColor = orange,

        onClick = {}
    )
}