package com.example.moviebooking.storage;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

// ========== INTERNAL STORAGE: Helper class for file read/write operations ==========
public class InternalStorageHelper {

    private static final String RECEIPT_FILE = "booking_receipts.txt";

    // ========== INTERNAL STORAGE: Write booking receipt to file ==========
    public static void saveReceipt(Context context, String bookingId, String movie,
                                    String theatre, String showTime, String seats,
                                    int seatCount, int totalPrice) {
        String receipt = "========================================\n"
                + "       🎬 CINEBOOK BOOKING RECEIPT\n"
                + "========================================\n"
                + "Booking ID : " + bookingId + "\n"
                + "Movie      : " + movie + "\n"
                + "Theatre    : " + theatre + "\n"
                + "Show Time  : " + showTime + "\n"
                + "Seats      : " + seats + "\n"
                + "Tickets    : " + seatCount + "\n"
                + "Total Paid : ₹" + totalPrice + "\n"
                + "Date       : " + new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a",
                java.util.Locale.getDefault()).format(new java.util.Date()) + "\n"
                + "========================================\n\n";

        try {
            // MODE_APPEND to add new receipts without overwriting old ones
            FileOutputStream fos = context.openFileOutput(RECEIPT_FILE, Context.MODE_APPEND);
            fos.write(receipt.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========== INTERNAL STORAGE: Read all receipts from file ==========
    public static String readAllReceipts(Context context) {
        StringBuilder content = new StringBuilder();
        try {
            FileInputStream fis = context.openFileInput(RECEIPT_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            fis.close();
        } catch (Exception e) {
            content.append("No receipts found.");
        }
        return content.toString();
    }

    // ========== INTERNAL STORAGE: Count stored receipts ==========
    public static int countReceipts(Context context) {
        String allReceipts = readAllReceipts(context);
        if (allReceipts.equals("No receipts found.")) return 0;
        // Count occurrences of "Booking ID" to get receipt count
        int count = 0;
        int index = 0;
        while ((index = allReceipts.indexOf("Booking ID", index)) != -1) {
            count++;
            index++;
        }
        return count;
    }

    // ========== INTERNAL STORAGE: Clear all receipts ==========
    public static void clearReceipts(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(RECEIPT_FILE, Context.MODE_PRIVATE);
            fos.write("".getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
