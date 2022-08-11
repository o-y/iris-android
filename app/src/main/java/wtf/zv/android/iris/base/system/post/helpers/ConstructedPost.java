package wtf.zv.android.iris.base.system.post.helpers;

import android.net.Uri;

import androidx.annotation.NonNull;

import java.util.Objects;

/** Deprecated - use {@link wtf.zv.iris.proto.android.RetainedPost} instead */
@Deprecated
public class ConstructedPost {
    @NonNull private final Uri postUrl;
    @NonNull private final Uri imageUrl;
    @NonNull private final String formattedTitle;

    ConstructedPost(@NonNull Uri postUrl, @NonNull Uri imageUrl, @NonNull String formattedTitle) {
        this.postUrl = postUrl;
        this.imageUrl = imageUrl;
        this.formattedTitle = formattedTitle;
    }

    @NonNull
    public Uri getPostUrl() {
        return postUrl;
    }

    @NonNull
    public Uri getImageUrl() {
        return imageUrl;
    }

    @NonNull
    public String getFormattedTitle() {
        return formattedTitle;
    }

    @NonNull
    @Override
    public String toString() {
        return "ConstructedPost{" +
                "postUrl=" + postUrl +
                ", imageUrl=" + imageUrl +
                ", formattedTitle='" + formattedTitle + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstructedPost)) return false;
        ConstructedPost that = (ConstructedPost) o;
        return getPostUrl().equals(that.getPostUrl()) && getImageUrl().equals(that.getImageUrl()) && getFormattedTitle().equals(that.getFormattedTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPostUrl(), getImageUrl(), getFormattedTitle());
    }
}
