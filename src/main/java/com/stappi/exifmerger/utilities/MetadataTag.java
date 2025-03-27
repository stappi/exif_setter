/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger.utilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.MicrosoftTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.util.Optional;

/**
 *
 * @author Michael Stappert
 */
@AllArgsConstructor
@Getter
public enum MetadataTag {
    AUTHORS(MicrosoftTagConstants.EXIF_TAG_XPAUTHOR, "", ""),
    CAPTURE_DATE(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, "Creation Time", "DateTimeOriginal"),
    TITLE(MicrosoftTagConstants.EXIF_TAG_XPTITLE, "", "");

    private final TagInfo jpegTag;
    private final String pngTag;
    private final String tiffTag;

    public Optional<TiffOutputDirectory> getOutputDirectory(TiffOutputSet outputSet) throws ImagingException {
        return switch (this) {
            case AUTHORS -> Optional.of(outputSet.getOrCreateRootDirectory());
            case CAPTURE_DATE -> Optional.of(outputSet.getOrCreateExifDirectory());
            default -> Optional.empty();
        };
    }
}
