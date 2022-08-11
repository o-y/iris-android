package wtf.zv.android.iris.data.retention.collection.write;

import androidx.datastore.core.DataStore;
import androidx.datastore.rxjava2.RxDataStoreBuilder;

import com.google.protobuf.GeneratedMessageLite;

import dagger.assisted.AssistedFactory;
import wtf.zv.android.iris.data.retention.collection.read.ReadableBlockCollectionStore;
import wtf.zv.android.iris.data.retention.collection.write.WritableBlockCollectionStore;
import wtf.zv.android.iris.data.retention.internal.RetentionConsumer;
import wtf.zv.android.iris.data.retention.internal.RetentionSupplier;
import wtf.zv.android.iris.data.retention.internal.annotation.RepeatedProtobuf;

@AssistedFactory
public interface WritableBlockCollectionStoreFactory<
        @RepeatedProtobuf Container extends GeneratedMessageLite<Container, ? extends GeneratedMessageLite.Builder<Container, ?>>,
        Child extends GeneratedMessageLite<Child, ? extends GeneratedMessageLite.Builder<Child, ?>>> {
    /**
     * Provides a Dagger compliant instance of {@link WritableBlockCollectionStore} which allows you to
     * <b>only read</b> from a given {@link DataStore<Container>} through a number of helper methods.
     *
     * @param consumer - this consumer should add the data of type {@link Child} to the
     *                 {@link com.google.protobuf.GeneratedMessageLite.Builder} of type {@link Child}.
     *                 eg {@code (container, data) -> container.toBuilder().addField(data).build()}.
     *
     * @param supplier - this supplier should return the repeated field of type {@link Child}, this
     *                 should canonically be done using a method reference, for instance: {@code
     *                 store.create(K::repeatedFieldReference)}. If there are multiple fields, by
     *                 which access should be granted, multiple {@link javax.inject.Singleton}
     *                 modules should be developed which return a {@link RxDataStoreBuilder<Container>}.
     */
    WritableBlockCollectionStore<Container, Child> readWrite(RetentionConsumer<Container, Child> consumer, RetentionSupplier<Container, Child> supplier);
}

