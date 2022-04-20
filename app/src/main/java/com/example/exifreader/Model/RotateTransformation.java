package com.example.exifreader.Model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import java.security.MessageDigest;

/** This class extends the Bitmap Transformation to perform image rotation,
 * as required by one of the self assignment tasks, through the Glide library.
 */
public class RotateTransformation extends BitmapTransformation {

    private final int height;
    private final int width;
    private final float rotationAngle;

    public RotateTransformation(float rotateRotationAngle, int height, int width) {
        this.rotationAngle = rotateRotationAngle;
        this.height = height;
        this.width = width;
    }

    /** It takes the given bitmap image and rotates by "rotationAngle" and then resizes the rotated image.
     */
    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle);
        Bitmap rotate_image = Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
        return Bitmap.createScaledBitmap(rotate_image,height,width,true);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(("rotate" + rotationAngle).getBytes());
    }
}