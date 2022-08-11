package wtf.zv.android.iris.helpers.bitmap;

import android.graphics.Bitmap;

import androidx.palette.graphics.Palette;

public class BitmapUtility {
    public static Palette createPaletteSync(Bitmap bitmap) {
        return Palette.from(bitmap).generate();
    }
}
