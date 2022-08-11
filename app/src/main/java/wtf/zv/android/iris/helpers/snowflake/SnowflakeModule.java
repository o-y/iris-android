package wtf.zv.android.iris.helpers.snowflake;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import de.mkammerer.snowflakeid.SnowflakeIdGenerator;

@Module
@InstallIn(SingletonComponent.class)
public class SnowflakeModule {
    @Provides
    @Singleton
    public SnowflakeIdGenerator provideSnowflakeGenerator(){
        return SnowflakeIdGenerator.createDefault(0);
    }
}
