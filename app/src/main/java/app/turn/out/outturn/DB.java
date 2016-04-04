package app.turn.out.outturn;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DB extends SQLiteOpenHelper {

    private static final String DATA_BASE = "Outturn.db";
    private static final String TABLE = "RESULT_WEIGHT",TABLE_SESSION="RESULT_SESSION";
    private static final String COL_ID = "ID", COL_DATE = "DATE", COL_WEIGHT = "WEIGHT",COL_NFC="NFCID",COL_NOBAGID="NOBAGID";

    public DB(Context context) {
        super(context, DATA_BASE, null, 1);
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE TEXT, WEIGHT FLOAT, NFCID INTEGER,NOBAGID INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_SESSION + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NOBAG INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_SESSION);
        onCreate(db);
    }

    public void insertSessionData(int numbag){
        SQLiteDatabase db1 = getWritableDatabase();

        ContentValues cv1=new ContentValues();

        cv1.put("NOBAG",numbag);


        long result1=db1.insert(TABLE_SESSION,null,cv1);
    }

    public boolean insertData(String date, float weight, int nfcids,int numBag) {



        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_DATE, date);
        cv.put(COL_WEIGHT, weight);
        cv.put(COL_NFC, nfcids);
        cv.put(COL_NOBAGID,numBag);

        long result = db.insert(TABLE, null, cv);





        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ArrayList<OutTurn> getDatas(String date) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("select distinct w.nobagid,(select rw.id  from result_weight rw where w.nobagid=rw.nobagid) as ID,(select rw.date  from result_weight rw where w.nobagid=rw.nobagid) as DATE,(select sum (rw.weight)  from result_weight rw where w.nobagid=rw.nobagid) as WEIGHT,(select rw.nobag  from result_session rw where w.nobagid=rw.id) as NOBAG  from result_weight w  where w.date='"+date+"'", null);



        ArrayList<OutTurn> outTurns = new ArrayList<OutTurn>();

        while (res.moveToNext()) {
            OutTurn outturn = new OutTurn();

            outturn.setId(res.getInt(0));
            outturn.setwNumBag(res.getInt(1));
            outturn.setDate(res.getString(2));
            outturn.setWeight(res.getFloat(3));
            outturn.setsNumBag(res.getInt(4));

            //outturn.setDate(res.getString(2));
            //outturn.setWeight(res.getFloat(2));
           // outturn.setNfcId(res.getInt(3));

           // outturn.setwNumBag(res.getInt(4));
           // outturn.setsId(res.getInt(5));
           // outturn.setsNumBag(res.getInt(6));


            outTurns.add(outturn);
        }

        return outTurns;
    }

    public int getLastId(){

        SQLiteDatabase db=getWritableDatabase();
        String query = "SELECT ID from "+TABLE_SESSION+" order by ID DESC limit 1";
        Cursor c = db.rawQuery(query,null);
        if (c != null && c.moveToFirst()) {
            return  c.getInt(0); //The 0 is the column index, we only have 1 column, so the index is 0
        }

        return 0;
    }


    //public float getSum(){
    //    SQLiteDatabase db=getWritableDatabase();
    //    Cursor res=db.rawQuery("SELECT sum(weight) as total FROM RESULT_WEIGHT ", null);

    //    float total=res.getFloat(0);
    //    return total;
   // }


    public ArrayList<OutTurn> getSearchDatas(String from,String to) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("select distinct w.nobagid,(select rw.id  from result_weight rw where w.nobagid=rw.nobagid) as ID,(select rw.date  from result_weight rw where w.nobagid=rw.nobagid) as DATE,(select sum (rw.weight)  from result_weight rw where w.nobagid=rw.nobagid) as WEIGHT,(select rw.nobag  from result_session rw where w.nobagid=rw.id) as NOBAG  from result_weight w  where w.date  between '"+from+"' and '" + to + "'", null);

        ArrayList<OutTurn> outTurns = new ArrayList<OutTurn>();

        while (res.moveToNext()) {
            OutTurn outturn = new OutTurn();
            outturn.setId(res.getInt(0));
            outturn.setwNumBag(res.getInt(1));
            outturn.setDate(res.getString(2));
            outturn.setWeight(res.getFloat(3));
            outturn.setsNumBag(res.getInt(4));

            outTurns.add(outturn);
        }

        return outTurns;
    }




    public ArrayList<OutTurn> getSearchDatasToReport(String date,int numBag) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Result_weight w left join Result_session s where w.NOBAGID=s.id and w.date='" + date + "' and w.NOBAGID=" + numBag, null);

        ArrayList<OutTurn> outTurns = new ArrayList<OutTurn>();

        while (res.moveToNext()) {
            OutTurn outturn = new OutTurn();
            outturn.setId(res.getInt(0));
            outturn.setDate(res.getString(1));
            outturn.setWeight(res.getFloat(2));
            outturn.setNfcId(res.getInt(3));

            outturn.setwNumBag(res.getInt(4));
            outturn.setsId(res.getInt(5));
            outturn.setsNumBag(res.getInt(6));

            outTurns.add(outturn);
        }

        return outTurns;
    }





}
