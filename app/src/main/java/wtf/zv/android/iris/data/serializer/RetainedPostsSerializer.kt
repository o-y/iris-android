package wtf.zv.android.iris.data.serializer

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import wtf.zv.iris.proto.android.RetainedPosts
import java.io.InputStream
import java.io.OutputStream

class RetainedPostsSerializer : Serializer<RetainedPosts> {
    override val defaultValue: RetainedPosts get() = RetainedPosts.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): RetainedPosts {
        return try {
            RetainedPosts.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            exception.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: RetainedPosts, output: OutputStream) {
        t.writeTo(output)
    }
}