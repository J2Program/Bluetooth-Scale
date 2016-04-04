package app.turn.out.outturn;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;


import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;


import android.support.design.widget.CoordinatorLayout;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devpaul.bluetoothutillib.SimpleBluetooth;
import com.devpaul.bluetoothutillib.dialogs.DeviceDialog;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Handler;

public class StartActivity extends Activity {

    Pref pref;

    private SimpleBluetooth simpleBluetooth;
    private static final int SCAN_REQUEST = 119;
    private static final int CHOOSE_SERVER_REQUEST = 120;
    private String macAddress;
    StringBuilder stringBuilder;
    boolean isNFCConnected=false;
    boolean isSession=true;

    TextView tvResult, mTextView, mTextView1;
    Button  btnBack;

    int nfc;
    int numBags;

    DB db;





    MediaPlayer mp;



    int counter;
    float count;

    float max = 0;

    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    private int bgNumber = 0;
    public String nfcId;


    final static int soundMP=R.raw.testings;

    private boolean nfcArrayBoolean = false;

    ArrayList<String> nfcArray = new ArrayList<>(bgNumber);


    CoordinatorLayout coordinatorLayout;

    android.os.Handler handler = new android.os.Handler();
    android.os.Handler soundHandler = new android.os.Handler();
    AudioManager am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        am=(AudioManager) getSystemService(Context.AUDIO_SERVICE);


        mTextView = (TextView) findViewById(R.id.mTextView);
        mTextView1 = (TextView) findViewById(R.id.mTextView1);

        handler.postDelayed(runnable, 10000);
        soundHandler.postDelayed(soundRunnable,500);

