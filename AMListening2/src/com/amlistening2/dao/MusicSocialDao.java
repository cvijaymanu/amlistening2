/**
 * 
 */
package com.amlistening2.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import com.amlistening2.commons.AmListen2Util;
import com.amlistening2.commons.Logger;
import com.amlistening2.commons.TrackInfoTO;

/**
 * @author vijju
 * 
 */
public class MusicSocialDao implements IMusicSocialDAO {

	private MusicSocailDBHelper dbHelper;
	private final static String TAG = "MusicSocialDao";

	public MusicSocialDao(Context context) {
		dbHelper = new MusicSocailDBHelper(context);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void insertTrackInfo(TrackInfoTO trackInfoTO) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		/*
		 * contentValues.put(
		 * ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID, null);
		 */
		try {
			contentValues.put(
					ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ALBUM,
					trackInfoTO.getTrackAlbum());
			contentValues.put(
					ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_NAME,
					trackInfoTO.getTrackName());
			contentValues.put(
					ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ARTIST,
					trackInfoTO.getTrackArtist());
			// contentValues.put(
			// ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_COUNT,
			// trackInfoTO.getPlayedCount() + 1);
			// contentValues.put(
			// ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_DATE,
			// Calendar.getInstance().getTime().toString());
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_FACEBOOK,
							false);
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_GPLUS,
							false);
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_TWITTER,
							false);

			long newRowId = db.insert(
					ColumnReaderContract.ColumnEntry.TABLE_NAME, null,
					contentValues);
			if (newRowId != -1) {
				trackInfoTO.setTrackId(newRowId);
				insertTrackPlayCount(newRowId);
			}
			Logger.d("MUSICSOCIALDAO", "New rowid is " + newRowId);
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			assert db != null;
			db.close();
		}
	}

	/**
	 * @param contentValues
	 * @param newRowId
	 */
	private void insertTrackPlayCount(long newRowId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues
				.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_REFERENCE_ENTRY_ID,
						newRowId);
		contentValues.put(
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_PLAYED_DATE,
				AmListen2Util.getTodayFormattedDate());
		long row = db.insertOrThrow(
				ColumnReaderContract.ColumnEntry.TABLE_NAME_TRACK_RECORD, null,
				contentValues);
		Logger.d("Inserting track table", "Row id: " + row);
	}

	@Override
	public void trackLookup(TrackInfoTO trackInfoTO) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String selection = "";
		String[] projection = {
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID,
				// ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_COUNT,
				// ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_DATE,
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_FACEBOOK,
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_GPLUS,
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_TWITTER };

		selection += ((null != trackInfoTO.getTrackAlbum()) ? ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ALBUM
				+ "=? and "
				: "")
				+ ((null != trackInfoTO.getTrackArtist()) ? ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ARTIST
						+ "=? and "
						: "")
				+ ((null != trackInfoTO.getTrackArtist()) ? ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_NAME
						+ "=?"
						: "");
		try {
			List<String> selectionArgs = new ArrayList<String>(10);
			if (trackInfoTO.getTrackAlbum() != null)
				selectionArgs.add(trackInfoTO.getTrackAlbum());
			if (trackInfoTO.getTrackArtist() != null)
				selectionArgs.add(trackInfoTO.getTrackArtist());
			if (trackInfoTO.getTrackName() != null)
				selectionArgs.add(trackInfoTO.getTrackName());
			Cursor c = db.query(ColumnReaderContract.ColumnEntry.TABLE_NAME,
					projection, selection,
					selectionArgs.toArray(new String[selectionArgs.size()]),
					null, null, null);
			if (c.isBeforeFirst() && c.getCount() != 0) {
				c.moveToFirst();
				trackInfoTO
						.setTrackId(c.getLong(c
								.getColumnIndexOrThrow(ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID)));
				// trackInfoTO
				// .setPlayedCount(c.getInt(c
				// .getColumnIndexOrThrow(ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_COUNT)));
				// trackInfoTO
				// .setPlayedDate(c.getString(c
				// .getColumnIndexOrThrow(ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_DATE)));
				trackInfoTO
						.setPostToFacebook(Boolean.valueOf((1 == c.getInt(c
								.getColumnIndexOrThrow(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_FACEBOOK))) ? "true"
								: "false"));
				trackInfoTO
						.setPostedToGPlus(Boolean.valueOf((1 == c.getInt(c
								.getColumnIndexOrThrow(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_GPLUS))) ? "true"
								: "false"));
				trackInfoTO
						.setPostedToTwitter(Boolean.valueOf((1 == c.getInt(c
								.getColumnIndexOrThrow(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_TWITTER))) ? "true"
								: "false"));
				getTrackPlaybackInfo(trackInfoTO); // load track play count from
													// Track table

			}
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
		} finally {
			db.close();
		}
	}

	@SuppressLint("NewApi")
	private void getTrackPlaybackInfo(TrackInfoTO trackInfoTO) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String selectTrackCount = ColumnReaderContract.ColumnEntry.COLUMN_NAME_REFERENCE_ENTRY_ID
				+ "=" + trackInfoTO.getTrackId();

		String selectTrackCountToday = selectTrackCount
				+ " and "
				+ ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_PLAYED_DATE
				+ "='" + AmListen2Util.getTodayFormattedDate() + "'";
		String selectedTrackCountRecent = selectTrackCount
				+ " AND PLAYED_DATE >= DATE('NOW','LOCALTIME','-7 DAY') AND PLAYED_DATE <= DATE('NOW','LOCALTIME') ";
		long trackCount = DatabaseUtils.queryNumEntries(db,
				ColumnReaderContract.ColumnEntry.TABLE_NAME_TRACK_RECORD,
				selectTrackCount);
		long trackPlayedToday = DatabaseUtils.queryNumEntries(db,
				ColumnReaderContract.ColumnEntry.TABLE_NAME_TRACK_RECORD,
				selectTrackCountToday);
		long trackPlayedRecently = DatabaseUtils.queryNumEntries(db,
				ColumnReaderContract.ColumnEntry.TABLE_NAME_TRACK_RECORD,
				selectedTrackCountRecent);
		trackInfoTO.setTotalPlayedCount(trackCount);
		trackInfoTO.setTodayPlayedCount(trackPlayedToday);
		trackInfoTO.setWeekPlayedCount(trackPlayedRecently);
		Logger.d("Track Count", "Track count is" + trackCount
				+ "Today played count is " + trackPlayedToday);
	}

	@Override
	public boolean updateTrackRecord(TrackInfoTO trackInfoTO) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		boolean status = false;
		try {
			// contentValues.put(
			// ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_COUNT,
			// trackInfoTO.getPlayedCount());
			// contentValues.put(
			// ColumnReaderContract.ColumnEntry.COLUMN_NAME_PLAYED_DATE,
			// trackInfoTO.getPlayedDate());
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_FACEBOOK,
							trackInfoTO.postToFacebook());
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_GPLUS,
							trackInfoTO.isPostedToGPlus());
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_TWITTER,
							trackInfoTO.isPostedToTwitter());
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_SOCIAL_POSTE_DATE,
							AmListen2Util.getTodayFormattedDate());
			String selection = ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID
					+ " =?";
			String[] selArgs = { String.valueOf(trackInfoTO.getTrackId()) };

			status = (db.update(ColumnReaderContract.ColumnEntry.TABLE_NAME,
					contentValues, selection, selArgs) == 0) ? false : true;
			insertTrackPlayCount(trackInfoTO.getTrackId());
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			db.close();
		}
		return status;
	}

	@Override
	public TrackInfoTO getTopSongFor(String timePeriod) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		TrackInfoTO trackInfoTO = new TrackInfoTO();
		String rawQuery = "SELECT T.TRACK_ID,COUNT(*) AS COUNT FROM TRACK_RECORDS T, MUSIC_SOCIAL M WHERE T.TRACK_ID=M.TRACK_ID AND M.POSTED_FACEBOOK=0 AND ";
		String lastQuery = " GROUP BY T.TRACK_ID, T.PLAYED_DATE ORDER BY COUNT DESC LIMIT 1";

		// forming the query based on timePeriod to pull the top song
		if (timePeriod.equalsIgnoreCase(AmListen2Util.TOP_SONG_TODAY)) {
			rawQuery += "T.PLAYED_DATE= DATE('NOW','LOCALTIME')";
		} else if (timePeriod.equalsIgnoreCase(AmListen2Util.TOP_SONG_WEEK)) {
			rawQuery += "T.PLAYED_DATE >= DATE('NOW','LOCALTIME','-7 DAY') AND T.PLAYED_DATE <= DATE('NOW','LOCALTIME')";
		} else if (timePeriod.equalsIgnoreCase(AmListen2Util.TOP_SONG_MONTH)) {
			rawQuery += "T.PLAYED_DATE >= DATE('NOW','LOCALTIME','-1 MONTH') AND T.PLAYED_DATE <= DATE('NOW','LOCALTIME')";
		}
		rawQuery += lastQuery;
		Logger.d("getTopSongFor", "formed query is " + rawQuery);
		Cursor cursor = db.rawQuery(rawQuery, null);
		if (cursor != null && cursor.isBeforeFirst() && cursor.getCount() != 0) {
			cursor.moveToFirst();
			trackInfoTO
					.setTrackId(cursor.getLong(cursor
							.getColumnIndex(ColumnReaderContract.ColumnEntry.COLUMN_NAME_REFERENCE_ENTRY_ID)));
			trackInfoTO.setTotalPlayedCount(cursor.getInt(cursor
					.getColumnIndex("COUNT")));
			Logger.d("getTopSongFor", "Track ID: " + trackInfoTO.getTrackId());
			Logger.d("getTopSongFor",
					"Play Count: " + trackInfoTO.getTotalPlayedCount());
		}
		loadTrackInfo(trackInfoTO);
		Logger.d("getTopSongFor", "Loaded the track info");
		return trackInfoTO;
	}

	private void loadTrackInfo(TrackInfoTO trackInfoTO) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		String selection = ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID
				+ "=?";
		String[] selectionArgs = { String.valueOf(trackInfoTO.getTrackId()) };
		String[] projection = {
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ALBUM,
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ARTIST,
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_NAME,
				ColumnReaderContract.ColumnEntry.COLUMN_NAME_SOCIAL_POSTE_DATE };
		try {
			Cursor cursor = database.query(
					ColumnReaderContract.ColumnEntry.TABLE_NAME, projection,
					selection, selectionArgs, null, null, null);
			if (cursor != null & cursor.isBeforeFirst()
					&& cursor.getCount() != 0) {
				cursor.moveToFirst();
				trackInfoTO
						.setTrackAlbum(cursor.getString(cursor
								.getColumnIndex(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ALBUM)));
				trackInfoTO
						.setTrackArtist(cursor.getString(cursor
								.getColumnIndex(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_ARTIST)));
				trackInfoTO
						.setTrackName(cursor.getString(cursor
								.getColumnIndex(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_NAME)));

				trackInfoTO
						.setSocialPostDate(new SimpleDateFormat(
								AmListen2Util.DATE_FORMAT, Locale.US).parse(cursor.getString(cursor
								.getColumnIndex(ColumnReaderContract.ColumnEntry.COLUMN_NAME_SOCIAL_POSTE_DATE))));

			}
		} catch (ParseException e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
	}

	@Override
	public boolean updateSocialPostStatus(long trackId, boolean status) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		ContentValues contentValues = new ContentValues();
		boolean updateStatus = false;
		try {
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_TRACK_POSTED_FACEBOOK,
							status);
			contentValues
					.put(ColumnReaderContract.ColumnEntry.COLUMN_NAME_SOCIAL_POSTE_DATE,
							AmListen2Util.getTodayFormattedDate());
			String selection = ColumnReaderContract.ColumnEntry.COLUMN_NAME_ENTRY_ID
					+ " =?";
			String[] selArgs = { String.valueOf(trackId) };

			updateStatus = (db.update(
					ColumnReaderContract.ColumnEntry.TABLE_NAME, contentValues,
					selection, selArgs) == 0) ? false : true;
		} catch (Exception e) {
			Log.e(TAG, Log.getStackTraceString(e));
		} finally {
			db.close();
		}
		return updateStatus;
	}

}
