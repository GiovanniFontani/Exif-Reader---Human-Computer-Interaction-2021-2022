package com.example.exifreader.Model;

import androidx.annotation.NonNull;

import com.bumptech.glide.request.RequestOptions;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.GpsDirectory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/** This class is in the Model package as it contains attributes and logical functions
 * for the management of the image in the application. There are no views. It will be linked through
 * the controller class "ImageDataAdapter" to the view class "ImageHolder" using an
 * Andorid RecyclerView into MainActivity.
 * The ImageShowActivity use objects of this class for the single image show.
 */
public class ImageData extends File {

    private float rotationDegree;
    private final int width;
    private final int height;
    private final ArrayList<ExifData> exifData = new ArrayList<>();
    private GeoLocation geoLocation;


    /** Constructor method that takes care of retrieving the key attributes in order to ensure
     * uniformity regardless of the model (there are mobile phone models that handle width,
     * height and rotation differently).
     * @param filepath Path to the file on the system
     */
    public ImageData(@NonNull String filepath)
    {
        super(filepath);
        HashMap<String, Integer> info = extractImgRappFromExif();
        int exifRotation= info.get("Orientation");
        int exifWidth = info.get("Width");
        int exifHeight = info.get("Height");

        /* Uniforms to distinguish portrait and landscape by width
         and height regardless of orientation. This is to better manage the representation of images.
         */
        if( (exifRotation == 90 || exifRotation == 270) && exifWidth > exifHeight ){
            this.width =  exifHeight;
            this.height = exifWidth;
        }else
        {
            this.width =  exifWidth;
            this.height = exifHeight;
        }
        this.rotationDegree = 0;
        this.geoLocation = null;
    }

    /** Performs the scaling. One of the self assignment tasks requires making sure that the maximum
     *  representation size is 512px, and the other is scaled with respect to the initial aspect ratio.
     * Here it is made possible to pass a maximum size to the function (it will be called with 512)
     * @param max Maximum dimensions size
     * @return A HashMap with scaled values of width and height.
     */
    public HashMap<String,Integer> scaleImageMaxSize(int max) {
        HashMap<String, Integer> dimensions = new HashMap<>();
        float scaleWidth = width;
        float scaleHeight = height;
        if (height > max || width > max) {
            if (height > width) {
                scaleWidth = (float) (max * width / height);
                scaleHeight = max;
            } else if (height < width) {
                scaleHeight = (float) (max * height / width);
                scaleWidth = max;
            } else {
                scaleWidth = max;
                scaleHeight = max;
            }
        }
        dimensions.put("scaledHeight", Math.round(scaleHeight));
        dimensions.put("scaledWidth", Math.round(scaleWidth));
        return dimensions;
    }

    /** Creates a RequestOption to be provided to the Glide library for image representation,
     * specifying the right representation size required by the app.
     * @param dimensionsLimit Maximum dimensions size
     * @return    RequestOption Object with right resize values depending on whether portrait or landscape.
     */
    public RequestOptions scaledDimensionRequest(int dimensionsLimit) {
        HashMap<String,Integer> scaledDimensions =  scaleImageMaxSize(dimensionsLimit);
        RequestOptions request;
        if (rotationDegree % 180 == 0)
            request = new RequestOptions().override(scaledDimensions.get("scaledWidth"),
                    scaledDimensions.get("scaledHeight"));
        else
            request = new RequestOptions().override(scaledDimensions.get("scaledHeight"),
                    scaledDimensions.get("scaledWidth"));
        return request;
    }

    /** Uses the Java ImageMetadataReader class, recommended in the document relating
     *  to self assignment, at the link https://github.com/drewnoakes/metadata-extractor/,
     * for the extraction of ALL the exif data and the creation of the <ExifData> list .
     *
     */
    public void loadAllExifData(){
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(this);
        } catch (ImageProcessingException | IOException e) {
            e.printStackTrace();
        }
        assert metadata != null;
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                exifData.add(new ExifData(tag.getDirectoryName(), tag.getTagName(), tag.getDescription()));
            }
        }
        Collections.reverse(exifData);
    }

    /** Uses the Java ImageMetadataReader class to extract the exif data related to width,
     *  height and rotation. This function is created to avoid extracting all the exif data as soon as
     *  the ImageData object is created. These parameters are sufficient for the representation.
     *  The others are loaded only if the display of the exif data is requested, in a lazy approach.
     * @return HashMap with Width, Height and Rotation read from exif
     */
    public  HashMap<String, Integer> extractImgRappFromExif(){
        HashMap<String, Integer> firstImgRapp = new HashMap<>();
        String heightStr = "0";
        String widthStr = "0";
        String orientation =  "0";
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(this);
        } catch (ImageProcessingException | IOException e) {
            e.printStackTrace();
        }
        assert metadata != null;
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                if (tag.getDirectoryName().equals("JPEG")) {
                    if (tag.getTagName().contains("Height") || tag.getTagName().contains("height")) {
                        heightStr = tag.getDescription().replaceAll("[^0-9]", "");
                    }
                    if (tag.getTagName().contains("Width") || tag.getTagName().contains("width")) {
                        widthStr = tag.getDescription().replaceAll("[^0-9]", "");
                    }
                }
                if (tag.getTagName().contains("Orientation") || tag.getTagName().contains("orientation")) {
                    orientation = tag.getDescription().replaceAll("[^0-9]", "");
                }
            }
        }
        firstImgRapp.put("Height", (int) Math.floor(Integer.parseInt(heightStr)));
        firstImgRapp.put("Width", (int) Math.floor(Integer.parseInt(widthStr)));
        firstImgRapp.put("Orientation", (int) Math.floor(Integer.parseInt(orientation)));
        return firstImgRapp;
    }

    /** Uses the Java ImageMetadataReader class to extract the exif data related to gps position
     * of where the image was taken. In the exif data there are more information about the gps,
     * but for the representation on the map they are sufficient. For this reason ImageData has a dedicated attribute.
     * @return  Boolean value that specifies if there are coordinates in the exif data.
     */
    public boolean loadImageMapData() {
        Metadata metadata = null;
        try {
            metadata = ImageMetadataReader.readMetadata(this);
        } catch (ImageProcessingException | IOException e) {
            e.printStackTrace();
        }
        assert metadata != null;
        Collection<GpsDirectory> gpsDirectories = metadata.getDirectoriesOfType(GpsDirectory.class);
        Iterator<GpsDirectory> iterator;
        if (gpsDirectories.size() != 0){
            iterator = gpsDirectories.iterator();
            while (iterator.hasNext()) {
                GpsDirectory gpsDir = iterator.next();
                GeoLocation geoLoc = gpsDir.getGeoLocation();
                if (geoLoc != null) {
                    double latitude = geoLoc.getLatitude();
                    double longitude = geoLoc.getLongitude();
                    if (latitude != 0 && longitude != 0) {
                        geoLocation = new GeoLocation(latitude, longitude);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public String getPathName(){
        return this.getAbsolutePath();
    }

    public float getRotationDegree() {
        return rotationDegree;
    }

    public void setRotationDegree(float rotationDegree) {
        this.rotationDegree = rotationDegree % 360;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ArrayList<ExifData> getExifData() {
        return exifData;
    }

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }
}