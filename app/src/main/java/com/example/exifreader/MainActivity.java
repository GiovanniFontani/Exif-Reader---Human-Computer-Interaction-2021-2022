package com.example.exifreader;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.drew.imaging.ImageProcessingException;
import com.example.exifreader.Controller.ImageDataAdapter;
import com.example.exifreader.Model.Util.ImagesGallery;
import com.example.exifreader.Controller.PhotoIconListener;
import com.example.exifreader.View.ImageHolder;
import com.google.android.material.button.MaterialButton;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** This activity shows the images in the mobile memory (image extensions defined in the
 * ImageGallery class which provides functions for uploading). Images are shown using a Recyclerview
 * managed with ImageDataAdapter and ImageDataHolder. It is possible to inspect a single image with
 * a long click on it, or select multiple images and inspect them in a "gallery" by clicking on
 * the Open Selected Image Gallery button. This activity is linked to the activity_main.xml
 */
public class MainActivity extends AppCompatActivity implements PhotoIconListener {

    private ImageButton infoButton;
    private ImageButton closeButton;
    private MaterialButton selectedImagesGalleryButton;
    private RecyclerView recyclerView;
    private TextView galleryNumber;

    private ImageDataAdapter imageDataAdapter;
    private List<File> images;
    private List<File> multiImages;
    private static final int MY_READ_PERMISSION_CODE = 101;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoButton = findViewById(R.id.info_button);
        closeButton = findViewById(R.id.info_close_button);
        galleryNumber = findViewById(R.id.gallery_number);
        selectedImagesGalleryButton = findViewById(R.id.selected_images_gallery_button);
        multiImages = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview_gallery_images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager( this,  3));
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_PERMISSION_CODE);
        displayGallery();
    }

    /* Shows the gallery and sets the listeners */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayGallery() {
        images = ImagesGallery.listOfImages();
        imageDataAdapter = new ImageDataAdapter(this,images,this);
        recyclerView.setAdapter(imageDataAdapter);
        galleryNumber.setText("Exif Reader Gallery: Photos (" + images.size()+")");

        /* Button listener that manages the throw of the ImageShowActivity */
        selectedImagesGalleryButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ImageShowActivity.class).putExtra("gallery", "true");
            ArrayList<String> myList = new ArrayList<>();
            for(File img : multiImages)
                myList.add(img.getAbsolutePath());
            intent.putExtra("multiImages", myList);
            startActivity(intent);
        });

        /* Button listener that manages the information pop-up */
        infoButton.setOnClickListener(view->{
            findViewById(R.id.info_main_activity_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.gallery_linear_layout).setVisibility(View.INVISIBLE);
            infoButton.setVisibility(View.INVISIBLE);
            selectedImagesGalleryButton.setVisibility(View.INVISIBLE);
        });

        /* Button listener that close the information pop-up */
        closeButton.setOnClickListener(view -> {
            findViewById(R.id.info_main_activity_layout).setVisibility(View.INVISIBLE);
            findViewById(R.id.gallery_linear_layout).setVisibility(View.VISIBLE);
            infoButton.setVisibility(View.VISIBLE);
            selectedImagesGalleryButton.setVisibility(View.VISIBLE);
        });
    }

    /** Handles the simple click of an image item which is then selected/deselected and
     * added/removed. It also manages the list and the button to inspect them.
     * @param holder The item that was touched
     * @param image The image of the model linked to the item
     */
    @Override
    public void onPhotoClick(ImageHolder holder, File image) {
        if (multiImages.contains(image)) {
            holder.getImageView().setBackgroundResource(R.color.app_background);
            multiImages.remove(image);
            if(multiImages.size() == 0)
                selectedImagesGalleryButton.setEnabled(false);
        }else{
            if(multiImages.size() == 0)
                selectedImagesGalleryButton.setEnabled(true);
            multiImages.add(image);
            holder.getImageView().setBackgroundResource(R.drawable.image_gallery_selected);
        }
    }

    /** Handles the long click of an image element by immediately launching ImageShowActivity to inspect it.
     * @param holder The item that was touched
     * @param image The image of the model linked to the item
     */
    @Override
    public void onPhotoLongClick(ImageHolder holder, File image) {
        Intent intent = new Intent(MainActivity.this, ImageShowActivity.class).putExtra("image", image.getAbsolutePath());
        intent.putExtra("gallery", "false");
        startActivity(intent);
    }

    /* Manages the read permission of the mobile phone memory */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode == MY_READ_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
               displayGallery();
            }else{
                Toast permissionDennied = Toast.makeText(MainActivity.this, "To use the application you need to grant this permission. Close the app and reopen it", Toast.LENGTH_LONG);
                permissionDennied.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 200);
                permissionDennied.show();
            }
        }
    }
}
