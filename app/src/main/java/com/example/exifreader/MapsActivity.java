package com.example.exifreader;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.exifreader.Model.ImageData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.exifreader.databinding.ActivityMapsBinding;


/**EXTRA CREDIT TASK:
   * This activity allows to view the point at which the image was
   * taken if the coordinates of the photo are among the Exif Data.
   * A Google Maps is used to represent the coordinates of the specific Image Data.
        */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ImageData image;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = new ImageData(getIntent().getStringExtra("image"));
        image.loadImageMapData();
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * A marker for where the image was taken is added.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng photoPosition = new LatLng(image.getGeoLocation().getLatitude(), image.getGeoLocation().getLongitude());
        mMap.addMarker(new MarkerOptions().position(photoPosition).title(image.getPathName()));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(photoPosition)
                .zoom(13)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}