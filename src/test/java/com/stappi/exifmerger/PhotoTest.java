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
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

import com.stappi.exifmerger.utilities.GpsCoordinate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Michael Stappert
 */
public class PhotoTest {

    private static final SimpleDateFormat DATE_TIME_FORMAT
            = new SimpleDateFormat("yyyyMMdd_kkmmss");

//    private static final List<String> TEST_FILES = new ArrayList<>(Arrays.asList(
//            "images_3/20120915-210644_Redding.jpg",
//            "images_3/20130407-165015_Berlin.jpg"
//    ));

//    @ValueSource(strings = {
//        "images_3/20120915-210644_Redding.jpg",
//        "images_3/20130407-165015_Berlin.jpg"
//    })
//    @BeforeAll
//    static void setUp() {
//        testString = "Hello, JUnit 5!";
//        System.out.println("Setup vor jedem Test");
//    }

    @ParameterizedTest
    @ValueSource(strings = {
            "images_3/20120915-210644_Redding.jpg",
            "images_3/20130407-165015_Berlin.jpg",
//            "images_3/20120915-210644_Redding.png",
//            "images_3/20200522-144535_Tierpark.ARW",
//            "images_3/20201025-171154_Spaziergang_Ebersberg.dng"
    })
    void getSetCaptureDate(String photoPath) {
        try {
            Date expectedDate = new Date(System.currentTimeMillis());
            File photoFile = copyFile(loadFile(photoPath));
            Photo photo = new Photo(photoFile);
            photo.setCaptureDate(expectedDate);
            assertThat(DATE_TIME_FORMAT.format(photo.getCaptureDate())).isEqualTo(DATE_TIME_FORMAT.format(expectedDate));
            removeFile(photoFile);
        } catch (URISyntaxException | IOException | ParseException ex) {
            Logger.getLogger(PhotoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    void getSetGps(String photoPath) {
        try {
            double longitude = -73.989308;
            double latitude = 40.741895;
            File photoFile = copyFile(loadFile(photoPath));
            Photo photo = new Photo(photoFile);
            photo.setGps(longitude, latitude);
            assertThat(photo.getGps()).isEqualTo(Optional.of(new GpsCoordinate(longitude, latitude)));
            removeFile(photoFile);
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(PhotoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static File loadFile(String photoPath) throws URISyntaxException {
        Path path = Paths.get(PhotoTest.class.getClassLoader().getResource(photoPath).toURI());
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
