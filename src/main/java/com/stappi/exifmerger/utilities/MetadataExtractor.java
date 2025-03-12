/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger.utilities;

import org.apache.commons.imaging.*;
import org.apache.commons.imaging.common.GenericImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.imaging.common.ImageMetadata;

public class MetadataExtractor {

    private final ImageMetadata metadata;

    public MetadataExtractor(File file) throws IOException {
        this.metadata = Imaging.getMetadata(file);
    }

    public String getMetadataValue(MetadataTag tag) throws ImagingException {

        if (metadata instanceof TiffImageMetadata tiffImageMetadata) {
            return extractFromTiff(tiffImageMetadata, tag.getTiffTag());
        } else if (metadata instanceof JpegImageMetadata jpegImageMetadata) {
            return extractFromJpeg(jpegImageMetadata, tag.getJpegTag());
        } else if (metadata instanceof GenericImageMetadata genericImageMetadata) {
            return extractFromGeneric(genericImageMetadata, tag.getPngTag());
        }
        return "";
    }

    public Optional<GpsCoordinate> getGpsCoordinate() throws ImagingException {
        if (metadata instanceof TiffImageMetadata tiffImageMetadata) {
            return Optional.of(new GpsCoordinate(
                    tiffImageMetadata.getGpsInfo().getLongitudeAsDegreesEast(),
                    tiffImageMetadata.getGpsInfo().getLatitudeAsDegreesNorth()
            ));
        } else if (metadata instanceof JpegImageMetadata jpegImageMetadata) {
            return Optional.of(new GpsCoordinate(
                    jpegImageMetadata.getExif().getGpsInfo().getLongitudeAsDegreesEast(),
                    jpegImageMetadata.getExif().getGpsInfo().getLatitudeAsDegreesNorth()
            ));
        }
        return Optional.empty();
    }

    private String extractFromTiff(TiffImageMetadata tiffMetadata, String tagName) {
        Map<String, String> metadataMap = tiffMetadata.getAllFields().stream()
                .collect(Collectors.toMap(
                        field -> field.getTagName(),
                        field -> field.getValueDescription(),
                        (existing, newValue) -> existing));

        return metadataMap.getOrDefault(tagName, "");
    }

    private String extractFromJpeg(JpegImageMetadata jpegMetadata, TagInfo tag) {
        if (jpegMetadata == null) return "";

        try {
            Object fieldValue = jpegMetadata.getExif().getFieldValue(tag);
            return fieldValue != null ? fieldValue.toString() : "";
        } catch (ImagingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String extractFromGeneric(GenericImageMetadata genericMetadata, String tagName) {
        return genericMetadata.getItems().stream()
                .map(Object::toString)
                .filter(item -> item.startsWith(tagName + ": "))
                .findFirst()
                .map(item -> item.replace(tagName + ": ", ""))
                .orElse("");
    }
}

