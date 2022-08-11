package wtf.zv.android.iris.base.system.post.helpers;

import android.net.Uri;

/** Deprecated - use {@link wtf.zv.iris.proto.android.RetainedPost} instead */
@Deprecated
public final class ConstructedPostBuilder {
    private Uri postUrl;
    private Uri imageUrl;
    private String formattedTitle;

    public static ConstructedPostBuilder instance(){
        return new ConstructedPostBuilder();
    }

    public ConstructedPostBuilder setPostUrl(Uri postUrl) {
        this.postUrl = postUrl;
        return this;
    }

    public ConstructedPostBuilder setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ConstructedPostBuilder setFormattedTitle(String formattedTitle) {
        this.formattedTitle = formattedTitle;
        return this;
    }

    public ConstructedPost build() {
        return new ConstructedPost(postUrl, imageUrl, formattedTitle);
    }

    private ConstructedPostBuilder() {}
}