        counter = 0;

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            //Toast.makeText(this, "Disable", Toast.LENGTH_LONG).show();
        } else {
            //  Toast.makeText(this, "Enable", Toast.LENGTH_LONG).show();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Number of Bags");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bgNumber = Integer.parseInt(input.getText().toString());
                numBags=bgNumber;
                if (bgNumber <= 0) {
                    startActivity(new Intent(StartActivity.this, StartActivity.class));
                    System.exit(0);
                }
                mTextView1.setText("Remaining : " + bgNumber + "");

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
                dialog.cancel();

            }
        });

        builder.show();


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.startActiy);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        tvResult = (TextView) findViewById(R.id.tvResult);

        db = new DB(this);

        // btnOnOff=(Button)findViewById(R.id.btnOnOff);
        // btnOnOff.setEnabled(false);


        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(1);
            }
        });


        /* btnOnOff.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            stopSound();
                                        }
                                    }

        ); */

        pref = new

                Pref(this);

        simpleBluetooth = new

                SimpleBluetooth(this, this);

        simpleBluetooth.initializeSimpleBluetooth();

        if (pref.getMacAddress().

                contentEquals("")

                )

        {
            simpleBluetooth.scan(SCAN_REQUEST);
            // Toast.makeText(StartActivity.this,pref.getMacAddress(),Toast.LENGTH_LONG).show();
        } else

        {
            // Toast.makeText(StartActivity.this,pref.getMacAddress(),Toast.LENGTH_LONG).show();
            simpleBluetooth.connectToBluetoothDevice(pref.getMacAddress());
        }

        // btnOnOff.setBackgroundResource(R.drawable.acceptoff);


        simpleBluetooth.setSimpleBluetoothListener(new SimpleBluetoothListener() {


                                                       @Override
                                                       public void onBluetoothDataReceived(byte[] bytes, String data) {


                                                           am.setStreamVolume(AudioManager.STREAM_MUSIC,am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                                                           String strIncom = new String(bytes);
                                                           if (strIncom.length() > 7) {
                                                               try {
                                                                   Log.d("SampleTesting", strIncom);

                                                                   String s = strIncom.substring(1, 5);
                                                                   count=0;
                                                                   count = Float.parseFloat(reverse(s));


                                                                   tvResult.setText(count + " KG");



                                                                   if (count > 23) {

                                                                       if (max < count)
                                                                           max = count;

                                                                       tvResult.setTextColor(Color.WHITE);
                                                                       coordinatorLayout.setBackgroundResource(R.drawable.bluetoothoff);

                                                                   }
                                                                   else {






                                                                       if (max > 23) {

                                                                           Calendar c = Calendar.getInstance();
                                                                           SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                                                           String formattedDate = df.format(c.getTime());

                                                                           if (bgNumber > 0 && nfcId != null) {

                                                                               if (nfcArrayBoolean == true) {


                                                                                   int nfc = Integer.parseInt(nfcId);

                                                                                    if(isSession==true){
                                                                                        db.insertSessionData(numBags);
                                                                                        isSession=false;
                                                                                    }

                                                                                   boolean result = db.insertData(formattedDate, max, nfc,db.getLastId());

                                                                                   bgNumber--;

                                                                                   mTextView.setText("Finding .. Next ..  ");
                                                                                   mTextView1.setText("Remaining : " + bgNumber + "");

                                                                                   mTextView1.setTextColor(Color.BLACK);



                                                                                   if (result == true) {
                                                                                       isNFCConnected=true;
                                                                                       LinearLayout s1=(LinearLayout)findViewById(R.id.liLayout);
                                                                                       s1.setBackgroundColor(Color.WHITE);
                                                                                       Log.d("MY_TAG", "Result Added");
                                                                                       nfcArrayBoolean = false;
                                                                                   } else {
                                                                                       Log.d("MY_TAG", "Result not added !");
                                                                                       Toast.makeText(StartActivity.this, "DB not affected!", Toast.LENGTH_LONG).show();
                                                                                   }

                                                                                   if (bgNumber == 0) {
                                                                                      // startActivity(new Intent(StartActivity.this, StartActivity.class));
                                                                                       System.exit(1);
                                                                                   }
                                                                               }
                                                                           }

                                                                           // Toast.makeText(StartActivity.this, max + "", Toast.LENGTH_LONG).show();
                                                                           max = count;
                                                                           //Toast.makeText(StartActivity.this, "Minimum Value", Toast.LENGTH_LONG).show();

                                                                       }

                                                                       tvResult.setTextColor(Color.RED);
                                                                       coordinatorLayout.setBackgroundResource(R.drawable.backgru);


                                                                   }




                                                               } catch (Exception e) {
                                                                   // connectionStatus.setText(e.toString());
                                                                   count=0;
                                                               }






                                                           }


                                                       }


                                                       @Override
                                                       public void onDeviceConnected(BluetoothDevice device) {
                                                           //a device is connected so you can now send stuff to it
                                                           Toast.makeText(StartActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                                                           //tvResult.setText("Connected");
                                                           // Log.w("CONNECT", "Device connected");

                                                       }

                                                       @Override
                                                       public void onDeviceDisconnected(BluetoothDevice device) {


                                                           // device was disconnected so connect it again?

                                                           Toast.makeText(StartActivity.this, "Disconnected!", Toast.LENGTH_SHORT).show();
                                                           //tvResult.setText("Disconnected");
                                                           Log.d("MY_TAG", "Device disconnected");

                                                           startActivity(new Intent(StartActivity.this, StartActivity.class));

                                                           System.exit(0);
                                                       }

                                                       @Override
                                                       public void onDiscoveryStarted() {
                                                           // coordinatorLayout.setBackgroundResource(R.drawable.bluetoothoff);
                                                       }

                                                       @Override
                                                       public void onDiscoveryFinished() {
                                                           //coordinatorLayout.setBackgroundResource(R.drawable.backgru);
                                                       }
                                                   }

        );


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_REQUEST || requestCode == CHOOSE_SERVER_REQUEST) {

            if (resultCode == RESULT_OK) {

                macAddress = data.getStringExtra(DeviceDialog.DEVICE_DIALOG_DEVICE_ADDRESS_EXTRA);

                if (pref.getMacAddress().contentEquals("")) {
                    pref.setMacAddress(macAddress);
                }

                if (requestCode == SCAN_REQUEST) {

                    simpleBluetooth.connectToBluetoothDevice(macAddress);

                } else {
                    simpleBluetooth.connectToBluetoothServer(macAddress);
                }

            }
        }


    }


    public void scanResult() {
        simpleBluetooth.scan(SCAN_REQUEST);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_scan) {
            simpleBluetooth.scan(SCAN_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public String reverse(String input) {
        char[] in = input.toCharArray();
        int begin = 0;
        int end = in.length - 1;
        char temp;
        while (end > begin) {
            temp = in[begin];
            in[begin] = in[end];
            in[end] = temp;
            end--;
            begin++;
        }
        return new String(in);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupForegroundDispatch(StartActivity.this, mNfcAdapter);
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter  The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //simpleBluetooth.endSimpleBluetooth();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 10000);


            //Toast.makeText(StartActivity.this, bgNumber + " ", Toast.LENGTH_LONG).show();
            mTextView1.setText("Remaining : " + bgNumber + "");
        }
    };

    Runnable soundRunnable=new Runnable() {
        @Override
        public void run() {
            soundHandler.removeCallbacks(soundRunnable);
            soundHandler.postDelayed(soundRunnable, 500);


            mp = MediaPlayer.create(getApplicationContext(), R.raw.testings);
            mp.setLooping(true);

            mp.setVolume(am.getStreamMaxVolume(AudioManager.STREAM_RING),am.getStreamMaxVolume(AudioManager.STREAM_RING));

            if(count>23){
                mp.start();
            }else if(mp.isPlaying()){
               mp.release();

            }


        }
    };





    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // if(keyCode==KeyEvent.KEYCODE_BACK)


        return false;
        // Disable back button..............
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /*
		 * This method gets called, when a new Intent gets associated with the current activity instance.
		 * Instead of creating a new activity, onNewIntent will be called. For more information have a look
		 * at the documentation.
		 *
		 * In our case this method gets called, when the user attaches a Tag to the device.
		 */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
			/*
			 * See NFC forum specification for "Text Record Type Definition" at 3.2.1
			 *
			 * http://www.nfc-forum.org/specs/
			 *
			 * bit_7 defines encoding
			 * bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding;

            if ((payload[0] & 128) == 0)
                textEncoding = "UTF-8";
            else
                textEncoding = "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                mTextView.setText("Detected ");
                mTextView1.setText("Remaining : " + bgNumber + "");

                LinearLayout s=(LinearLayout)findViewById(R.id.liLayout);
                s.setBackgroundColor(Color.GREEN);
                mTextView1.setTextColor(Color.BLACK);
                // Intent intentBL=new Intent(StartActivity.this,StartActivity.class);
                // intentBL.putExtra("nfc", result);


                if (!nfcArray.contains(result)) {


                        nfcArray.add(result);
                        nfcArrayBoolean = true;
                        nfcId = result;

                } else {
                    mTextView.setText("Already exist !");
                    mTextView1.setTextColor(Color.WHITE);

                    s.setBackgroundColor(Color.RED);
                }


                //startActivity(intentBL);
                // StartActivity.this.finish();

                //  Toast.makeText(StartActivity.this,result,Toast.LENGTH_LONG).show();
            }
        }
    }

}
