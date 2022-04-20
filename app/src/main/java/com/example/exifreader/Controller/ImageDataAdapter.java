package com.example.exifreader.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.exifreader.R;
import com.example.exifreader.View.ImageHolder;
import java.io.File;
import java.util.List;

/** This Class is placed in the Controller package because it makes it possible to connect
 * the ImageData objects (Model) to ImageHolder (View). Each ImageData object is shown as an
 * ImageHolder inside the RecyclerView into MainActivity.
 */
public class ImageDataAdapter extends RecyclerView.Adapter<ImageHolder> {

    private final Context context;
    private final List<File> images;
    private final PhotoIconListener photoIconListener;

    public ImageDataAdapter(Context context, List<File> images, PhotoIconListener photoIconListener){
        this.context = context;
        this.images = images;
        this.photoIconListener = photoIconListener;
    }

    /** Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type (ImageHolder)
     * to represent an item.
     */
    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(context).inflate(R.layout.gallery_image_item,parent,false));
    }

    /** Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the RecyclerView.ViewHolder.itemView to reflect the ImageData at the
     * given position. The photoIconListener reference allow to manage an specific itemView Listener.
     * The photoIconListener reference allows us to manage a specific itemView listener
     * directly in the activities that implement the PhotoIconListener interface
     * @param holder ImageHolder where the ImageData information will go
     * @param position Position of the ImageData in the list
     */
    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        File image = images.get(position);
        Glide.with(context).load(image.getAbsolutePath()).into(holder.getImageView());
        holder.itemView.setOnClickListener(view -> photoIconListener.onPhotoClick(holder, image));
        holder.itemView.setOnLongClickListener(view -> {
            photoIconListener.onPhotoLongClick(holder, image);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

}
