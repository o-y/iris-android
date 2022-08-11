package wtf.zv.android.iris.base.system.post.handlers;

import static io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe.subscribe;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import de.mkammerer.snowflakeid.SnowflakeIdGenerator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wtf.zv.android.iris.data.retention.collection.write.WritableBlockCollectionStore;
import wtf.zv.android.iris.data.retention.collection.write.WritableBlockCollectionStoreFactory;
import wtf.zv.iris.proto.android.RetainedPost;
import wtf.zv.iris.proto.android.RetainedPosts;

/** Handles post retention in a {@link WritableBlockCollectionStore} */
public class PostRetentionService {

    private final WritableBlockCollectionStore<RetainedPosts, RetainedPost> postStore;

    @Inject
    PostRetentionService(
            WritableBlockCollectionStoreFactory<RetainedPosts, RetainedPost> postStore,
            SnowflakeIdGenerator snowflakeIdGenerator
    ){
        this.postStore = postStore.readWrite(
                (container, data) -> container.toBuilder().addPosts(data).build(),
                RetainedPosts::getPostsList);
    }

    public void retainPost(RetainedPost post){
        updateDataAsync(post.toBuilder().setTimestamp(System.currentTimeMillis()).build());
    }

    private void updateDataAsync(RetainedPost retainedPost){
        postStore.getContainer()
                .map(RetainedPosts::getPostsList)
                .map(postList -> postList.stream().noneMatch(post -> post.getPostUrl().equals(retainedPost.getPostUrl())))
                .subscribe(isUnique -> {
                    if (isUnique){
                        android.util.Log.i("slyo", "Retaining post: " + retainedPost);
                        postStore.updateDataAsync(retainedPost);
                    } else {
                        android.util.Log.i("slyo", "[NOT UNIQUE]: Not retaining post: " + retainedPost);
                    }
                });
    }
}
