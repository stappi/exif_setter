/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger;

import com.stappi.exifmerger.utilities.Utilities;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum für verschiedene Funktionen mit Parametern und Beschreibung
 *
 * @author Michael Stappert
 */
@AllArgsConstructor
@Getter
public enum CommandLineOperator {
    HELP(
            Stream.of("--help", "-h").collect(Collectors.toList()),
            "Shows all commands with examples. Detailed Information for a function with [-h -d].",
            ""),
    SET_DATE(
            Stream.of("--date", "-d").collect(Collectors.toList()),
            "Sets date of the image.",
            """
            Detailed Information:
            
            Option 1: Sets the capture date for all images. You can specify n images directly and/or through n directories. All image files within a directory (including subdirectories) will be selected. 
                        Command: -d "dd.MM.yyyy kk:mm:ss" "photo_1.jpg" ... "photo_n.jpg" ... "directory_1" ... "directory_N"
                        Example: -d "25.02.2025 15:20:35" "C:/photo_1.jpg" "C:/Bilder"

            Option 2: Sets the capture date based on the capture date of an image.
                        Command: -d "source_photo.jpg" "photo_1.jpg" ... "photo_n.jpg" ... "directory_1" ... "directory_N"-d "SimpleDateFormat" "photo_1.jpg" ... "photo_n.jpg" ... "directory_1" ... "directory_N"

            Option 3: Sets the capture date using the filename with the help of SimpleDateFormat. 
                        Command: -d "SimpleDateFormat" "photo_1.jpg" ... "photo_n.jpg" ... "directory_1" ... "directory_N"
                        Example: -d "yyyyMMdd-kkmmss_Bild" "C:/photo_1.jpg" "C:/Bilder"
            """),
    VERSION(
            Stream.of("--version", "-v").collect(Collectors.toList()),
            "Version number of exif merger.",
            "");

    private final List<String> tags;
    private final String description;
    private final String detailedInformation;

    @Override
    public String toString() {

//        System.out.println("tag..>" + Stream.of(CommandLineOperator.values()).map(operator -> operator.getTags().stream().collect(Collectors.joining(","))).mapToInt(tag -> tag.length()).max().orElse(0));
//        System.out.println("description..>" + Stream.of(CommandLineOperator.values()).map(operator -> operator.getDescription()).mapToInt(description -> description.length()).max().orElse(0));
//        System.out.println("command..>" + Stream.of(CommandLineOperator.values()).map(operator -> operator.getCommand()).mapToInt(command -> command.length()).max().orElse(0));
        return MessageFormat.format("{0}\t{1}",
                Utilities.formatWithPadding(tags.stream().collect(Collectors.joining(",")), 12),
                description);
    }

    public static CommandLineOperator valueOfLabel(String label) {
        return Stream.of(values())
                .filter(operator -> operator.getTags().stream().anyMatch(tag -> tag.equals(label)))
                .findFirst()
                .orElse(HELP);
    }
}
