# Exif Reader - Human Computer Interaction 2021-2022

Images are everywhere. With the proliferation of images and image formats (primarily JPEG) came the realization that there was a need for a common standard for exchanging metadata about digital images taken with digital cameras. That is precisely what the Exchangeable Image File Format (EXIF). ExifReader app reads and show images and related exif data from mobile phone memory. 

# Development 

The app project has been developed through Java - Android Studio, with the use of the Glide library to render the images, metadata-extractor library for reading metadata from media files, and play-services-maps library used to manage google maps. App works for Andorid Phones which have an API level &lt;= 29. 

# Instructions to test

To test the application you can use the [Bluestacks5](https://www.bluestacks.com/it/bluestacks-5.html?utm_source=Google&utm_medium=CPC&utm_campaign=aw-ded-it-bluestacks5-brand&gclid=Cj0KCQjwgYSTBhDKARIsAB8Kukv6lIO4Esdp5ZpGA3nBb2DJ2SCUnL7L5R9tGbmQgvDjeF3x3Fuma88aAuO9EALw_wcB) android emulator. Once the emulator is installed and started, open SystemApp-MediaManager and add test images by dragging images to the right pane. Test images are provided in the example_photos folder from the root. Now using Bluestack5 start ExifReader.apk file in the Runnable folder from the root. The application will be installed and it will be possible to start it. It usually runs in landscape mode but with the side controls it is also possible to switch to portrait mode.<br>
Alternatively you can try to install the .apk file by downloading it on an andorid smartphone with Lvl Api <= 29 (Andorid 10)

# How to use 

* It is possible to inspect a single image with a long touch, or generate a selection of images, selecting them with simple touches, to be displayed all together in a sort of gallery (Extra).<br>
* Once selected, the images are shown individually.If one size is greater than 512px, the image is scaled using 512px as a limit. It is also possible to rotate the image in 90 'steps to the right or left.<br>
* For each image you can view all the exif data in a list and if there is GPS data relating to where the photo was taken, you can view the position in a Google Map (Extra).<br>
* The application can be used both in landscape and portrait mode without loss of functionality.
