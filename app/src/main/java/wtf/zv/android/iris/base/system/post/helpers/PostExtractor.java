package wtf.zv.android.iris.base.system.post.helpers;

import android.graphics.Bitmap;
import android.net.Uri;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Optional;

import wtf.zv.android.iris.helpers.instagram.InstagramUtility;
import wtf.zv.iris.proto.InstagramPost;
import wtf.zv.iris.proto.Post;
import wtf.zv.iris.proto.RedditPost;
import wtf.zv.iris.proto.android.RetainedPost;

public class PostExtractor {
    public static RetainedPost transformPost(Post post){
        RetainedPost.Builder builder = RetainedPost.newBuilder();

        switch (post.getPostCase()){
            case REDDITPOST:
                RedditPost redditPost = post.getRedditPost();
                builder
                        .setImageUrl(redditPost.getImageUrl())
                        .setPostUrl(redditPost.getPostUrl())
                        .setTitle(String.format("r/%s - %s", redditPost.getSubreddit(), redditPost.getTitle()));
                break;
            case INSTAGRAMPOST:
                InstagramPost instagramPost = post.getInstagramPost();
                builder
                        .setImageUrl(instagramPost.getImageUrl())
                        .setPostUrl(getInstagramPostUrl(instagramPost).toString())
                        .setTitle(String.format("IG - %s", instagramPost.getAuthor()));
                break;
        }

        return builder.build();
    }

    public static Optional<Bitmap> extractBitmapFromPost(RetainedPost post){
        try {
            return Optional.of(Picasso.get().load(post.getImageUrl()).get());
        } catch (IOException ignored){
            return Optional.empty();
        }
    }

    private static String getInstagramPostUrl(InstagramPost instagramPost){
        switch (instagramPost.getPostIdOrUrlCase()){
            case POSTID:
                String instagramPostId = InstagramUtility.getInstagramPostId(instagramPost.getPostId());
                return String.format("https://instagram.com/p/%s", instagramPostId);
            case POSTURL:
                return instagramPost.getPostUrl();
        }

        throw new RuntimeException("The InstagramPost: " + instagramPost + " does not have a post ID or post URL set!");
    }
}
