package comp5216.sydney.edu.au.finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import comp5216.sydney.edu.au.finalproject.pojo.User;

public class MySQLiteOpenHelper  extends SQLiteOpenHelper {


    private static final String DB_NAME = "mySQLite.db";

    private static final String TABLE_NAME_USER = "user";

    private static final String CREATE_TABLE_SQL = "create table " + TABLE_NAME_USER + "(id INTEGER PRIMARY KEY AUTOINCREMENT, userName text, password text)";
    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long insertData(User user){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName",user.getUserName());
        values.put("password",user.getPassword());
        return db.insert(TABLE_NAME_USER,null,values);

    }

    public List<User> selectByUserNameAndPassword(String strUserName, String strPassWord) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USER, null, "userName=? and password=?", new String[]{strUserName, strPassWord}, null, null, null);
        List<User> userList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String userName1 = cursor.getString(cursor.getColumnIndex("userName"));
                @SuppressLint("Range") String password1 = cursor.getString(cursor.getColumnIndex("password"));
                User user = new User();
                user.setUserName(userName1);
                user.setPassword(password1);
                userList.add(user);
            }
            return userList;
        }
        return null;
    }

    public List<User> selectByUserName(String strUserName) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_USER, null, "userName=?", new String[]{strUserName}, null, null, null);
        List<User> userList = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String userName1 = cursor.getString(cursor.getColumnIndex("userName"));
                User user = new User();
                user.setUserName(userName1);
                userList.add(user);
            }
            return userList;
        }
        return null;
    }
}
