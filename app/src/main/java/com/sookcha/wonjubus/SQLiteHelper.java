package com.sookcha.wonjubus;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sookcha on 14. 11. 21..
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FavoriteDB";

    // Books table name
    private static final String TABLE_FAVORITES = "favorites";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_LOCATIONLAT = "locationLAT";
    private static final String KEY_LOCATIONLNG = "locationLNG";

    private static final String[] COLUMNS = {KEY_ID,KEY_NAME,KEY_NUMBER};

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOK_TABLE = "CREATE TABLE favorites ( " +
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "name TEXT, "+"number INTEGER, " + "location TEXT, " + "locationLAT TEXT, " + "locationLNG TEXT" + " )";

        db.execSQL(CREATE_BOOK_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS favorites");
        this.onCreate(db);
    }

    public void addFavorite(Favorites favorite){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, favorite.name);
        values.put(KEY_NUMBER, favorite.number);
        values.put(KEY_LOCATION, favorite.location);
        values.put(KEY_LOCATIONLAT, favorite.locationLAT);
        values.put(KEY_LOCATIONLNG, favorite.locationLNG);
        db.insert(TABLE_FAVORITES, null,values);
        db.close();
    }

    public Favorites getFavorite(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, COLUMNS, " id = ?", new String[] { String.valueOf(id) }, // d. selections args
        null, null,null,null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        Favorites favorite = new Favorites();
        favorite.id = Integer.parseInt(cursor.getString(0));
        favorite.name = cursor.getString(1);
        favorite.number = Integer.parseInt(cursor.getString(2));


        return favorite;
    }

    public List<Favorites> getAllFavorites() {
        List<Favorites> favorites = new LinkedList<Favorites>();
        String query = "SELECT * FROM " + TABLE_FAVORITES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Favorites favorite = null;
        if (cursor.moveToFirst()) {
            do {
                favorite = new Favorites();
                favorite.id = (Integer.parseInt(cursor.getString(0)));
                favorite.name = (cursor.getString(1));
                favorite.number = Integer.parseInt(cursor.getString(2));
                favorite.location = cursor.getString(3);

                favorites.add(favorite);
                } while (cursor.moveToNext());
            }
        return favorites;
        }

    public int updateFavorite(Favorites favorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", favorite.name); // get title
        values.put("number", favorite.number); // get author
        int i = db.update(TABLE_FAVORITES,values,KEY_ID+" = ?",new String[] { String.valueOf(favorite.id) }); //selection args
        db.close();
        return i;
    }


}
