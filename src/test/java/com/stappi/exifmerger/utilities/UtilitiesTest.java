/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger.utilities;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author Michael Stappert
 */
public class UtilitiesTest {

    private static Stream<Arguments> formatWithPaddingValues() {
        return Stream.of(
                Arguments.of("", 5, "     "),
                Arguments.of("a", 5, "a    "),
                Arguments.of("a", 1, "a")
        );
    }

//    private static Stream<Arguments> loadPhotosPaths() throws URISyntaxException {
//        return Stream.of(
//                Arguments.of(
//                        new ArrayList<>(Arrays.asList(
//                                loadFile("images_1/20161025-1855_Portland_Cape_Elizabeth.jpg").getAbsolutePath(),
//                                loadFile("images_1/20161029-1638_New_York_2.jpg").getAbsolutePath(),
//                                loadFile("images_1/20161101-2010_Philadelphia_Basketball.jpg").getAbsolutePath(),
//                                loadFile("images_1/20171101-143344_Rawa_Island.jpg").getAbsolutePath(),
//                                loadFile("images_1/20171117-144514_Singapur_Marina_Bay_Sands.jpg").getAbsolutePath(),
//                                loadFile("images_2").getAbsolutePath())),
//                        new ArrayList<>(Arrays.asList(
//                                loadFile("images_1/20161025-1855_Portland_Cape_Elizabeth.jpg"),
//                                loadFile("images_1/20161029-1638_New_York_2.jpg"),
//                                loadFile("images_1/20161101-2010_Philadelphia_Basketball.jpg"),
//                                loadFile("images_1/20171101-143344_Rawa_Island.jpg"),
//                                loadFile("images_1/20171117-144514_Singapur_Marina_Bay_Sands.jpg"),
//                                loadFile("images_2/20181109-141035_Marokko_Schildkroete.jpg"),
//                                loadFile("images_2/20190404-173856_Australien_Great_Ocean_Road.jpg"),
//                                loadFile("images_2/20190409-185026_Australien_Uluru.jpg"),
//                                loadFile("images_2/20190424-104806_Australien_Ningaloo_Reef.jpg"),
//                                loadFile("images_2/20210517-150158_Kroatien.jpg")))
//                ),
//                Arguments.of(
//                        new ArrayList<>(Arrays.asList(
//                                loadFile("images_1/20161025-1855_Portland_Cape_Elizabeth.jpg").getAbsolutePath(),
//                                loadFile("images_1/20161025-1855_Portland_Cape_Elizabeth.jpg").getAbsolutePath())),
//                        new ArrayList<>(Arrays.asList(
//                                loadFile("images_1/20161025-1855_Portland_Cape_Elizabeth.jpg")))
//                )
//        );
//    }

    private static final File loadFile(String photo) throws URISyntaxException {
        Path path = Paths.get(UtilitiesTest.class.getClassLoader().getResource(photo).toURI());
        return path.toFile();
    }

    @ParameterizedTest
    @MethodSource("formatWithPaddingValues")
    void formatWithPadding(String text, int noOfSpaces, String expectedValue) {
        assertThat(Utilities.formatWithPadding(text, noOfSpaces)).isEqualTo(expectedValue);
    }

//    @ParameterizedTest
//    @ValueSource(strings = {"123", " 123456"})
//    void formatWithPaddingException(String value) {
//        doThrow(new IllegalArgumentException("count is negative: -" + (value.length() - 4))).when(Utilities.formatWithPadding(value, 4));
//        assertThrows(IllegalArgumentException.class, () -> Utilities.formatWithPadding(value, 4));
//    }
    
//    @ParameterizedTest
//    @MethodSource("loadPhotosPaths")
//    void loadPhotos(List<String> paths, List<File> exptectedPhotos) {
//        assertThat(Utilities.loadPhotos(paths)).isEqualTo(exptectedPhotos);
//    }
}
