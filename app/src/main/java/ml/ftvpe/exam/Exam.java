package ml.ftvpe.exam;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteProgram;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by alberto on 20/12/14.
 */
public class Exam {
    private Calendar dayOfExam;
    private String examName;
    private int topicLenght;
    private int currentTopic = 1;
    private ExamSQLiteHelper dbh;
    int ID;

    public Exam(int day, int month, int year, String examName, int topicLenght, ExamSQLiteHelper dbh) {
        initCommon(day, month, year,examName, topicLenght, dbh);
        initDB();
    }

    public Exam(int day, int month, int year, String examName, int topicLenght, ExamSQLiteHelper dbh, int id) {
        initCommon(day, month, year,examName, topicLenght, dbh);
        this.ID = id;
    }

    private void initCommon(int day, int month, int year, String examName, int topicLenght, ExamSQLiteHelper dbh){
        this.dbh = dbh;
        this.dayOfExam = Calendar.getInstance();
        this.dayOfExam.set(year,month-1,day);
        this.examName = examName;
        this.topicLenght = topicLenght;
    }

    private void initDB() {
        SQLiteDatabase db = dbh.getWritableDatabase();

        if(db != null) {
            db.execSQL("INSERT INTO Exams (name, topic, topicLength, day) " +
                    "VALUES (?, " + currentTopic +
                    ", " + topicLenght + ", '" + getDayOfExamStr() + "')",new String[]{examName});
        }
        findId(db);

        db.close();
    }

    private void findId(SQLiteDatabase db){
        db = dbh.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT ID FROM Exams WHERE name=? AND " +
                "topic = "+currentTopic+" AND topicLength = "+topicLenght+" AND day = '"+
                getDayOfExamStr()+"'", new String[]{examName});

        int id = -1;
        if (c.moveToFirst()) {
            do id = c.getInt(0);
            while (c.moveToNext());
        }
        Log.i("id",""+id);
        this.ID = id;
    }

    public Calendar getDayOfExam() {
        return dayOfExam;
    }

    public String getDayOfExamStr() {
        return new SimpleDateFormat("dd/MM/yyyy").format(dayOfExam.getTime());
    }

    public String getDayOfExamStr(String Path) {
        return new SimpleDateFormat(Path).format(dayOfExam.getTime());
    }

    public String getExamName() {
        return examName;
    }

    public int getTopicLenght() {
        return topicLenght;
    }

    public int getCurrentTopic() {
        return currentTopic;
    }

    public void setCurrentTopic(int whichTopic) {
        if(whichTopic<topicLenght&&whichTopic>0) {
            this.currentTopic = whichTopic;
            updateDB("topic",whichTopic);
        }
    }

    public void topicStudied(){
        if(currentTopic<topicLenght) updateDB("topic", ++currentTopic);
    }

    private void updateDB(String column, int num){
        SQLiteDatabase db = dbh.getWritableDatabase();

        if(db != null) db.execSQL("UPDATE Exams SET "+column+" = "+num+" WHERE ID ="+ID);

        db.close();
    }

    public long daysToExam(){
        return (dayOfExam.getTimeInMillis()-Calendar.getInstance().getTimeInMillis())/86400000 ;
    }

    public long daysOfTopic(){
        return daysToExam()/(topicLenght-currentTopic+1);
    }

    public void remove(){
        SQLiteDatabase db = dbh.getWritableDatabase();

        if(db != null) db.execSQL("DELETE FROM Exams WHERE ID ="+ID);

        db.close();
    }

    public long freeDaysOfTopic(){
        return daysToExam()%topicLenght;
    }

}

