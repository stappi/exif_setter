package com.stappi.exifmerger;

import static com.stappi.exifmerger.CommandLineOperator.SET_DATE;
import static com.stappi.exifmerger.CommandLineOperator.VERSION;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.apache.commons.imaging.ImagingException;

/**
 *
 * @author Michael Stappert
 */
public class Main {

    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd_kkmmss");

    public static void main(String[] args) throws IOException, ParseException {

        try {
//            Photo photo = new Photo(new File("C:\\Users\\Michael Stappert\\Documents\\NetBeansProjects\\ExifMerger\\src\\test\\resources\\images_2\\20181109-141035_Marokko_Schildkroete.jpg"));
//            Photo photo = new Photo(new File("C:\\Users\\Michael Stappert\\Documents\\NetBeansProjects\\ExifMerger\\src\\test\\resources\\images_3\\20200522-144535_Tierpark.ARW"));
//            Photo photo = new Photo(new File("C:\\Users\\Michael Stappert\\Documents\\NetBeansProjects\\ExifMerger\\target\\test-classes\\images_3\\test_20120915-210644_Redding.jpg"));
            Photo photo = new Photo(new File("C:\\Users\\micha\\IdeaProjects\\exif_setter\\src\\test\\resources\\images_1\\20130407-203051_Berlin.jpg"));
//            Photo photo = new Photo(new File("C:\\Users\\Michael Stappert\\Documents\\NetBeansProjects\\ExifMerger\\src\\test\\resources\\images_3\\20120915-210644_Redding.png"));
            System.out.println("--> " + photo.getCaptureDate());
            photo.setCaptureDate(DATE_TIME_FORMAT.parse("20130407_165015"));
        } catch (ImagingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

//        switch (parseCommandLineOperator(args.length == 0 ? "" : args[0])) {
//            case SET_DATE:
//                updateDate(args);
//                break;
//            case VERSION:
//                version();
//                break;
//            case HELP:
//            default:
//                help(args);
//        }
    }

    private static void updateDate(String[] args) {

        System.out.println("update exif capture date");
        List<Photo> images = Photo.loadPhotos(IntStream
                .range(2, args.length)
                .mapToObj(index -> args[index])
                .collect(Collectors.toList()));

        String dateParameter = args[1];
//        if (dateParameter.matches("^\\d{4}:\\d{2}:\\d{2} \\d{2}:\\d{2}:\\d{2}$")) {
//            images.stream().forEach(photo -> {
//                try {
//                    ExifMergeUtilities.updateExifDate(photo, dateParameter);
//                } catch (IOException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//        } else if (new File(dateParameter).isFile()) {
//            String date = "";//TODO Datum aus File lesen
//            images.stream().forEach(photo -> {
//                try {
//                    ExifMergeUtilities.updateExifDate(photo, date);
//                } catch (IOException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//        } else {
//            images.stream().forEach(photo -> {
//                try {
//                    String date = "";//TODO Datum aus Dateiname mit SDF lesen
//                    ExifMergeUtilities.updateExifDate(photo, date);
//                } catch (IOException ex) {
//                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
//        }
    }

    private static void help(String[] args) {
        switch (parseCommandLineOperator(args.length > 1 ? args[1] : "")) {
            case SET_DATE:
                System.out.println(SET_DATE.getDetailedInformation());
                break;
            case VERSION:
                System.out.println(VERSION.getDetailedInformation());
                break;
            case HELP:
            default:
                System.out.println("Folgende Operatoren stehen zur Verfuegung:");
                Stream.of(CommandLineOperator.values()).forEach(operator -> System.out.println(operator.toString()));
        }
    }

    private static void version() {
        System.out.println("Version 1.0");
    }

    private static CommandLineOperator parseCommandLineOperator(String operator) {
        return CommandLineOperator.valueOfLabel(operator);
    }
}
