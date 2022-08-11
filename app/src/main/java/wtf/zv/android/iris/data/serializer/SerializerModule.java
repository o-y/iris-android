package wtf.zv.android.iris.data.serializer;

import android.content.Context;

import androidx.datastore.core.DataStoreFactory;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.datastore.rxjava2.RxDataStoreBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import wtf.zv.iris.proto.ReactorIrisClientServiceGrpc;
import wtf.zv.iris.proto.android.RetainedPosts;

@Module
@InstallIn(SingletonComponent.class)
public class SerializerModule {
    @Provides
    @Singleton
    RxDataStore<RetainedPosts> provideChannelStub(Context context) {
        return new RxDataStoreBuilder<RetainedPosts>(
                context,
                "retained_posts_store.pb",
                new RetainedPostsSerializer()).build();
    }
}
