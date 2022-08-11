package wtf.zv.android.iris

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import wtf.zv.android.iris.ui.navigations.Dashboard
import wtf.zv.android.iris.ui.navigations.Recents
import wtf.zv.android.iris.ui.util.Routes
import wtf.zv.android.iris.ui.theme.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IrisTheme(
                darkTheme = false
            ) {
                MainView()
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun MainView() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {
        composable(Routes.DASHBOARD) {
            Dashboard(
                viewModelFactory = HiltViewModelFactory(LocalContext.current, it),
                navController = navController
            )
        }

        composable(Routes.RECENTS) {
            Recents(viewModelFactory = HiltViewModelFactory(LocalContext.current, it))
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview
@Composable
fun DefaultPreview() {
    IrisTheme {
        MainView()
    }
}

//@ExperimentalAnimationApi
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    IrisTheme {
//        MainView()
//    }
//}