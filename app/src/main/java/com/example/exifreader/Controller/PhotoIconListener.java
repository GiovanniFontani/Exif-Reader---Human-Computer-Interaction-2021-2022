package com.example.exifreader.Controller;
import com.example.exifreader.View.ImageHolder;

import java.io.File;

/** This interface is defined to be able to manage the listeners on the recyclerview items in the
 * activities. The activities that will want to use the adapters that can request this behavior,
 * will have to implement this interface and give their own reference to the adapter at the time of creation.
 */
public interface PhotoIconListener {
    void onPhotoClick(ImageHolder holder, File image);
    void onPhotoLongClick(ImageHolder holder, File image);
}
