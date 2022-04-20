package com.example.exifreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.exifreader.Controller.ExifDataAdapter;
import com.example.exifreader.Model.ImageData;
import com.google.android.material.button.MaterialButton;

/**This activity manages the visualization of the exif data of the image that was displayed
 * in the ImageShowActivity and if the GPS coordinates are available it makes it possible to
 * view on the map where the photo was taken by launching the MapsActivity. This activity is
 * linked to the activity_exif.xml file.
 */
public class ExifActivity extends AppCompatActivity{

    private ImageButton exifCloseButton;
    private MaterialButton mapButton;
    private RecyclerView dataRecyclerView;
    private TextView imageNameTextView;
    private TextView exifDataCounter;

    private ExifDataAdapter exifDataAdapter;
    private ImageData image;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exif);
        mapButton = findViewById(R.id.google_map_button);
        exifCloseButton = findViewById(R.id.exif_close_button);
        imageNameTextView = findViewById(R.id.image_exif_name_textview);
        exifDataCounter = findViewById(R.id.exif_data_counter_textview);
        image = new ImageData(getIntent().getStringExtra("image"));
        initExifDataList(image);
        exifDataCounter.setText("Exif Data (" + image.getExifData().size() + " fields)");
        imageNameTextView.setText(image.getName());
        imageNameTextView.setSelected(true);
        if (image.loadImageMapData()) {
            mapButton.setEnabled(true);
        }

        /* Button listener that manages the throw of the MapsActivity */
        mapButton.setOnClickListener(view -> {
            Intent intent = new Intent(ExifActivity.this, MapsActivity.class).putExtra("image", image.getPathName());
            startActivity(intent);
        });

        /* Button listener that manages the closing of the current activity and returns to the ImageShowActivity */
        exifCloseButton.setOnClickListener(view -> {
            Intent replyIntent = new Intent();
            setResult(RESULT_OK, replyIntent);
            finish();
        });
    }

    /** Loads the exif data of the image displayed in ImageShowActivity with the function described
     *  in ImageData and using a Recyclerview managed with ExifDataAdapter and ExifDataHolder.*/
    private void initExifDataList(ImageData image){
        image.loadAllExifData();
        dataRecyclerView = findViewById(R.id.exif_data_recycler_view);
        dataRecyclerView.setHasFixedSize(true);
        dataRecyclerView.setLayoutManager(new GridLayoutManager( this,  1));
        exifDataAdapter = new ExifDataAdapter(this, image.getExifData());
        dataRecyclerView.setAdapter(exifDataAdapter);
    }
}