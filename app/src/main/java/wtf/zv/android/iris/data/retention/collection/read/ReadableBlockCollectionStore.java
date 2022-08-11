package wtf.zv.android.iris.data.retention.collection.read;

import androidx.datastore.rxjava2.RxDataStore;

import com.google.protobuf.GeneratedMessageLite;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;
import io.reactivex.Flowable;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import wtf.zv.android.iris.data.retention.internal.RetentionSupplier;
import wtf.zv.android.iris.data.retention.internal.annotation.RepeatedProtobuf;

/** See {@link ReadableBlockCollectionStoreFactory#readOnly} for documentation.
 *
 * <p>Instances of this class can <b>not</b> be injected, they should be provided using {@link
 * ReadableBlockCollectionStoreFactory#readOnly}.
 * */
public class ReadableBlockCollectionStore<
        @RepeatedProtobuf Container extends GeneratedMessageLite<Container, ? extends GeneratedMessageLite.Builder<Container, ?>>,
        Child extends GeneratedMessageLite<Child, ? extends GeneratedMessageLite.Builder<Child, ?>>> {

    private final RxDataStore<Container> store;
    private final RetentionSupplier<Container, Child> supplier;

    @AssistedInject
    public ReadableBlockCollectionStore(
            RxDataStore<Container> store,
            @Assisted RetentionSupplier<Container, Child> supplier
    ) {
        this.store = store;
        this.supplier = supplier;
    }

  /**
   * Gets a {@link Flux<Child>} of the collection from DataStore.
   *
   * <p>Provides efficient, cached (when possible) access to the latest durably persisted state. The
   * flow will always either emit a value or throw an exception encountered when attempting to read
   * from disk. If an exception is encountered, collecting again will attempt to read the data
   * again.
   *
   * <p>NOTE: This data is reactive, therefore subscribing to the {@link Flowable<Child>} will *
   * result in upstream propagations of new data when children are added to get collectio.
   *
   * @return a {@link Flux<Child>} representing a stream of data from the repeated {@link Container}
   *     proto field.
   */
  public Flux<Child> getCollection() {
        return RxJava2Adapter
                .flowableToFlux(store.data())
                .flatMapIterable(supplier::accept);
    }

    /**
     * Gets a {@link Flowable<Child>} of the collection from DataStore in the {@link Flowable} format.
     *
     * <p>Provides efficient, cached (when possible) access to the latest durably persisted state.
     * The flow will always either emit a value or throw an exception encountered when attempting
     * to read from disk. If an exception is encountered, collecting again will attempt to read the
     * data again.
     *
     * <p>This is useful for any UI related work as {@link Flowable<Child>} is has first party support
     * in Compose.
     *
     * <p>NOTE: This data is reactive, therefore subscribing to the {@link Flowable<Child>} will
     * result in upstream propagations of new data when children are added to get collectio.
     *
     * @return a {@link Flowable<Child>} representing a stream of data from the repeated {@link Container} proto
     * field.
     */
    public Flowable<Child> getCollectionAsFlowable(){
        return store.data().flatMapIterable(supplier::accept);
    }

    /** Get a {@link Flux<Container>} representing the raw data from the DataStore. */
    public Flux<Container> getContainer(){
        return RxJava2Adapter.flowableToFlux(store.data());
    }
}