package ml.ftvpe.exam;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class AddExam extends ActionBarActivity {

    private ExamSQLiteHelper dbh;
    //private StableArrayAdapter adapterList;
    private ArrayList<Display_exam> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);

        Button btnAddExam = (Button) findViewById(R.id.button);

        btnAddExam.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.editText)).getText().toString();
                String topics = ((EditText) findViewById(R.id.editText3)).getText().toString();
                if(topics.equals("")||Integer.parseInt(topics)==0){
                    Toast.makeText(AddExam.this,"No pueden haber 0 temas",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name.equals("")){
                    Toast.makeText(AddExam.this,"Debe añadir un nombre al tema",Toast.LENGTH_SHORT).show();
                    return;
                }
                String date = ((EditText) findViewById(R.id.editText2)).getText().toString();
                if(date.length()!=10){
                    Toast.makeText(AddExam.this,"La fecha no tiene el formato adecuado",Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(date));

                    if(calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()<0){
                        Toast.makeText(AddExam.this,"La fecha tiene que ser posterior a hoy",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (Exception e){
                    Toast.makeText(AddExam.this,"La fecha no tiene el formato adecuado",Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent data = new Intent();
                data.setData(Uri.parse(date+topics+" "+name));

                setResult(RESULT_OK, data);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_exam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
