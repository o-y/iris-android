package wtf.zv.android.iris.ui.components.recents

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.OpenInNew
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import poppinsFontFamily
import wtf.zv.android.iris.ui.theme.blue
import androidx.core.content.ContextCompat.startActivity
import android.net.Uri
import android.content.Intent
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import wtf.zv.android.iris.R

@Composable
fun RecentsCard(
    heading: String,
    date: String,
    url: String,

    imageUrl: String,
    postUrl: String,

    modifier: Modifier = Modifier,
    debugMode: Boolean = false,

){
    val context = LocalContext.current;
    val imagePainter = rememberImagePainter(url);

    Card(
        elevation = 4.dp,
        modifier = modifier
    ) {
        Column() {
            BoxWithConstraints (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                contentAlignment = Alignment.Center
            ){
                if (debugMode){
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.iris_logo_variant),
                        contentDescription = heading,
                    )
                } else {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = imagePainter,
                        contentDescription = heading,
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, top = 10.dp)
            ) {
                Text(
                    text = heading,
                    fontSize = 14.sp,
                    fontFamily = poppinsFontFamily,
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                )

                Text(
                    text = date,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )

                Row(
                    modifier = Modifier.padding(top = 0.dp)
                ) {
                    IconButton(
                        onClick = { openUrl(context, postUrl) },
                    ) {
                        Icon(
                            modifier = Modifier.size(23.dp),
                            imageVector = Icons.Rounded.OpenInNew,
                            contentDescription = "Icon",
                            tint = Color.Black.copy(alpha = 0.5f),
                        )
                    }

                    IconButton(
                        onClick = { openUrl(context, imageUrl) },
                    ) {
                        Icon(
                            modifier = Modifier.size(23.dp),
                            imageVector = Icons.Rounded.Image,
                            contentDescription = "Icon",
                            tint = Color.Black.copy(alpha = 0.5f),
                        )
                    }
                }
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW).setData(Uri.parse(url))
    startActivity(context, intent, null);
}

@Preview
@Composable
fun ConfigSwitchesPreview(){
    RecentsCard(
        heading = "NASA - a Dazzling Dynamic Duo",
        date = "1 hour ago",
        debugMode = true,
        url = "https://www.nasa.gov/sites/default/files/styles/full_width_feature/public/thumbnails/image/potw2125a.jpg",
        imageUrl = "",
        postUrl = ""
    )
}