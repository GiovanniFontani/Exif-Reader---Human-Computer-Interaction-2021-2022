package com.example.exifreader.View;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.exifreader.R;

/**This class belongs to the package view. is the representation of the various images of the gallery in the main acvity.
 * Is linked to the gallery_image_item.xml file in the layout folder in the resources. Extends RecyvlerView.ViewHolder
 * since each holder is connected to an ImageData object of the Model package by the ImageDataAdapter object of the Controller
 * package.
 */
public class ImageHolder extends RecyclerView.ViewHolder{

    private final ImageView imageView;

    public ImageHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = itemView.findViewById(R.id.gallery_image);
    }

    public ImageView getImageView(){
        return this.imageView;
    }

}
