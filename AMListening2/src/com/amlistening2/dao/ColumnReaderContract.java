package com.amlistening2.dao;

import android.provider.BaseColumns;

/**
 * @author Vijay Chiliveri (cvijaymanu@gmail.com)
 * 
 */
public class ColumnReaderContract {

	public abstract class ColumnEntry implements BaseColumns {

		public static final int DATABASE_VERSION = 2;
		public static final String DATABASE_NAME = "MUSICSOCIAL.db";
		public static final String TABLE_NAME = "MUSIC_SOCIAL";
		public static final String COLUMN_NAME_ENTRY_ID = "TRACK_ID";
		public static final String COLUMN_NAME_TRACK_ALBUM = "TRACK_ALBUM";
		public static final String COLUMN_NAME_TRACK_NAME = "TRACK_NAME";
		//public static final String COLUMN_NAME_PLAYED_COUNT = "PLAYED_COUNT";
		//public static final String COLUMN_NAME_PLAYED_DATE = "PLAYED_DATE";
		public static final String COLUMN_NAME_TRACK_ARTIST = "ARTIST";
		public static final String COLUMN_NAME_TRACK_POSTED_FACEBOOK = "POSTED_FACEBOOK";
		public static final String COLUMN_NAME_TRACK_POSTED_GPLUS = "POSTED_GPLUS";
		public static final String COLUMN_NAME_TRACK_POSTED_TWITTER = "POSTED_TWITTER";
		public static final String COLUMN_NAME_SOCIAL_POSTE_DATE = "POSTED_DATE";
		
		//child table columns
		public static final String TABLE_NAME_TRACK_RECORD = "TRACK_RECORDS";
		public static final String COLUMN_NAME_REFERENCE_ENTRY_ID = "TRACK_ID";
		public static final String COLUMN_NAME_TRACK_PLAYED_DATE = "PLAYED_DATE";
		
	}
	
	private ColumnReaderContract() {
	}
}
