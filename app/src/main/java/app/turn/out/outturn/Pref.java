package app.turn.out.outturn;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Pref {

    Context context;


    SharedPreferences sharedPreferences;
    String MyPREFERENCES="OutTurn";

    public Pref(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void setMacAddress(String mac)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("scalemac",mac);
        editor.commit();
    }

    public String getMacAddress()
    {
        return sharedPreferences.getString("scalemac","");
    }






}
