package wtf.zv.android.iris.data.retention.collection.read;

import androidx.datastore.core.DataStore;
import androidx.datastore.rxjava2.RxDataStoreBuilder;

import com.google.protobuf.GeneratedMessageLite;

import dagger.assisted.AssistedFactory;
import wtf.zv.android.iris.data.retention.collection.read.ReadableBlockCollectionStore;
import wtf.zv.android.iris.data.retention.collection.write.WritableBlockCollectionStore;
import wtf.zv.android.iris.data.retention.internal.RetentionSupplier;
import wtf.zv.android.iris.data.retention.internal.annotation.RepeatedProtobuf;

/** Generic wrapper which provides access to a {@link DataStore<T>}.
 *
 * <p>{@link Container} - should be a plural generated Protobuf instance which extends {@code GeneratedMessageLite.Builder},
 * this proto definition should contain <b>at least one</b> repeated field which matches the type of
 * {@link Child}.
 *
 * <p>{@link Child} - should be a singular generated Protobuf instance which extends {@code GeneratedMessageLite.Builder},
 * this proto definition is what will be persisted in the link {@link DataStore}, and should therefore
 * consist of a unique ID by which it can be identified.
 */
@AssistedFactory
public interface ReadableBlockCollectionStoreFactory<
        @RepeatedProtobuf Container extends GeneratedMessageLite<Container, ? extends GeneratedMessageLite.Builder<Container, ?>>,
        Child extends GeneratedMessageLite<Child, ? extends GeneratedMessageLite.Builder<Child, ?>>> {

    /**
     * Provides a Dagger compliant instance of {@link WritableBlockCollectionStore} which allows you to <b>
     * read and write</b> a given {@link DataStore<Container>} through a number of helper methods.
     *
     * @param supplier - this supplier should return the repeated field of type {@link Container}, this
     *                 should canonically be done using a method reference, for instance: {@code
     *                 store.create(K::repeatedFieldReference)}. If there are multiple fields, by
     *                 which access should be granted, multiple {@link javax.inject.Singleton}
     *                 modules should be developed which return a {@link RxDataStoreBuilder<Container>}.
     */
    ReadableBlockCollectionStore<Container, Child> readOnly(RetentionSupplier<Container, Child> supplier);
}
