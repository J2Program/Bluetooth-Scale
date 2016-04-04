package app.turn.out.outturn;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Report extends AppCompatActivity implements View.OnClickListener {

    DB db;
    ListView listView;
    TextView tvTotal,tvRTotal,tvWTotal;
    Button btnFromTo;

    private Button fromDateEtxt;
    private Button toDateEtxt;

    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        fromDateEtxt = (Button) findViewById(R.id.etxt_fromdate);
        toDateEtxt = (Button) findViewById(R.id.etxt_todate);

        tvRTotal = (TextView) findViewById(R.id.tvRTotal);
        tvWTotal = (TextView) findViewById(R.id.tvWTotal);

        setDateTimeField();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        db = new DB(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());



        ArrayList<OutTurn> out = db.getDatas(formattedDate);
        float g=0;
        for (int a=0; a<out.size(); a++){
            g=g+out.get(a).getWeight();
        }




        String siz = String.valueOf(out.size());
        //tvTotal=(TextView)findViewById(R.id.tvTotal);
       // tvTotal.setText("Today Records : " + siz);
        tvRTotal.setText(siz+" (Today)");
        tvWTotal.setText( String.format("%.1f",g)+"Kg (Today)");




        final String[] arry = new String[out.size()];

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,arry);

        CustomAdapter adapter = new CustomAdapter(this, out, arry);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);



    }

    private void setDateTimeField() {
        fromDateEtxt.setOnClickListener(this);
        toDateEtxt.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
               newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(newDate.getTime());


                fromDateEtxt.setText(formattedDate );

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
             Calendar newDate = Calendar.getInstance();
             newDate.set(year, monthOfYear, dayOfMonth);


                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = df.format(newDate.getTime());
                toDateEtxt.setText(formattedDate);


                ArrayList<OutTurn> outs = db.getSearchDatas(fromDateEtxt.getText()+"",toDateEtxt.getText()+"");



                final String[] arrys = new String[outs.size()];

                tvRTotal.setVisibility(View.VISIBLE);
                tvRTotal.setText(arrys.length+"");

                float i=0;

                for(int a=0; a<outs.size(); a++){
                    i=i+outs.get(a).getWeight();
                }

                tvWTotal.setVisibility(View.VISIBLE);
                tvWTotal.setText( String.format("%.1f",i)+"Kg");


                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,arry);

                CustomAdapter adapters = new CustomAdapter(Report.this, outs, arrys);

                listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(adapters);

            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }


    @Override
    public void onClick(View view) {
        if(view == fromDateEtxt) {
            fromDatePickerDialog.show();
        } else if(view == toDateEtxt) {
            toDatePickerDialog.show();
        }
    }



}
