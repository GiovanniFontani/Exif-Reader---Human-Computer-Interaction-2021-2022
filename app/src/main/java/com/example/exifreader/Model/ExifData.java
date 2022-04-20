package com.example.exifreader.Model;

/** This class is in the Model package as it contains attributes and logical functions
 * for the management of the exifData in the application. There are no views. It will be linked
 * through the Controller class "ExifDataAdapter" to the View class "ExifDataHolder" using an
 * Andorid RecyclerView into ExifActivity
 */
public class ExifData {
    private final String tagType;
    private final String tagName;
    private final String description;

    public ExifData ( String tagType, String fieldName, String description){
        this.tagType = tagType;
        this.tagName = fieldName;
        this.description = description;
    }

    public String getTagType() {
        return tagType;
    }

    public String getTagName() {
        return tagName;
    }

    public String getDescription() {
        return description;
    }

}
