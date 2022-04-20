package com.example.exifreader.Model.Util;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.example.exifreader.MainActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/** This class is defined to provide static methods to retrieve images with "simpleExtension"
 *  extensions in the smartphone memory by creating a list of file type objects.
 *  Each file type object requires the file path to be created.
 */
public class ImagesGallery {
    static String[] simpleExtensions = {".JPEG" , ".jpeg" , ".JPG" , ".jpg" , ".PNG", ".png", ".TIFF", ".tiff", ".BMP", ".bmp"};

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean checkIfFileHasExtension(String s, String[] extn) {
        return Arrays.stream(extn).anyMatch(s::endsWith);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<File> findImage(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        if (files != null){
            for(File singleFile: files){
                if(singleFile.isDirectory() && !singleFile.isHidden()){
                    arrayList.addAll(findImage(singleFile));
                }
                else{
                    if (checkIfFileHasExtension(singleFile.getName(),simpleExtensions)){
                        arrayList.add(singleFile);
                    }
                }
            }
        }
        return arrayList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<File> listOfImages(){
        return new ArrayList<>(findImage(new File(Environment.getExternalStorageDirectory().getAbsolutePath())));
    }
}
