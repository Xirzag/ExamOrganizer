package ml.ftvpe.exam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ExamSQLiteHelper dbh;
    private Display_exam[] examList;
    private StableArrayAdapter adapterList;
    private ArrayList<Display_exam> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbh = new ExamSQLiteHelper(MainActivity.this, "DBExams", null, 1);

        /*new Exam(16,1,2015,"Ec",5,dbh);
        new Exam(22,1,2015,"Me",9,dbh);*/

        addDisplayExam(dbh);



        list = new ArrayList<Display_exam>();
        for (int i = 0; i < examList.length; ++i) {
            list.add(examList[i]);
        }
        adapterList = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);


        final ListView examCmb;


        examCmb = (ListView)findViewById(R.id.LstOpciones);

        examCmb.setAdapter(adapterList);

        examCmb.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder menu = new AlertDialog.Builder(MainActivity.this);
                menu.setTitle("Opciones");

                menu.setItems(new String[]{"Borrar Examen","Tema aprendido","Poner tema 1"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Display_exam selectedExam = list.get(position);
                                switch(which){
                                    case 0: //Borrar examen
                                        selectedExam.exam.remove();
                                        list.remove(position);
                                        adapterList.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this,"Borrado",Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1: //Tema aprendido
                                        selectedExam.exam.topicStudied();
                                        selectedExam.updateInfo();
                                        adapterList.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this,"Tema aprendido",Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2: // poner temas a 1
                                        selectedExam.exam.setCurrentTopic(1);
                                        selectedExam.updateInfo();
                                        adapterList.notifyDataSetChanged();
                                        Toast.makeText(MainActivity.this,"Temas resetados",Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });

                menu.create().show();

                return true;
            }
        });



        testDB();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new:
                Intent intent = new Intent(MainActivity.this, AddExam.class);
                startActivityForResult(intent, request_code);

                return true;
            case R.id.action_settings:
                resetDB();
                list.clear();
                adapterList.notifyDataSetChanged();
                Toast.makeText(this,"Borrados",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    int request_code = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == request_code) && (resultCode == RESULT_OK)){
            String returned = data.getDataString();
            Toast.makeText(this,returned,Toast.LENGTH_SHORT).show();

            int startName = returned.indexOf(" ");

            Display_exam display_exam = new Display_exam(this);
            display_exam.setInfo(new Exam(
                    Integer.parseInt(returned.substring(0,2)),
                    Integer.parseInt(returned.substring(3,5)),
                    Integer.parseInt(returned.substring(6,10)),
                    returned.substring(startName+1),
                    Integer.parseInt(returned.substring(10,startName)),
                    dbh));
            list.add(display_exam);
            adapterList.notifyDataSetChanged();
        }
    }

    private void addDisplayExam(ExamSQLiteHelper dbh){
        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT name, topic, topicLength, day, ID FROM Exams ", null);
        View layout = findViewById(R.id.mainLayout);

        examList = new Display_exam[c.getCount()];
        Log.i("DB Rows",c.getCount()+"");

        if (c.moveToFirst()) {
            int i = 0;

            do {
                Exam exam = new Exam(Integer.parseInt(c.getString(3).substring(0,2)),
                        Integer.parseInt(c.getString(3).substring(3, 5)),
                        Integer.parseInt(c.getString(3).substring(6, 10)),
                        c.getString(0),c.getInt(2),dbh, c.getInt(4));
                exam.setCurrentTopic(c.getShort(1));

                Display_exam display_exam = new Display_exam(this);
                display_exam.setInfo(exam);
                examList[i] = display_exam;
                i++;
            } while(c.moveToNext());
        }

    }

    static class ViewHolder{
        TextView tit;
        TextView exam;
        TextView topic;
    }

    private class StableArrayAdapter extends ArrayAdapter<Display_exam> {

        HashMap<Display_exam, Integer> mIdMap = new HashMap<Display_exam, Integer>();
        Activity context;

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<Display_exam> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
            this.context = (Activity) context;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View item = convertView;
            ViewHolder holder;

            if(item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(R.layout.display_exam, null);

                holder = new ViewHolder();
                holder.tit = (TextView)item.findViewById(R.id.info);
                holder.exam = (TextView)item.findViewById(R.id.daysForExam);
                holder.topic = (TextView)item.findViewById(R.id.daysForTopic);

                item.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)item.getTag();
            }

            holder.tit.setText(list.get(position).info.getText());
            holder.exam.setText(list.get(position).daysForExam.getText());
            holder.topic.setText(list.get(position).daysForTopic.getText());

            return(item);
        }
    }

    private void testDB(){

        SQLiteDatabase db = dbh.getWritableDatabase();
        Cursor c = db.rawQuery(" SELECT name,day FROM Exams ", null);

        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Log.i("DB",c.getString(0)+" "+c.getString(1));
            } while(c.moveToNext());
        }

    }

    private void resetDB(){
        SQLiteDatabase db = dbh.getWritableDatabase();
        dbh.onUpgrade(db,0,1);
    }
}
