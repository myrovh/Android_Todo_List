package myrovh.to_dolistreminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class ReminderDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminderDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_REMINDERS = "reminders";
    private static final String KEY_REMINDERS_ID = "_id";
    private static final String KEY_REMINDERS_TITLE = "title";
    private static final String KEY_REMINDERS_DESCRIPTION = "description";
    private static final String KEY_REMINDERS_DUEDATE = "dueDate";
    private static final String KEY_REMINDERS_COMPLETE = "complete";

    private static ReminderDatabase singleton;

    private ReminderDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static ReminderDatabase getInstance(Context context) {
        if (singleton == null) {
            singleton = new ReminderDatabase(context.getApplicationContext());
        }
        return singleton;
    }

    private ContentValues createContentValues(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(KEY_REMINDERS_TITLE, reminder.getTitle());
        values.put(KEY_REMINDERS_DESCRIPTION, reminder.getDescription());
        values.put(KEY_REMINDERS_COMPLETE, (reminder.isComplete()) ? 1 : 0); //Convert boolean to int
        values.put(KEY_REMINDERS_DUEDATE, reminder.getDueDateAsEpoc());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_REMINDERS_TABLE = "CREATE TABLE " + TABLE_REMINDERS + "(" +
                KEY_REMINDERS_ID + " INTEGER PRIMARY KEY," +
                KEY_REMINDERS_TITLE + " TEXT," +
                KEY_REMINDERS_DESCRIPTION + " TEXT," +
                KEY_REMINDERS_COMPLETE + " INT," +
                KEY_REMINDERS_DUEDATE + " LONG" + ")";

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

    public void addReminder(Reminder reminder) {
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
    public void editReminder(Reminder reminder) {
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
}
