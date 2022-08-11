package wtf.zv.android.iris.ui.models

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import wtf.zv.android.iris.data.retention.collection.read.ReadableBlockCollectionStoreFactory
import wtf.zv.iris.proto.android.RetainedPosts
import wtf.zv.iris.proto.android.RetainedPost
import androidx.lifecycle.ViewModel
import wtf.zv.android.iris.data.retention.collection.read.ReadableBlockCollectionStore
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import wtf.zv.android.iris.base.service.state.ServiceState
import wtf.zv.android.iris.ui.util.CachedMutableLiveData

@HiltViewModel
class RetainedPostsViewModelKt @Inject constructor(
    postStore: ReadableBlockCollectionStoreFactory<RetainedPosts, RetainedPost>
) : ViewModel() {

    private val retainedPostsCount = CachedMutableLiveData(0);
    private val blockCollectionStore: ReadableBlockCollectionStore<RetainedPosts, RetainedPost> = postStore.readOnly { it.postsList };
    private val retainedPost = MutableSharedFlow<RetainedPost>(replay = Int.MAX_VALUE, onBufferOverflow = BufferOverflow.DROP_OLDEST);

    fun getPosts(): List<RetainedPost> {
        return retainedPost.replayCache.reversed();
    }

    fun getPostsCount(): MutableLiveData<Int> {
        return retainedPostsCount.mutableLiveData
    }

    init {
        blockCollectionStore.collection.subscribe(retainedPost::tryEmit);

        blockCollectionStore.container
            .map { it.postsList }
            .map { it.count() }
            .subscribe {
                retainedPostsCount.postValue(it)
            }
    }
}