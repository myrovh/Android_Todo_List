package myrovh.to_dolistreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class ReminderDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminderDatabase";
    private static final int DATABASE_VERSION = 2;

    private static final String TABLE_REMINDERS = "reminders";
    private static final String KEY_REMINDERS_ID = "_id";
    private static final String KEY_REMINDERS_TITLE = "title";
    private static final String KEY_REMINDERS_DESCRIPTION = "description";
    private static final String KEY_REMINDERS_DUEDATE = "dueDate";
    private static final String KEY_REMINDERS_COMPLETE = "complete";
    private static final String KEY_REMINDERS_LATITUDE = "latitude";
    private static final String KEY_REMINDERS_LONGITUDE = "longitude";

    private static final String QUERY_ALLREMINDERS = String.format("SELECT * FROM %s", TABLE_REMINDERS);

    ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private ContentValues createContentValues(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(KEY_REMINDERS_TITLE, reminder.getTitle());
        values.put(KEY_REMINDERS_DESCRIPTION, reminder.getDescription());
        values.put(KEY_REMINDERS_COMPLETE, (reminder.isComplete()) ? 1 : 0); //Convert boolean to int
        values.put(KEY_REMINDERS_DUEDATE, reminder.getDueDateAsEpoc());
        if(reminder.latitude == null) {
            values.putNull(KEY_REMINDERS_LATITUDE);
            values.putNull(KEY_REMINDERS_LONGITUDE);
        }
        else {
            values.put(KEY_REMINDERS_LATITUDE, reminder.getLocation().latitude);
            values.put(KEY_REMINDERS_LONGITUDE, reminder.getLocation().longitude);
        }
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "(" +
                KEY_REMINDERS_ID + " INTEGER PRIMARY KEY," +
                KEY_REMINDERS_TITLE + " TEXT," +
                KEY_REMINDERS_DESCRIPTION + " TEXT," +
                KEY_REMINDERS_COMPLETE + " INT," +
                KEY_REMINDERS_DUEDATE + " LONG," +
                KEY_REMINDERS_LATITUDE + " LONG," +
                KEY_REMINDERS_LONGITUDE + " LONG" + ")";

        sqLiteDatabase.execSQL(CREATE_REMINDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            //Delete old database and create new one
            //All data will be lost
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
            onCreate(sqLiteDatabase);
        }
    }

    void addReminder(Reminder reminder) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = createContentValues(reminder);
            db.insertOrThrow(TABLE_REMINDERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("DATABASE", "Error while trying to add new reminder");
        } finally {
            db.endTransaction();
        }
    }

    //Edits a reminder that already exists in the database by replacing it by the gives reminder object
    //Transaction will only be successful if a single row is edited
    void editReminder(Reminder reminder) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        ContentValues values = createContentValues(reminder);
        int affectedRows;
        affectedRows = db.updateWithOnConflict(TABLE_REMINDERS, values, KEY_REMINDERS_ID + " = ?", new String[]{String.valueOf(reminder.getId())}, SQLiteDatabase.CONFLICT_REPLACE);
        if (affectedRows == 1) {
            db.setTransactionSuccessful();
        } else {
            Log.d("DATABASE", "Error while trying to edit a reminder");
        }
        db.endTransaction();
    }

    public void deleteReminder(Reminder reminder) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        int affectedRows;
        affectedRows = db.delete(TABLE_REMINDERS, KEY_REMINDERS_ID + " = ?", new String[]{String.valueOf(reminder.getId())});
        if (affectedRows == 1) {
            db.setTransactionSuccessful();
        } else {
            Log.d("DATABASE", "Error while trying to edit a reminder");
        }
        db.endTransaction();
    }

    ArrayList<Reminder> getAllReminders() {
        ArrayList<Reminder> reminderList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_ALLREMINDERS, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Reminder newReminder = new Reminder(
                            cursor.getInt(cursor.getColumnIndex(KEY_REMINDERS_ID)),
                            cursor.getString(cursor.getColumnIndex(KEY_REMINDERS_TITLE)),
                            cursor.getString(cursor.getColumnIndex(KEY_REMINDERS_DESCRIPTION)),
                            dateToCalendar(new Date(cursor.getLong(cursor.getColumnIndex(KEY_REMINDERS_DUEDATE)))),
                            1 == cursor.getInt(cursor.getColumnIndex(KEY_REMINDERS_COMPLETE)),
                            null,
                            null);
                    if(!cursor.isNull(cursor.getColumnIndex(KEY_REMINDERS_LATITUDE))) {
                        LatLng location = new LatLng(
                                cursor.getDouble(cursor.getColumnIndex(KEY_REMINDERS_LATITUDE)),
                                cursor.getDouble(cursor.getColumnIndex(KEY_REMINDERS_LONGITUDE)));
                        newReminder.setLocation(location);
                    }
                    reminderList.add(newReminder);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("DATABASE", "There was an error while extracting reminder objects from the database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        Log.d("DATABASE", "Get All Reminders Called. There are " + reminderList.size() + " reminders in the list.");
        return reminderList;
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }
}
