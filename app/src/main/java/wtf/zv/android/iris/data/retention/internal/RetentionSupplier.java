package wtf.zv.android.iris.data.retention.internal;

import java.util.List;

@FunctionalInterface
public interface RetentionSupplier<Container, Child> {

    /**
     * Returns a {@link List<Child>} extracted from the {@link Container} which should be a protobuf
     * file with at least one repeated field delegating to {@link Child}
     */
    List<Child> accept(Container container);
}