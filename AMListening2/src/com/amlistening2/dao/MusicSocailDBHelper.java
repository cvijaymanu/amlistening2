/**
 * 
 */
package com.amlistening2.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amlistening2.commons.Logger;

/**
 * @author vijju
 * 
 */
public class MusicSocailDBHelper extends SQLiteOpenHelper {

	private static final String TEXT_TYPE = " TEXT";
	private static final String DATE_TYPE = " DATE";
	private static final String BOOLEAN_TYPE = " BOOLEAN";
	private static final String COMMA_SEP = ",";

	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ ColumnReaderContract.ColumnEntry.TABLE_NAME
			+ " ("
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID
			+ " INTEGER PRIMARY KEY,"
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ALBUM
			+ TEXT_TYPE
			+ COMMA_SEP
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_NAME
			+ TEXT_TYPE
			+ COMMA_SEP
			// + ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_COUNT
			// + TEXT_TYPE
			// + COMMA_SEP
			// + ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_DATE
			// + DATE_TYPE
			// + COMMA_SEP
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ARTIST
			+ TEXT_TYPE
			+ COMMA_SEP
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_FACEBOOK
			+ BOOLEAN_TYPE + COMMA_SEP
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_TWITTER
			+ BOOLEAN_TYPE + COMMA_SEP
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_GPLUS
			+ BOOLEAN_TYPE + COMMA_SEP
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_SOCIAL_POSTE_DATE
			+ DATE_TYPE	+ " )";
	private static final String SQL_CREATE_TRACK_COUNT_CHILD = "CREATE TABLE "
			+ ColumnReaderContract.ColumnEntry.TABLE_NAME_TRACK_RECORD + " ("
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_REFERENCE_ENTRY_ID
			+ " INTEGER" + COMMA_SEP
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_PLAYED_DATE
			+ DATE_TYPE + COMMA_SEP + "FOREIGN KEY("
			+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_REFERENCE_ENTRY_ID
			+ ") REFERENCES " + ColumnReaderContract.ColumnEntry.TABLE_NAME
			+ "(" + ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID
			+ "))";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ ColumnReaderContract.ColumnEntry.TABLE_NAME;
	private static final String SQL_DELETE_TRACK_ENTRIES = "DROP TABLE IF EXISTS "
			+ ColumnReaderContract.ColumnEntry.TABLE_NAME_TRACK_RECORD;

	public MusicSocailDBHelper(Context context) {
		super(context, ColumnReaderContract.ColumnEntry.DATABASE_NAME, null,
				ColumnReaderContract.ColumnEntry.DATABASE_VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			Logger.d("Track Table", "Creating Table1");
			db.execSQL(SQL_CREATE_ENTRIES);
			Logger.d("Track Table", "Creating Table2");
			db.execSQL(SQL_CREATE_TRACK_COUNT_CHILD);
			Logger.d(getClass().getName(), "Created tables");
		} catch (Exception e) {
			Log.e("Creating Tables", Log.getStackTraceString(e));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		try {
			db.execSQL(SQL_DELETE_TRACK_ENTRIES);
			db.execSQL(SQL_DELETE_ENTRIES);
			onCreate(db);
		} catch (Exception e) {
			Log.e(getClass().getName(), Log.getStackTraceString(e));
		} finally {
			// db.close();
		}
	}

	@Override
	@SuppressLint("Override")
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
