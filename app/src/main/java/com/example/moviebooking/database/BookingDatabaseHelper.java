package com.example.moviebooking.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// ========== SQLITE DATABASE: Helper class for database operations ==========
public class BookingDatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "CineBookDB";
    private static final int DATABASE_VERSION = 1;

    // Table Info
    private static final String TABLE_BOOKINGS = "bookings";

    // Column Names
    private static final String COL_ID = "id";
    private static final String COL_BOOKING_ID = "booking_id";
    private static final String COL_MOVIE = "movie";
    private static final String COL_THEATRE = "theatre";
    private static final String COL_SHOW_TIME = "show_time";
    private static final String COL_SEATS = "seats";
    private static final String COL_SEAT_COUNT = "seat_count";
    private static final String COL_TOTAL_PRICE = "total_price";
    private static final String COL_BOOKING_DATE = "booking_date";

    public BookingDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // ========== SQLITE: Create Table ==========
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_BOOKINGS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_BOOKING_ID + " TEXT, "
                + COL_MOVIE + " TEXT, "
                + COL_THEATRE + " TEXT, "
                + COL_SHOW_TIME + " TEXT, "
                + COL_SEATS + " TEXT, "
                + COL_SEAT_COUNT + " INTEGER, "
                + COL_TOTAL_PRICE + " INTEGER, "
                + COL_BOOKING_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    // ========== SQLITE: Upgrade Table ==========
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    // ========== SQLITE: INSERT - Add a new booking ==========
    public long insertBooking(String bookingId, String movie, String theatre,
                              String showTime, String seats, int seatCount, int totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOKING_ID, bookingId);
        values.put(COL_MOVIE, movie);
        values.put(COL_THEATRE, theatre);
        values.put(COL_SHOW_TIME, showTime);
        values.put(COL_SEATS, seats);
        values.put(COL_SEAT_COUNT, seatCount);
        values.put(COL_TOTAL_PRICE, totalPrice);
        values.put(COL_BOOKING_DATE, new java.text.SimpleDateFormat("dd MMM yyyy, hh:mm a",
                java.util.Locale.getDefault()).format(new java.util.Date()));

        long result = db.insert(TABLE_BOOKINGS, null, values);
        db.close();
        return result;
    }

    // ========== SQLITE: RETRIEVE - Get all bookings ==========
    public List<String[]> getAllBookings() {
        List<String[]> bookingList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKINGS + " ORDER BY " + COL_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String[] booking = new String[8];
                booking[0] = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_ID));
                booking[1] = cursor.getString(cursor.getColumnIndexOrThrow(COL_MOVIE));
                booking[2] = cursor.getString(cursor.getColumnIndexOrThrow(COL_THEATRE));
                booking[3] = cursor.getString(cursor.getColumnIndexOrThrow(COL_SHOW_TIME));
                booking[4] = cursor.getString(cursor.getColumnIndexOrThrow(COL_SEATS));
                booking[5] = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COL_SEAT_COUNT)));
                booking[6] = String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(COL_TOTAL_PRICE)));
                booking[7] = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOKING_DATE));
                bookingList.add(booking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookingList;
    }

    // ========== SQLITE: COUNT - Get total number of bookings ==========
    public int getBookingCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKINGS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    // ========== SQLITE: DELETE - Delete a booking by bookingId ==========
    public int deleteBooking(String bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKINGS, COL_BOOKING_ID + " = ?", new String[]{bookingId});
        db.close();
        return result;
    }

    // ========== SQLITE: DELETE ALL - Clear all bookings ==========
    public void deleteAllBookings() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKINGS, null, null);
        db.close();
    }
}
