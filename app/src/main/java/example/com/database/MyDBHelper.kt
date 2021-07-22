package example.com.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteOpenHelper (context: Context): SQLiteOpenHelper(context, name, null, version) {
    companion object {
        private const val name = "mdatabase.db" //資料庫名稱
        private const val version = 1 //資料庫版本
    }

    override fun onCreate(db: SQLiteDatabase) {
        //建立資料表『myTable』，包含一個book字串欄位和一個price整數欄位
        db.execSQL("CREATE TABLE Phone(id integer PRIMARY KEY, name text NOT NULL, phone integer NOT NULL, address text, note text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //刪除資料表
        db.execSQL("DROP TABLE IF EXISTS Phone")
        //重建資料庫
        onCreate(db)
    }
}