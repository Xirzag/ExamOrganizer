package ml.ftvpe.exam;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by alberto on 20/12/14.
 */
public class Display_exam extends TableLayout implements java.io.Serializable{
    public TextView info;
    public TextView daysForExam;
    public TextView daysForTopic;
    public Exam exam;

    public Display_exam(Context context) {
        super(context);
        create();
    }

    public Display_exam(Context context, AttributeSet attrs) {
        super(context, attrs);
        create();
    }

    private void create() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li =
                (LayoutInflater)getContext().getSystemService(infService);
        li.inflate(R.layout.display_exam, this, true);

        daysForExam = (TextView) findViewById(R.id.daysForExam);
        daysForTopic = (TextView) findViewById(R.id.daysForTopic);
        info = (TextView) findViewById(R.id.info);
    }

    public void updateInfo(){
        info.setText(exam.getExamName()+" Tema "+exam.getCurrentTopic());
        daysForExam.setText(exam.daysToExam()+ " " + exam.getDayOfExamStr());
        daysForTopic.setText(exam.daysOfTopic()+" Libres: "+exam.freeDaysOfTopic());
    }

    public void setInfo(Exam exam){
        this.exam = exam;
        updateInfo();
    }
}
