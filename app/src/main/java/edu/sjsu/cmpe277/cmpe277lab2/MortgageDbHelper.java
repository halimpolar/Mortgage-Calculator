package edu.sjsu.cmpe277.cmpe277lab2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Polar on 3/16/17.
 */

public class MortgageDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Mortgage.db";

    public static class MortgageEntry implements BaseColumns {
        public static final String TABLE_NAME = "mortgage";
        public static final String COLUMN_NAME_PROPERTY = "property";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_CITY = "city";
        public static final String COLUMN_NAME_STATE = "state";
        public static final String COLUMN_NAME_ZIPCODE = "zipcode";
        public static final String COLUMN_NAME_LOAN_AMOUNT = "loan_amount";
        public static final String COLUMN_NAME_DOWN_PAYMENT = "down_payment";
        public static final String COLUMN_NAME_ANNUAL_PERCENTAGE_RATE = "apr";
        public static final String COLUMN_NAME_TERMS = "terms";
        public static final String COLUMN_NAME_CALCULATION = "calculation";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MortgageEntry.TABLE_NAME + " (" +
                    MortgageEntry._ID + " INTEGER PRIMARY KEY," +
                    MortgageEntry.COLUMN_NAME_PROPERTY + " TEXT," +
                    MortgageEntry.COLUMN_NAME_ADDRESS + " TEXT," +
                    MortgageEntry.COLUMN_NAME_CITY + " TEXT," +
                    MortgageEntry.COLUMN_NAME_STATE + " TEXT," +
                    MortgageEntry.COLUMN_NAME_ZIPCODE + " TEXT,"  +
                    MortgageEntry.COLUMN_NAME_LOAN_AMOUNT + " TEXT,"  +
                    MortgageEntry.COLUMN_NAME_DOWN_PAYMENT + " TEXT,"  +
                    MortgageEntry.COLUMN_NAME_ANNUAL_PERCENTAGE_RATE + " TEXT," +
                    MortgageEntry.COLUMN_NAME_TERMS + " TEXT," +
                    MortgageEntry.COLUMN_NAME_CALCULATION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MortgageEntry.TABLE_NAME;

    public MortgageDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
