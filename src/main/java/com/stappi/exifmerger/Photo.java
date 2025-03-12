/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.stappi.exifmerger.utilities.GpsCoordinate;
import com.stappi.exifmerger.utilities.MetadataExtractor;
import com.stappi.exifmerger.utilities.MetadataTag;
import com.stappi.exifmerger.utilities.MetadataWriter;
import org.apache.commons.imaging.ImagingException;

/**
 * https://github.com/cescoffier/Commons-Image-IO/blob/master/src/main/java/de/akquinet/commons/image/io/ImageMetadata.java
 *
 * @author Michael Stappert
 */
public class Photo {

    private static final FileFilter ACCEPTED_FILES = (File file)
            -> file.getName().toLowerCase().endsWith(".jpg")
            || file.getName().toLowerCase().endsWith(".jpeg")
            || file.getName().toLowerCase().endsWith(".png")
            || file.getName().toLowerCase().endsWith(".arw")
            || file.getName().toLowerCase().endsWith(".dng");

    private static final SimpleDateFormat DATE_TIME_FORMAT
            = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");

    private File file;
    private MetadataExtractor metadataExtractor;
    private MetadataWriter metadataWriter;

    public Photo(File file) throws IOException {
        this.file = file;
        this.metadataExtractor = new MetadataExtractor(file);
        this.metadataWriter = new MetadataWriter(file);
    }

    public Date getCaptureDate() throws IOException, ParseException {
        return DATE_TIME_FORMAT.parse(metadataExtractor.getMetadataValue(MetadataTag.CAPTURE_DATE));
    }

    public void setCaptureDate(Date captureDate) throws IOException {
        setValue(MetadataTag.CAPTURE_DATE, DATE_TIME_FORMAT.format(captureDate));
    }
    
    public String getAuthors() throws ImagingException {        
        return metadataExtractor.getMetadataValue(MetadataTag.AUTHORS);
    }

    public void setAuthors(String authors) throws IOException {
        setValue(MetadataTag.AUTHORS, authors);
    }

    public Optional<GpsCoordinate> getGps() throws IOException {
        return metadataExtractor.getGpsCoordinate();
    }

    public void setGps(double longitude, double latitude) throws IOException {
        this.metadataWriter.setExifGPSTag(longitude, latitude);
        metadataExtractor = new MetadataExtractor(file);
    }

    private void setValue(MetadataTag tag, String value) throws IOException {
        this.metadataWriter.setValue(tag, value);
        metadataExtractor = new MetadataExtractor(file);
    }

    public static List<Photo> loadPhotos(List<String> paths) {
        return paths.stream()
                .map(File::new)
                .flatMap(file -> file.isDirectory()
                ? Optional.ofNullable(file.listFiles()).map(Arrays::stream).orElse(Stream.empty())
                : Stream.of(file))
                .filter(file -> ACCEPTED_FILES.accept(file))
                .distinct()
                .flatMap(file -> {
                    try {
                        return Stream.of(new Photo(file));
                    } catch (IOException ex) {
                        Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, "Fehler beim Laden des Fotos: " + file.getAbsolutePath(), ex);
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }
}
