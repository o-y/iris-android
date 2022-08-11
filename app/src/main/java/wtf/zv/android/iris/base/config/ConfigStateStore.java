package wtf.zv.android.iris.base.config;

import java.util.Objects;
import javax.inject.Inject;
import javax.inject.Singleton;

import wtf.zv.android.iris.helpers.sink.ThreadSafeBuffer;
import wtf.zv.android.iris.base.config.internal.ThreadSafeConfigBuffer;
import wtf.zv.iris.proto.Config;

@Singleton
public final class ConfigStateStore {
    public static final long SYSTEM_INITIATE = System.nanoTime();
    private final ThreadSafeBuffer<Config> configBuffer;

    private CurrentConfig currentConfig = CurrentConfig.create(Config.newBuilder().build());

    @Inject
    public ConfigStateStore(
            @ThreadSafeConfigBuffer ThreadSafeBuffer<Config> configBuffer
    ){
        this.configBuffer = configBuffer;
    }

    public CurrentConfig getStore(){
        return currentConfig;
    }

    public void updateStore(Config config){
        currentConfig = CurrentConfig.create(config);

        configBuffer.getBuffer().emitNext(config, (signalType, emitResult) -> {
            android.util.Log.i("slyo", "[CONFIG:HANDLE]: Failed to emit: " + emitResult);
            return false;
        });
    }

    public static class CurrentConfig {
        private final long receivedAt;
        private final Config config;

        private CurrentConfig(Config config, long receivedAt) {
            this.receivedAt = receivedAt;
            this.config = config;
        }

        public static CurrentConfig create(Config config){
            return new CurrentConfig(config, System.nanoTime());
        }

        public long getReceivedAt(){
            return receivedAt;
        }

        public Config getConfig(){
            return config;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CurrentConfig)) return false;
            CurrentConfig that = (CurrentConfig) o;
            return getReceivedAt() == that.getReceivedAt() && getConfig().equals(that.getConfig());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getReceivedAt(), getConfig());
        }
    }
}
