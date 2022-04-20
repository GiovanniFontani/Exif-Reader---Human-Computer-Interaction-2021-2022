package com.example.exifreader.View;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.exifreader.R;

/**
 * This class belongs to the package view. Is the representation of the various exif data into ExifActivity.
 * Is linked to the exif_data_item.xml file in the layout folder in the resources. Extends RecyvlerView.ViewHolder
 * since each holder is connected to an ExifData object of the Model package by the ExifDataAdapter object of the Controller
 * package.
 */
public class ExifDataHolder extends RecyclerView.ViewHolder{

    private final TextView tagTypeView;
    private final TextView tagNameView;
    private final TextView descriptionView;

    public ExifDataHolder(View itemView) {
        super(itemView);
        this.tagTypeView = itemView.findViewById(R.id.tag_type_text_view);
        this.tagNameView = itemView.findViewById(R.id.tag_name_text_view);
        this.descriptionView = itemView.findViewById(R.id.tag_value_text_view);
    }

    public void bind(String tagType, String tagName, String description) {
        tagTypeView.setText(tagType);
        tagNameView.setText(tagName);
        descriptionView.setText(description);
        tagTypeView.setSelected(true);
        tagNameView.setSelected(true);
        descriptionView.setSelected(true);
    }


}
