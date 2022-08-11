import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import wtf.zv.android.iris.R

@Preview
@Composable
fun IrisLogoVariantCard(){
    IrisLogoCard(R.drawable.iris_logo_variant)
}

@Preview
@Composable
fun IrisLogoCard(){
    IrisLogoCard(R.drawable.iris_logo)
}

@Composable
private fun IrisLogoCard(@DrawableRes id: Int){
    Card(
        elevation = 10.dp
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IrisLogo(id)
        }
    }
}

@Composable
private fun IrisLogo(@DrawableRes id: Int){
    Image(
        painter = painterResource(id),
        contentDescription = "test",
        modifier = Modifier
            .width(265.dp)
            .padding(50.dp)
    )
}