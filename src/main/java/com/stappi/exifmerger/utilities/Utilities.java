/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stappi.exifmerger.utilities;

/**
 *
 * @author Michael Stappert
 */
public final class Utilities {
    

    public static String formatWithPadding(String text, int noOfSpaces) {
        return text + " ".repeat(noOfSpaces - text.length());
    }
}
