package com.example.exifreader.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.exifreader.Model.ExifData;
import com.example.exifreader.R;
import com.example.exifreader.View.ExifDataHolder;
import java.util.List;


/**This Class is placed in the Controller package because it makes it possible to connect
 * the ExifData (Model) to ExifDataHolder (View). Each ExifData object is shown as an
 * ExifHolder inside the RecyclerView into ExifActivity.
 */
public class ExifDataAdapter extends RecyclerView.Adapter<ExifDataHolder> {

    private final Context context;
    private final List<ExifData> exifDataList;

    public ExifDataAdapter(Context context, List<ExifData> exifDataList){
        this.context = context;
        this.exifDataList = exifDataList;
    }

    /** Called when RecyclerView into ExifActivity needs a new RecyclerView.ViewHolder of the
     * given type (ExifData) to represent an item.
     */
    @NonNull
    @Override
    public ExifDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ExifDataHolder(LayoutInflater.from(context).inflate(R.layout.exif_data_item, parent,false));
    }

    /** Called by RecyclerView into ExifActivity to display the data at the specified position.
     * This method should update the contents of the RecyclerView.ViewHolder.itemView to reflect
     * the ExifData at the given position.
    */
    @Override
    public void onBindViewHolder(@NonNull ExifDataHolder holder, int position) {
        holder.bind(exifDataList.get(position).getTagType(),exifDataList.get(position).getTagName(),exifDataList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return exifDataList.size();
    }
}
