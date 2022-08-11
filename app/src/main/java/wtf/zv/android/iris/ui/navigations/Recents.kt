package wtf.zv.android.iris.ui.navigations

import android.text.format.DateUtils
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import wtf.zv.android.iris.ui.components.recents.RecentsCard
import wtf.zv.android.iris.ui.models.RetainedPostsViewModelKt

@ExperimentalAnimationApi
@Composable
fun Recents(viewModelFactory: ViewModelProvider.Factory){
    val retainedPostsViewModel = viewModel<RetainedPostsViewModelKt>(factory = viewModelFactory);

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            items(items = retainedPostsViewModel.getPosts(), itemContent = { post ->
                RecentsCard(
                    heading = post.title,
                    date = DateUtils.getRelativeTimeSpanString(post.timestamp).toString(),
                    url = post.imageUrl,

                    imageUrl = post.imageUrl,
                    postUrl = post.postUrl,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                )
            })
        }
    }
}
