package com.inka.netsync.data.cache.db;

public class SqlStorage {

	public static final String ALTER_SQL_SETTING_TABLE = "ALTER TABLE Setting ADD " + 
													"swXPlay INTEGER NOT NULL DEFAULT 0" +
													";";
	
	public static final String ALTER_SQL_CONTENT_TABLE = "ALTER TABLE Content ADD " + 
													"licenseInfo TEXT NOT NULL DEFAULT ''" +
													";";
	
	public static final String ALTER_SQL_CONTENT_TABLE_ADD_SERIAL = "ALTER TABLE Content ADD " + 
													"serial TEXT NOT NULL DEFAULT ''" +
													";";
	
	public static final String ALTER_SQL_CATEGORY_TABLE_ADD_TITLEID = "ALTER TABLE Category ADD " + 
													"titleId INTEGER" +
													";";
	
	public static final String ALTER_SQL_CONTENT_TABLE_ADD_LMSRAWSECTION = "ALTER TABLE Content ADD " + 
													"lmsRawSection TEXT NOT NULL DEFAULT ''" +
													";";
	
	public static final String ALTER_SQL_CONTENT_TABLE_ADD_CONTENTIMAGE = "ALTER TABLE Content ADD " + 
													"contentImage TEXT NOT NULL DEFAULT ''" +
													";";
	
	public static final String ALTER_SQL_TITLE_TABLE_ADD_TITLEDESCRIPTION = "ALTER TABLE Title ADD " + 
													"titleDescription TEXT NOT NULL DEFAULT ''" +
													";";

	public static final String ALTER_SQL_CONTENT_TABLE_ADD_DIRECTORY = "ALTER TABLE Content ADD " +
			"directory TEXT NOT NULL DEFAULT ''" +
			";";

	public static final String SELECT_SQL_CONTENT = "SELECT * FROM 'Content' %s ORDER BY contentName";
	public static final String SELECT_SQL_BOOKMARK = "SELECT * FROM 'Bookmark' %s ORDER BY bookmarkLocation";


	public static final String MIGRATION_DROP_CONTENT_TABLE = "DROP TABLE " + MetaData.CONTENT_TABLE;
	public static final String MIGRATION_RENAME_CONTENT_TABLE = "ALTER TABLE " + MetaData.CONTENT_TABLE_BACKUP + " RENAME TO " + MetaData.CONTENT_TABLE;

	public static final String MIGRATION_DROP_BOOKMARK_TABLE = "DROP TABLE " + MetaData.BOOKMARK_TABLE;
	public static final String MIGRATION_RENAME_BOOKMARK_TABLE = "ALTER TABLE " + MetaData.BOOKMARK_TABLE_BACKUP + " RENAME TO " + MetaData.BOOKMARK_TABLE;

}