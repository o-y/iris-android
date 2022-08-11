package wtf.zv.android.iris.helpers.instagram;

public class InstagramUtility {
    public static String getInstagramPostId(String mediaId) {
        String postId = "";
        try {
            long id = Long.parseLong(mediaId.substring(0, mediaId.indexOf('_')));
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

            while (id > 0) {
                long remainder = (id % 64);
                id = (id - remainder) / 64;
                postId = alphabet.charAt((int)remainder) + postId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postId;
    }
}
