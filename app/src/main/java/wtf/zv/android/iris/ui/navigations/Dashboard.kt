package wtf.zv.android.iris.ui.navigations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.History
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import wtf.zv.android.iris.ui.components.elements.ActionIrisCard
import wtf.zv.android.iris.ui.components.switches.ConfigSwitches
import wtf.zv.android.iris.ui.components.topbar.IrisControls
import wtf.zv.android.iris.ui.components.topbar.IrisStatusTopBar
import wtf.zv.android.iris.ui.models.ConfigSwitchesViewModel
import wtf.zv.android.iris.ui.models.StatusViewModel
import wtf.zv.android.iris.ui.models.RetainedPostsViewModelKt
import wtf.zv.android.iris.ui.theme.blue
import wtf.zv.android.iris.ui.theme.lightGrey
import wtf.zv.android.iris.ui.util.Routes

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun Dashboard(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController
){
    val statusViewModel = viewModel<StatusViewModel>(factory = viewModelFactory);
    val configSwitchesViewModel = viewModel<ConfigSwitchesViewModel>(factory = viewModelFactory);
    val retainedPostsViewModel = viewModel<RetainedPostsViewModelKt>(factory = viewModelFactory);

    val defaultCardPadding = Modifier.padding(start = 15.dp, end = 15.dp);

    val postsCount by retainedPostsViewModel.getPostsCount().observeAsState(initial = 0)

    Column(
        Modifier.background(color = Color.White)
    ) {
        IrisStatusTopBar(statusViewModel = statusViewModel)

        Box(
            Modifier.padding(bottom = 50.dp)
        ) {
            IrisControls(configSwitchesViewModel = configSwitchesViewModel)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                .background(color = lightGrey.copy(alpha = 0.3f))
        ){
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ) {
                ActionIrisCard(
                    modifier = defaultCardPadding,

                    heading = "View previous posts",
                    subheading = "$postsCount retained posts",

                    icon = Icons.Rounded.History,
                    iconColor = blue,

                    onClick = {
                        navController.navigate(Routes.RECENTS)
                    }
                )

                ConfigSwitches(
                    modifier = defaultCardPadding,
                    configSwitchesViewModel = configSwitchesViewModel,
                    statusViewModel = statusViewModel
                )
            }
        }
    }
}