/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger.utilities;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.*;

import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;

public class MetadataWriter {

    private final File file;
    private final ImageMetadata metadata;

    public MetadataWriter(File file) throws IOException {
        this.file = file;
        this.metadata = Imaging.getMetadata(file);
    }

    public void setValue(MetadataTag tag, String value) throws IOException {

        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            TiffOutputSet outputSet = loadTiffOutputSet(jpegMetadata);
            updateValue(outputSet, tag, value);
            saveJpeg(file, outputSet);
        } else {
            throw new ImagingException(String.format("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: %s", file.getName()));
        }
    }

    public void setExifGPSTag(double longitude, double latitude) throws IOException {

        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            TiffOutputSet outputSet = loadTiffOutputSet(jpegMetadata);
            if (outputSet.getGpsDirectory() != null) {
                outputSet.getGpsDirectory().removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
                outputSet.getGpsDirectory().removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
            }
            outputSet.setGpsInDegrees(longitude, latitude);
            saveJpeg(file, outputSet);
        } else {
            throw new ImagingException(String.format("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: %s", file.getName()));
        }
    }

    /**
     * Erstellt oder aktualisiert das EXIF-Datum im TiffOutputSet.
     */
    private void updateValue(TiffOutputSet outputSet, MetadataTag tag, String value) throws ImagingException {

        tag.getOutputDirectory(outputSet).ifPresent(outputDirectory -> {
                    try {
                        outputDirectory.removeField(tag.getJpegTag());
                        addValue(outputDirectory, tag.getJpegTag(), value);
                    } catch (ImagingException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private void addValue(TiffOutputDirectory outputDirectory, TagInfo tagInfo, String value) throws ImagingException {
        if (tagInfo instanceof TagInfoAscii) {
            outputDirectory.add((TagInfoAscii) tagInfo, value);
        } else if (tagInfo instanceof TagInfoXpString) {
            outputDirectory.add((TagInfoXpString) tagInfo, value);
        } else {
            throw new ImagingException(String.format("Nicht unterstützter Tag-Typ: %s", tagInfo.getClass().getSimpleName()));
        }
    }

    /**
     * Speichert die aktualisierten EXIF-Daten in der Datei.
     */
    private void saveJpeg(File inputFile, TiffOutputSet outputSet) throws IOException {
        File tempFile = new File(inputFile.getParent(), inputFile.getName() + ".tmp");
        try (OutputStream os = new FileOutputStream(tempFile); FileInputStream fis = new FileInputStream(inputFile)) {
            new ExifRewriter().updateExifMetadataLossless(fis, os, outputSet);
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            throw new IOException(String.format("Fehler beim Ersetzen der Originaldatei: %s", inputFile.getAbsolutePath()));
        }
    }

    /**
     * Lädt ein TiffOutputSet aus bestehenden EXIF-Daten oder erstellt ein neues
     * Set.
     */
    private TiffOutputSet loadTiffOutputSet(JpegImageMetadata jpegMetadata) {
        try {
            return jpegMetadata.getExif().getOutputSet();
        } catch (ImagingException e) {
            return new TiffOutputSet();
        }
    }
}
