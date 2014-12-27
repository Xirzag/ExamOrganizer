package ml.ftvpe.exam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alberto on 21/12/14.
 */
public class ExamSQLiteHelper extends SQLiteOpenHelper implements java.io.Serializable {
    String sqlCreate = "CREATE TABLE Exams (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            " name TEXT, topic INTEGER, topicLength INTEGER, day TEXT)";
    public ExamSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Exams");
        db.execSQL(sqlCreate);
    }
}
