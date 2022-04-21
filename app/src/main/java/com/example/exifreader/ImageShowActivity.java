package com.example.exifreader;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.exifreader.Model.ImageData;
import com.example.exifreader.Model.RotateTransformation;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;


/** This activity manages the display of a single long-pressed image in the main activity or groups
 * of images selected with simple touches and the use of the Open Selected Gallery Image button.
 * In any case, the images will be scaled to have the largest dimension at 512 pixels and the other
 * scaled accordingly through the functions in the ImageData class. There is also the possibility
 * to rotate the images by 90' (assignment task) or in the case of multiple selection you can scroll
 * through the images (EXTRA assignment task). This activity is linked to the activity_image_show.xml
 * file.
 */
public class ImageShowActivity extends AppCompatActivity {


    private Button exifDataExploreButton;
    private ImageButton imageRight90RuotateButton;
    private ImageButton imageLeft90RuotateButton;
    private ImageButton imageShowCloseButton;
    private ImageView imageShowView;
    private MaterialButton previousImageButton;
    private MaterialButton nextImageButton;
    private TextView imageNameTextView;
    private TextView imageSizeTextView;
    private int currentPosition = 0;
    private ImageData currentImage;
    private ArrayList<String> multiImages;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        imageRight90RuotateButton = findViewById(R.id.image_90_right_ruotate_button);
        imageLeft90RuotateButton = findViewById(R.id.image_90_left_ruotate_button);
        imageShowCloseButton = findViewById(R.id.image_show_close_button);
        exifDataExploreButton = findViewById(R.id.exif_data_button);
        imageNameTextView = findViewById(R.id.image_show_name_textview);
        imageSizeTextView = findViewById(R.id.image_size_textview);
        imageShowView = findViewById(R.id.image_show_view);
        nextImageButton = findViewById(R.id.next_image_button);
        previousImageButton = findViewById(R.id.previous_image_button);

        /*The Recovery of the intent launched by MainActivity differentiates this activity based on the type of selection. */
        if(getIntent().getStringExtra("gallery").equals("true")){
            nextImageButton.setVisibility(View.VISIBLE);
            previousImageButton.setVisibility(View.VISIBLE);
            multiImages = (ArrayList<String>) getIntent().getSerializableExtra("multiImages");
            currentImage = new ImageData(multiImages.get(currentPosition));
            if (multiImages.size()>1)
                nextImageButton.setEnabled(true);
        }else{
            currentImage = new ImageData(getIntent().getStringExtra("image"));
        }

        showImage();

        /* Button listener that manages 90 degrees left rotation */
        imageLeft90RuotateButton.setOnClickListener(view -> {
            currentImage.setRotationDegree(currentImage.getRotationDegree() - 90f);
            RequestOptions request = currentImage.scaledDimensionRequest(512);
            request.transform(new RotateTransformation(currentImage.getRotationDegree(), request.getOverrideWidth(),request.getOverrideHeight()));
            Glide.with(ImageShowActivity.this)
                    .load(currentImage.getPathName())
                    .apply(request)
                    .placeholder(R.drawable.ic_icon_rotate_left_90)
                    .into(imageShowView);
        });

        /* Button listener that manages 90 degrees right rotation */
        imageRight90RuotateButton.setOnClickListener(view -> {
            currentImage.setRotationDegree(currentImage.getRotationDegree() + 90f);
            RequestOptions request = currentImage.scaledDimensionRequest(512);
            request.transform(new RotateTransformation(currentImage.getRotationDegree(),request.getOverrideWidth(),request.getOverrideHeight()));
            Glide.with(ImageShowActivity.this)
                    .load(currentImage.getPathName())
                    .apply(request)
                    .placeholder(R.drawable.ic_icon_rotate_right_90)
                    .into(imageShowView);
        });

        /* Button listener that manages the closing of the current activity and returns to the Main Activity */
        imageShowCloseButton.setOnClickListener(view -> {
            nextImageButton.setVisibility(View.INVISIBLE);
            previousImageButton.setVisibility(View.INVISIBLE);
            Intent replyIntent = new Intent();
            setResult(RESULT_OK, replyIntent);
            finish();
        });

        /* Button listener that manages the the throw of the ExifAcitivy */
        exifDataExploreButton.setOnClickListener(view -> {
            Intent intent = new Intent(ImageShowActivity.this, ExifActivity.class).putExtra("image", currentImage.getPathName());
            startActivity(intent);
        });

        /* Button listener that manages the scrolling to the next image of the selection */
        nextImageButton.setOnClickListener(view -> {
            currentPosition += 1;
            previousImageButton.setEnabled(true);
            currentImage = new ImageData(multiImages.get(currentPosition));
            showImage();
            if(currentPosition == multiImages.size()-1){
                nextImageButton.setEnabled(false);
            }
        });

        /* Button listener that manages the scrolling to the previous image of the selection */
        previousImageButton.setOnClickListener(view -> {
            currentPosition -= 1;
            nextImageButton.setEnabled(true);
            currentImage = new ImageData(multiImages.get(currentPosition));
            showImage();
            if(currentPosition == 0){
                previousImageButton.setEnabled(false);
            }
        });
    }

    /** Displays the current image by scaling to the limit size.
     * Resizing from original to scaled size is also indicated. */
    @SuppressLint("SetTextI18n")
    private void showImage(){
        imageNameTextView.setText(currentImage.getName());
        imageNameTextView.setSelected(true);

        HashMap<String,Integer> scaledDimensions = currentImage.scaleImageMaxSize(512);
        imageSizeTextView.setText("Image Size: " + currentImage.getWidth() +
                "x" + currentImage.getHeight() +"    Size Shown: " + scaledDimensions.get("scaledWidth")
                + "x" + scaledDimensions.get("scaledHeight"));
        RequestOptions request = currentImage.scaledDimensionRequest(512);
        Glide.with(this)
                .load(currentImage.getPathName())
                .apply(request)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageShowView);
    }
}