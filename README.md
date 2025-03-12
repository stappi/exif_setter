# ExifMerger
Command line tool, to set exif data for photos.

### EXIF-Tags aus `ExifTagConstants.java`

| **Tag**                  | **Konstante**                         | **Beschreibung** |
|--------------------------|--------------------------------------|------------------|
| **Aufnahmedatum**        | `EXIF_TAG_DATE_TIME_ORIGINAL`       | Datum und Uhrzeit der Aufnahme. Format: `YYYY:MM:DD HH:MM:SS` |
| **Digitalisierungsdatum** | `EXIF_TAG_DATE_TIME_DIGITIZED`      | Datum und Uhrzeit, wann das Bild digitalisiert wurde. |
| **Bildbeschreibung**      | `EXIF_TAG_IMAGE_DESCRIPTION`        | Bildtitel oder Beschreibung des Bildes. |
| **Künstler (Autor)**      | `EXIF_TAG_ARTIST`                   | Name des Künstlers oder Fotografen. |
| **Benutzerkommentar**     | `EXIF_TAG_USER_COMMENT`             | Benutzerkommentar zum Bild. |
| **Copyright**            | `EXIF_TAG_COPYRIGHT`                | Copyright-Informationen zum Bild. |
| **Kamerahersteller**     | `EXIF_TAG_MAKE`                     | Hersteller der Kamera (z. B. Canon, Nikon, Sony). |
| **Kameramodell**        | `EXIF_TAG_MODEL`                    | Modell der Kamera (z. B. Canon EOS 5D). |
| **Software**             | `EXIF_TAG_SOFTWARE`                 | Name der Software, die das Bild bearbeitet hat. |
| **Orientierung**         | `EXIF_TAG_ORIENTATION`              | Bildausrichtung (z. B. Normal, 180° gedreht). |
| **Belichtungszeit**      | `EXIF_TAG_EXPOSURE_TIME`            | Verschlusszeit der Kamera (z. B. `1/250` Sekunde). |
| **Blendenwert**         | `EXIF_TAG_FNUMBER`                  | Blendenwert (z. B. `f/2.8`). |
| **ISO-Wert**            | `EXIF_TAG_ISO`                      | ISO-Wert der Kameraeinstellungen. |
| **Belichtungsmodus**    | `EXIF_TAG_EXPOSURE_MODE`            | Modus der Belichtung (z. B. Automatisch, Manuell). |
| **Blitz**               | `EXIF_TAG_FLASH`                     | Gibt an, ob der Blitz verwendet wurde (Ja/Nein). |
| **Brennweite**          | `EXIF_TAG_FOCAL_LENGTH`              | Brennweite des Objektivs (z. B. `50mm`). |
| **GPS-Breite (Latitude)** | `EXIF_TAG_GPS_LATITUDE`            | Breitengrad der Aufnahme (z. B. `52° 31' 12" N`). |
| **GPS-Breite Ref**      | `EXIF_TAG_GPS_LATITUDE_REF`         | Gibt an, ob der Breitengrad Nord (`N`) oder Süd (`S`) ist. |
| **GPS-Länge (Longitude)** | `EXIF_TAG_GPS_LONGITUDE`          | Längengrad der Aufnahme (z. B. `13° 24' 0" E`). |
| **GPS-Länge Ref**       | `EXIF_TAG_GPS_LONGITUDE_REF`        | Gibt an, ob der Längengrad Ost (`E`) oder West (`W`) ist. |
| **GPS-Höhe**            | `EXIF_TAG_GPS_ALTITUDE`             | Höhe über Meeresspiegel (z. B. `34m`). |
| **Weißabgleich**        | `EXIF_TAG_WHITE_BALANCE`            | Gibt an, ob der Weißabgleich automatisch oder manuell eingestellt wurde. |
| **Belichtungsprogramm** | `EXIF_TAG_EXPOSURE_PROGRAM`         | Belichtungsprogramm (z. B. `Portrait`, `Landschaft`). |
| **Farbraum**            | `EXIF_TAG_COLOR_SPACE`              | Farbraum des Bildes (z. B. sRGB, AdobeRGB). |
| **Komprimierung**       | `EXIF_TAG_COMPRESSION`              | Gibt an, ob das Bild komprimiert wurde. |
| **Pixel X-Dimension**   | `EXIF_TAG_PIXEL_X_DIMENSION`        | Breite des Bildes in Pixeln. |
| **Pixel Y-Dimension**   | `EXIF_TAG_PIXEL_Y_DIMENSION`        | Höhe des Bildes in Pixeln. |
