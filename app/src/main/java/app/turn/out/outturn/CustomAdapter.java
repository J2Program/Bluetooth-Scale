package app.turn.out.outturn;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class CustomAdapter extends ArrayAdapter<String> {


    private final Context context;
    ArrayList<OutTurn> data;
    private final String[] values;
    DB db;
   // ArrayList<OutTurn> out;


    public CustomAdapter(Context context, ArrayList<OutTurn> data, String[] values) {
        super(context, R.layout.report_layout, values);
        this.context = context;
        this.data = data;
        this.values = values;
        db = new DB(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.report_layout, parent, false);

        TextView txtID = (TextView) rowView.findViewById(R.id.tvIDd);
        TextView txtDate = (TextView) rowView.findViewById(R.id.tvDate);
        TextView txtWeight = (TextView) rowView.findViewById(R.id.tvWeight);
        TextView txtNFC = (TextView) rowView.findViewById(R.id.tvNfc);

        TextView txtNoB = (TextView) rowView.findViewById(R.id.tvNoBagId);
        TextView tvNuBag = (TextView) rowView.findViewById(R.id.tvNuBag);



        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Toast.makeText(context, data.get(position).getId() + " ", Toast.LENGTH_LONG).show();

                    ArrayList<OutTurn> out = db.getSearchDatasToReport(data.get(position).getDate().toString(), data.get(position).getId());

                    StringBuffer stringBuffer = new StringBuffer();

                    float total = 0;

                    for (int a = 0; a < out.size(); a++) {
                        total = total + out.get(a).getWeight();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Date : " + data.get(position).getDate().toString() + "");


                    for (int a = 0; a < out.size(); a++) {
                        stringBuffer.append(" Weight: " + out.get(a).getWeight() + "KG | NFC ID: " + out.get(a).getNfcId() + "\n");
                    }
                    stringBuffer.append("\n");
                    stringBuffer.append("Total Weight : " + total + "KG\n");



                    builder.setMessage(stringBuffer.toString());
                    builder.show();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


        txtID.setText(data.get(position).getId() + " ");
        txtDate.setText(" " + data.get(position).getDate() + "");
       txtWeight.setText(" " + data.get(position).getWeight() + "KG");
        //txtNFC.setText(" " + data.get(position).getNfcId() + "");

        if(data.get(position).getsNumBag()==1){
            tvNuBag.setText("Number of Bag : ");

        }

        txtNoB.setText(" " + data.get(position).getsNumBag() + "");


        return rowView;
    }


}
