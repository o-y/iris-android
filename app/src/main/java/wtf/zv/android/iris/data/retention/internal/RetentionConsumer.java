package wtf.zv.android.iris.data.retention.internal;

@FunctionalInterface
public interface RetentionConsumer<Container, Child> {

    /**
     * Returns a {@link Container} after adding the given {@link Child} to it. Both the {@link Child}
     * and {@link Container} should be protobuf declarations. And the {@link Container} should have
     * at least one repeated field delegating to {@link Child}.
     */
    Container accept(Container container, Child child);
}