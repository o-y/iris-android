package wtf.zv.android.iris.data.retention.collection.write;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.datastore.rxjava2.RxDataStore;
import com.google.protobuf.GeneratedMessageLite;

import java.util.List;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.Single;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Mono;
import wtf.zv.android.iris.data.retention.collection.read.ReadableBlockCollectionStoreFactory;
import wtf.zv.android.iris.data.retention.collection.read.ReadableBlockCollectionStore;
import wtf.zv.android.iris.data.retention.internal.RetentionConsumer;
import wtf.zv.android.iris.data.retention.internal.RetentionSupplier;
import wtf.zv.android.iris.data.retention.internal.annotation.RepeatedProtobuf;

/** See {@link ReadableBlockCollectionStoreFactory} for documentation.
 *
 * <p>Instances of this class can <b>not</b> be injected, they should be provided using {@link
 * WritableBlockCollectionStoreFactory#readWrite}.
 * */
public class WritableBlockCollectionStore<
        @RepeatedProtobuf Container extends GeneratedMessageLite<Container, ? extends GeneratedMessageLite.Builder<Container, ?>>,
        Child extends GeneratedMessageLite<Child, ? extends GeneratedMessageLite.Builder<Child, ?>>>
        extends ReadableBlockCollectionStore<Container, Child> {

    private final RxDataStore<Container> store;
    private final RetentionConsumer<Container, Child> consumer;

    @AssistedInject
    public WritableBlockCollectionStore(
            RxDataStore<Container> store,
            @Assisted RetentionConsumer<Container, Child> consumer,
            @Assisted RetentionSupplier<Container, Child> supplier
    ) {
        super(store, supplier);

        this.store = store;
        this.consumer = consumer;
    }

    /**
     * Updates the data transactionally in an atomic read-modify-write operation. All operations
     * are serialized, and the transform itself is a async so it can perform heavy work
     * such as RPCs.
     *
     * This is a wrapper around the RxJava {@link RxDataStore} class, which converts a
     * {@link Single<Container>} to a {@link Mono<Container>} after adding the given {@link Child}
     * to it.
     */
    public Mono<Container> updateDataAsync(Child data){
        return RxJava2Adapter.singleToMono(store.updateDataAsync((currentStore) ->
                Single.just(consumer.accept(currentStore, data))));
    }
}
