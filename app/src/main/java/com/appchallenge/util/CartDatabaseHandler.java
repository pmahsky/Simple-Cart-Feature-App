package com.appchallenge.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appchallenge.model.Event;

import java.util.ArrayList;

/**
 * Created by Prashant on 05/04/17.
 */

public class CartDatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cart_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_EVENT = "event";

    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "name";
    private static final String KEY_END_DATE = "phone_number";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_EVENT_COUNT = "count";


    private static final int COLUMN_ID = 0;
    private static final int COLUMN_TITLE = 1;
    private static final int COLUMN_END_DATE = 2;
    private static final int COLUMN_THUMBNAIL_URL = 3;
    private static final int COLUMN_EVENT_COUNT = 4;
    private static final int COLUMN_EVENT_ID = 5;


    public CartDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_EVENT_TABLE = "CREATE TABLE " + TABLE_EVENT + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_END_DATE + " TEXT," + KEY_IMAGE_URL + " TEXT," + KEY_EVENT_COUNT + " TEXT," + KEY_EVENT_ID + " TEXT" + ")";

        sqLiteDatabase.execSQL(CREATE_EVENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);

        onCreate(sqLiteDatabase);

    }

    public void addEvent(Event event) {

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery("SELECT *" + " FROM " + TABLE_EVENT + " WHERE " + KEY_EVENT_ID + " = '" + event.getEventId() + "'", null);
        cursor.moveToFirst();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_EVENT_ID, event.getEventId());
        contentValues.put(KEY_TITLE, event.getTitle());
        contentValues.put(KEY_END_DATE, event.getEndDate());
        contentValues.put(KEY_IMAGE_URL, event.getThumbnailUrl());

        if (cursor.getCount() > 0) {
            int count = cursor.getInt(COLUMN_EVENT_COUNT);
            int newCount = count + 1;
            contentValues.put(KEY_EVENT_COUNT, newCount);

            database.update(TABLE_EVENT, contentValues, KEY_EVENT_ID + " = ?", new String[]{String.valueOf(event.getEventId())});

        } else {

            contentValues.put(KEY_EVENT_COUNT, event.getEventCount());
            database.insert(TABLE_EVENT, null, contentValues);
        }

        database.close();

    }

    public void addEventInCart(Event event) {

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT *" + " FROM " + TABLE_EVENT + " WHERE " + KEY_ID + " = '" + event.getRowId() + "'", null);
        cursor.moveToFirst();

        int count = cursor.getInt(COLUMN_EVENT_COUNT);
        int newCount = count + 1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, event.getTitle());
        contentValues.put(KEY_END_DATE, event.getEndDate());
        contentValues.put(KEY_IMAGE_URL, event.getThumbnailUrl());
        contentValues.put(KEY_EVENT_COUNT, newCount);

        database.update(TABLE_EVENT, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(event.getRowId())});
        cursor.close();
        database.close();

    }

    public void removeEventFromCart(Event event) {

        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT *" + " FROM " + TABLE_EVENT + " WHERE " + KEY_ID + " = '" + event.getRowId() + "'", null);
        cursor.moveToFirst();

        int count = cursor.getInt(COLUMN_EVENT_COUNT);
        int newCount = count - 1;

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE, event.getTitle());
        contentValues.put(KEY_END_DATE, event.getEndDate());
        contentValues.put(KEY_IMAGE_URL, event.getThumbnailUrl());
        contentValues.put(KEY_EVENT_COUNT, newCount);

        database.update(TABLE_EVENT, contentValues, KEY_ID + " = ?", new String[]{String.valueOf(event.getRowId())});
        cursor.close();
        database.close();

    }

    public ArrayList<Event> getEventList() {

        ArrayList<Event> eventArrayList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_EVENT;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            do {
                Event event = new Event();
                event.setRowId(Integer.parseInt(cursor.getString(COLUMN_ID)));
                event.setTitle(cursor.getString(COLUMN_TITLE));
                event.setEndDate(cursor.getString(COLUMN_END_DATE));
                event.setThumbnailUrl(cursor.getString(COLUMN_THUMBNAIL_URL));
                event.setEventCount(cursor.getInt(COLUMN_EVENT_COUNT));

                eventArrayList.add(event);

            } while (cursor.moveToNext());

        }

        cursor.close();
        database.close();

        return eventArrayList;

    }


    public void removeEvent(int id) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_EVENT, KEY_ID + " = ?", new String[]{String.valueOf(id)});
        sqLiteDatabase.close();

    }
}
