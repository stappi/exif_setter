/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger.utilities;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoAscii;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfoXpString;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.*;

import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;

public class MetadataWriter {

    private final File file;
    private ImageMetadata metadata;

    public MetadataWriter(File file) throws IOException {
        this.file = file;
        this.metadata = Imaging.getMetadata(file);
    }

//    /**
//     * Setzt das Aufnahmedatum für die Datei.
//     */
//    public void setCaptureDate(String value) throws ImagingException, IOException {
//
//        if (metadata instanceof JpegImageMetadata jpegMetadata) {
//            TiffOutputSet outputSet = loadTiffOutputSet(jpegMetadata);
//            updateCaptureDate(outputSet, value);
//            save(file, outputSet);
//        } else if (metadata instanceof TiffImageMetadata tiffMetadata) {
//            TiffOutputSet outputSet = tiffMetadata.getOutputSet();
//            updateCaptureDate(outputSet, value);
//            save(file, outputSet);
//        } else if (metadata instanceof GenericImageMetadata genericMetadata) {
//            // Versuch, die Metadaten zu aktualisieren, wenn möglich
//            if (genericMetadata.getItems().stream().anyMatch(item -> item.toString().contains("DateTimeOriginal"))) {
//                TiffOutputSet outputSet = new TiffOutputSet();
//                updateCaptureDate(outputSet, value);
//                save(file, outputSet);
//            } else {
//                throw new ImagingException("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: " + file.getName());
//            }
//        } else {
//            throw new ImagingException("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: " + file.getName());
//        }
//    }
//
//
//    /**
//     * Erstellt oder aktualisiert das EXIF-Datum im TiffOutputSet.
//     */
//    private void updateCaptureDate(TiffOutputSet outputSet, String captureDate) throws ImagingException {
//        TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
//        exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL); // Löscht altes Datum
//        exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, captureDate);
//    }

    public void setValue(MetadataTag tag, String value) throws ImagingException, IOException {

        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            TiffOutputSet outputSet = loadTiffOutputSet(jpegMetadata);
            updateValue(outputSet, tag, value);
            saveJpeg(file, outputSet);
        } else if (metadata instanceof TiffImageMetadata tiffMetadata) {
            TiffOutputSet outputSet = tiffMetadata.getOutputSet();
            updateValue(outputSet, tag, value);
            saveJpeg(file, outputSet);
        } else if (metadata instanceof GenericImageMetadata genericMetadata) {
            // Versuch, die Metadaten zu aktualisieren, wenn möglich
            if (genericMetadata.getItems().stream().anyMatch(item -> item.toString().contains(tag.getPngTag()))) {
                TiffOutputSet outputSet = new TiffOutputSet();
                updateValue(outputSet, tag, value);
                saveJpeg(file, outputSet);
            } else {
                throw new ImagingException("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: " + file.getName());
            }
        } else {
            throw new ImagingException("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: " + file.getName());
        }
    }

    public void setExifGPSTag(double longitude, double latitude) throws IOException, ImagingException, ImagingException {

        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            TiffOutputSet outputSet = loadTiffOutputSet(jpegMetadata);
            if (outputSet.getGpsDirectory() != null) {
                outputSet.getGpsDirectory().removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
                outputSet.getGpsDirectory().removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
            }
            outputSet.setGpsInDegrees(longitude, latitude);
            saveJpeg(file, outputSet);
        } else if (metadata instanceof TiffImageMetadata tiffMetadata) {
            TiffOutputSet outputSet = tiffMetadata.getOutputSet();
            if (outputSet.getGpsDirectory() != null) {
                outputSet.getGpsDirectory().removeField(GpsTagConstants.GPS_TAG_GPS_LONGITUDE);
                outputSet.getGpsDirectory().removeField(GpsTagConstants.GPS_TAG_GPS_LATITUDE);
            }
            outputSet.setGpsInDegrees(longitude, latitude);
            saveJpeg(file, outputSet);
        } else {
            throw new ImagingException("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: " + file.getName());
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
            throw new IllegalArgumentException("Nicht unterstützter Tag-Typ: " + tagInfo.getClass().getSimpleName());
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
            throw new IOException("Fehler beim Ersetzen der Originaldatei: " + inputFile.getAbsolutePath());
        }
    }

//    private void savePng(File inputFile, TiffOutputSet outputSet) throws Exception {
//        File inputFile = new File("input.png");
//        File outputFile = new File("output.png");
//
//        // Originalbild laden
//        BufferedImage image = ImageIO.read(inputFile);
//
//        // Metadaten als Key-Value-Paar setzen (tEXt-Chunk für PNG)
//        Map<String, String> metadata = new HashMap<>();
//        metadata.put("Creation Time", "2025-03-25 12:15:22"); // Alternative zu EXIF "DateTimeOriginal"
//
//        // PNG-Parameter setzen
//        PngImagingParameters params = new PngImagingParameters();
//        params.setTextChunks(Collections.singletonList(new PngText.Text(metadata)));
//
//        // Neues PNG-Bild mit Metadaten speichern
//        try (OutputStream os = new FileOutputStream(outputFile)) {
//            Imaging.writeImage(image, os, ImageFormats.PNG, params);
//        }
//    }
//
//    public static void main() throws Exception {
//        File inputFile = new File("input.dng");
//
//        try (ExifTool exifTool = new ExifTool()) {
//            exifTool.setImageMeta(inputFile, ExifTool.Tag.DATE_TIME_ORIGINAL, "2025:03:25 12:15:22");
//        }
//
//        System.out.println("DNG EXIF-Datum erfolgreich gesetzt!");
//    }

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
