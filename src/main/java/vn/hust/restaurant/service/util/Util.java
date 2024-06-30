package vn.hust.restaurant.service.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public final class Util {

    public static final String DATE_PATTERN_1 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_PATTERN_2 = "yyyy/MM/dd";
    public static final String DATE_PATTERN_3 = "yyyy/MM/dd hh:mm a";
    public static final String DATE_PATTERN_4 = "hh:mm a, dd/MM/yyy";
    public static final String DATE_PATTERN_5 = "hh:mm a";
    public static final String DATE_PATTERN_6 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_7 = "dd/MM/yyyy";
    public static final String DATE_PATTERN_8 = "hh:mm a";
    public static final String DATE_PATTERN_9 = "yyyy/MM/dd HH:mm";
    public static final String DATE_PATTERN_10 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN_11 = "HH:mm dd/MM/yyyy";
    public static final String DATE_PATTERN_12 = "yyyyMM";
    private static final Logger log = LoggerFactory.getLogger(Util.class);

    private Util() {
    }

    /**
     * Read bytes from a File into a byte[].
     *
     * @return A byte[] containing the contents of the File.
     * @throws IOException Thrown if the File is too long to read or couldn't be
     *                     read fully.
     */
    public static byte[] readBytesFromFile(String path) throws IOException {
        File file = new File(path);

        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException(
                "Could not completely read file " +
                    file.getName() +
                    " as it is too long (" +
                    length +
                    " bytes, max supported " +
                    Integer.MAX_VALUE +
                    ")"
            );
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    /**
     * Writes the specified byte[] to the specified File path.
     * <p>
     * theFile File Object representing the path to write to.
     *
     * @param bytes The byte[] of data to write to the File.
     * @throws IOException Thrown if there is problem creating or writing the
     *                     File.
     */
    public static String writeBytesToFile(byte[] bytes, String path, String fileName, boolean isImage, int resizeWidth) throws IOException {
        fileName = fileName.replaceAll("[^a-zA-Z0-9.]", "");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_";
        File file = new File(path + date + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        BufferedOutputStream bos = null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            if (isImage) {
                // Trong trường hợp read file bị null, không resize nữa
                // trả về url luôn
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                BufferedImage image = ImageIO.read(byteArrayInputStream);
                // Lấy chiều dài tỉ lệ theo chiều rộng cần resize
                //                    int height = image.getHeight() * resizeWidth / image.getWidth();
                //                    BufferedImage resized = resize(image, height, resizeWidth);
                file = new File(path + date + fileName);
                ImageIO.write(image, "png", file);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            try {
                //flush and close the BufferedOutputStream
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return date + fileName;
    }

    public static String writeBytesToFileADV(byte[] bytes, String path, String fileName, boolean isImage, int resizeWidth, String type)
        throws IOException {
        fileName = fileName.replaceAll("[^a-zA-Z0-9.]", "");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_";
        File file = new File(path + date + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        BufferedOutputStream bos = null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            if (isImage) {
                // Trong trường hợp read file bị null, không resize nữa
                // trả về url luôn
                try {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    BufferedImage image = ImageIO.read(byteArrayInputStream);
                    // Lấy chiều dài tỉ lệ theo chiều rộng cần resize
                    //                    int height = image.getHeight() * resizeWidth / image.getWidth();
                    //                    BufferedImage resized = resize(image, height, resizeWidth);
                    file = new File(path + date + fileName);
                    ImageIO.write(image, type, file);
                } catch (Exception ignore) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            try {
                //flush and close the BufferedOutputStream
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return date + fileName;
    }

    public static File writeBytesToFileWithoutResize(byte[] bytes, String path, String fileName) throws IOException {
        fileName = fileName.replaceAll("[^a-zA-Z0-9.]", "");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_";
        File file = new File(path + date + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        BufferedOutputStream bos = null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            // Trong trường hợp read file bị null, không resize nữa
            // trả về url luôn
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                BufferedImage image = ImageIO.read(byteArrayInputStream);
                // Lấy chiều dài tỉ lệ theo chiều rộng cần resize
                file = new File(path + date + fileName);
                ImageIO.write(image, "png", file);
                return file;
            } catch (Exception ignore) {
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                //flush and close the BufferedOutputStream
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    /**
     * @param date
     * @return date string with format dd/MM/yyyy
     */
    public static String convertDateWithTime(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat.applyPattern("hh:mm a, dd/MM/yyy");
        return simpleDateFormat.format(date1);
    }

    /**
     * @param date
     * @param patter
     * @return date string with new format patter
     */
    public static String convertString(String date, String patter) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat.applyPattern(patter);
        return simpleDateFormat.format(date1);
    }

    /**
     * @param date
     * @return string date with format yyyy/MM/dd hh:mm a
     */
    public static String convertDateWithTimeEnd(String date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        simpleDateFormat.applyPattern("yyyy/MM/dd hh:mm a");
        return simpleDateFormat.format(date1);
    }

    /**
     * @param date
     * @return string date with format yyyy/MM/dd hh:mm
     */
    public static String convertDateWithTime24(String date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        simpleDateFormat.applyPattern("dd/MM/yyy HH:mm");
        return simpleDateFormat.format(date1);
    }

    /**
     * @param date
     * @return string date with format dd/MM/yyyy hh:mm a
     */
    public static String convertDateWithTimeEnd2(String date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.US);
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy'T'hh:mm:ss", Locale.US);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        simpleDateFormat.applyPattern("dd/MM/yyyy hh:mm a");
        return simpleDateFormat.format(date1);
    }

    /**
     * @param date
     * @param patter1
     * @param patter2
     * @return change patter of string date
     */
    public static String changeFormatTime(String date, String patter1, String patter2) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patter1, Locale.US);
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return date;
        }
        simpleDateFormat.applyPattern(patter2);
        return simpleDateFormat.format(date1);
    }

    /**
     * @param date
     * @return string date with format dd/MM/yyyy
     */
    public static String convertDate(String date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat.applyPattern("dd/MM/yyy");
        return simpleDateFormat.format(date1);
    }

    /**
     * @return string date today with format dd/MM/yyyy
     */
    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    /**
     * @param patter
     * @return string date today with patter
     */
    public static String getCurrentDate(String patter) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patter, Locale.getDefault());
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * @param date
     * @param patter
     * @return convert string to Date with format
     */
    public static Date stringToDate(String date, String patter) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patter, Locale.US);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param date
     * @param patter
     * @return convert date to string with format
     */
    public static String dateToString(Date date, String patter) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patter, Locale.US);
        return simpleDateFormat.format(date);
    }

    /**
     * @param time
     * @return format number with 2 letter
     */
    public static String formatTime(int time) {
        DecimalFormat mFormat = new DecimalFormat("00");
        return mFormat.format(Double.valueOf(time));
    }

    /**
     * @return firt day of month string with format dd/MM/yyyy
     */
    public static String getFirstDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime());
    }

    /**
     * @return first Date of month
     */
    public static Date getFirstDateOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * @return last day of month string with format dd/MM/yyyy
     */
    public static String getLastDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime());
    }

    /**
     * @return last Date of month
     */
    public static Date getLastDateOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * @return string date now
     */
    public static String getDateNow() {
        Calendar calendar = Calendar.getInstance();
        String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.forLanguageTag("vi"));
        return (
            day +
                "\n" +
                Util.formatTime(calendar.get(Calendar.DAY_OF_MONTH)) +
                " - " +
                Util.formatTime(calendar.get(Calendar.MONTH) + 1) +
                " - " +
                calendar.get(Calendar.YEAR)
        );
    }

    public static String toString(Object s) {
        return Objects.toString(s, "");
    }

    public static String toString(Integer i) {
        return Objects.toString(i, "");
    }

    public static String getDateTimeLichSu(Date date, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (type == 0) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else {
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
        }
        return new SimpleDateFormat(DATE_PATTERN_10, Locale.US).format(calendar.getTime());
    }

    public static String getDiffTime(double hour) {
        long diff = (long) (hour * 60 * 60000);
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        if (diffDays > 0) {
            return diffDays + " ngày trước";
        } else if (diffHours > 0) {
            return diffHours + " giờ trước";
        } else if (diffMinutes > 0) {
            return diffMinutes + " phút trước";
        } else {
            return diffSeconds + " giây trước";
        }
    }

    /**
     * Tính tuổi
     *
     * @param DateOfBirth
     * @return
     * @author congnd
     */
    public static int calculateAge(LocalDate DateOfBirth) {
        return Period.between(DateOfBirth, LocalDate.now()).getYears();
    }

    /**
     * Xóa tất cả các dấu phẩy và ký tự trắng thừa sau 1 chuỗi
     *
     * @param str
     * @return
     * @author thanhld
     */
    public static String removeAllLastComma(String str) {
        if (StringUtils.isEmpty(str)) return str;
        return str.replace("(\\s*,*\\s*)*$", "");
    }

    public static String timestampToString(java.sql.Timestamp ts, String pattern) {
        try {
            Date date = new Date(ts.getTime());
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String formatInstant(Instant datetime, String pattern) {
        try {
            return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(datetime);
        } catch (Exception e) {
            return null;
        }
    }

    public static String writeBytesToFileNews(byte[] bytes, String path, String fileName, boolean isImage, String type) throws IOException {
        fileName = fileName.replaceAll("[^a-zA-Z0-9.]", "");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_";
        File file = new File(path + date + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        BufferedOutputStream bos = null;

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            if (isImage) {
                // Trong trường hợp read file bị null, không resize nữa
                // trả về url luôn
                try {
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    BufferedImage image = ImageIO.read(byteArrayInputStream);
                    // Lấy chiều dài tỉ lệ theo chiều rộng cần resize
                    //                    int height = image.getHeight() * resizeWidth / image.getWidth();
                    //                    BufferedImage resized = resize(image, height, resizeWidth);
                    file = new File(path + date + fileName);
                    ImageIO.write(image, type, file);
                } catch (Exception ignore) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            try {
                //flush and close the BufferedOutputStream
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return date + fileName;
    }

    public static byte[] getImageData(String comId, String pathImage, String locationPath) {
        try {
            if (Strings.isNullOrEmpty(locationPath)) {
                locationPath = "D:/mnt/epproduct";
            }
            FileInputStream fileInputStream = new FileInputStream(locationPath + "\\" + comId + "\\" + pathImage);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer, 0, buffer.length)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("Error when get image data: " + e.getMessage());
            return null;
        }
    }
}
