/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.stappi.exifmerger.utilities.GpsCoordinate;
import org.apache.commons.imaging.ImagingException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Michael Stappert
 */
public class PhotoTest {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd_kkmmss");

    private static Stream<Arguments> expectedCaptureDate() {
        return Stream.of(
                Arguments.of("images_3/20120915-210644_Redding.jpg", "20120915_210644"),
                Arguments.of("images_3/20120915-210644_Redding.png", "20120915_210644"),
                Arguments.of("images_3/20130407-165015_Berlin.jpg", "20130407_165015"),
                Arguments.of("images_3/20200522-144535_Tierpark.ARW", "20200522_144535"),
                Arguments.of("images_3/20201025-171154_Spaziergang_Ebersberg.dng", "20201025_171154")
        );
    }

    private static Stream<Arguments> expectedAuthors() {
        return Stream.of(
                Arguments.of("images_3/20120915-210644_Redding.jpg", "Peter Wutzengruber"),
                Arguments.of("images_3/20130407-165015_Berlin.jpg", "Peter Wutzengruber")//,
//                Arguments.of("images_3/20200522-144535_Tierpark.ARW", ""),
//                Arguments.of("images_3/20201025-171154_Spaziergang_Ebersberg.dng", "")
        );
    }

    private static Stream<Arguments> expectedTitle() {
        return Stream.of(
                Arguments.of("images_3/20120915-210644_Redding.jpg", "Redding in Kalifornien"),
                Arguments.of("images_3/20130407-165015_Berlin.jpg", "Berliner Fernsehturm"),
                Arguments.of("images_3/20200522-144535_Tierpark.ARW", "Wildpark Frankenhof"),
                Arguments.of("images_3/20201025-171154_Spaziergang_Ebersberg.dng", "Wald in Ebersberg")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "images_3/20120915-210644_Redding.jpg",
            "images_3/20130407-165015_Berlin.jpg"
    })
    void getSetCaptureDate(String photoPath) throws URISyntaxException, IOException, ParseException {
        Date expectedDate = new Date(System.currentTimeMillis());
        Photo photo = createPhoto(photoPath, true);
        photo.setCaptureDate(expectedDate);
        assertThat(DATE_TIME_FORMAT.format(photo.getCaptureDate())).isEqualTo(DATE_TIME_FORMAT.format(expectedDate));
        removeFile(photo.getFile());
    }

    @ParameterizedTest
    @MethodSource("expectedCaptureDate")
    void getCaptureDate(String photoPath, String expectedDate) throws URISyntaxException, IOException, ParseException {
        Photo photo = createPhoto(photoPath, false);
        assertThat(DATE_TIME_FORMAT.format(photo.getCaptureDate())).isEqualTo(expectedDate);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "images_3/20120915-210644_Redding.png",
            "images_3/20200522-144535_Tierpark.ARW",
            "images_3/20201025-171154_Spaziergang_Ebersberg.dng"
    })
    void setCaptureDateException(String photoPath) throws URISyntaxException, IOException {
        Photo photo = createPhoto(photoPath, false);
        ImagingException exception = assertThrows(ImagingException.class, () -> photo.setCaptureDate(new Date(System.currentTimeMillis())));
        assertThat(exception.getMessage()).isEqualTo(String.format("EXIF-Daten können für dieses Dateiformat nicht gesetzt werden: %s", photo.getFile().getName()));
    }

    @ParameterizedTest
    @MethodSource("expectedAuthors")
    void getAuthors(String photoPath, String expectedAuthors) throws URISyntaxException, IOException {
        Photo photo = createPhoto(photoPath, false);
        assertThat(photo.getAuthors()).isEqualTo(expectedAuthors);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "images_3/20120915-210644_Redding.jpg",
            "images_3/20130407-165015_Berlin.jpg"
    })
    void getSetAuthors(String photoPath) {
        try {
            String test = "Max Mustermann";
            File photoFile = copyFile(loadFile(photoPath));
            Photo photo = new Photo(photoFile);
            photo.setAuthors(test);
            assertThat(photo.getAuthors()).isEqualTo(test);
            removeFile(photoFile);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(PhotoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "images_3/20120915-210644_Redding.jpg",
            "images_3/20130407-165015_Berlin.jpg"
    })
    void getSetGps(String photoPath) throws IOException, URISyntaxException {
        double longitude = -73.989308;
        double latitude = 40.741895;
        Photo photo = createPhoto(photoPath, true);
        photo.setGps(longitude, latitude);
        assertThat(photo.getGps()).isEqualTo(Optional.of(new GpsCoordinate(longitude, latitude)));
        removeFile(photo.getFile());
    }

    @ParameterizedTest
    @MethodSource("expectedTitle")
    void getTile(String photoPath, String expectedTitle) throws URISyntaxException, IOException {
        Photo photo = createPhoto(photoPath, false);
        assertThat(photo.getTitle()).isEqualTo(expectedTitle);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "images_3/20120915-210644_Redding.jpg",
            "images_3/20130407-165015_Berlin.jpg"
    })
    void getSetTitle(String photoPath) {
        try {
            String test = "Test Title";
            File photoFile = copyFile(loadFile(photoPath));
            Photo photo = new Photo(photoFile);
            photo.setTitle(test);
            assertThat(photo.getTitle()).isEqualTo(test);
            removeFile(photoFile);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(PhotoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Photo createPhoto(String photoPath, boolean copyFile) throws URISyntaxException, IOException {
        File photoFile = copyFile ? copyFile(loadFile(photoPath)) : loadFile(photoPath);
        assertThat(photoFile).isNotNull();
        Photo photo = new Photo(photoFile);
        assertThat(photo).isNotNull();
        return photo;
    }

    private static File loadFile(String photoPath) throws URISyntaxException {
        Path path = Paths.get(Objects.requireNonNull(PhotoTest.class.getClassLoader().getResource(photoPath)).toURI());
        return path.toFile();
    }

    private static File copyFile(File sourceFile) throws IOException {
        File destinationFile = new File(sourceFile.getParent(), "test_" + sourceFile.getName());
        Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return destinationFile;
    }

    private static void removeFile(File file) throws IOException {
        Files.delete(file.toPath());
    }
}
