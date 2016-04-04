package app.turn.out.outturn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DbBackup {
		
	public static void copyDatabase(Context c) {
		
		String DATABASE_NAME = "Outturn.db";
		
		String databasePath = c.getDatabasePath(DATABASE_NAME).getPath();
		File f = new File(databasePath);
		OutputStream myOutput = null;
		OutputStream myOutputInternal = null;
		InputStream myInput = null;
		Log.d("testing", " lots db path " + databasePath);
		Log.d("testing", " lots db exist " + f.exists());


		if (f.exists()) {
			try {

			//File directory = new File("/storage/sdcard1/Employee_DB_BKPS/");
				File directoryIntenal = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EmployeeAA/");
				
//				if (!directory.exists())
//					directory.mkdir();

				if (!directoryIntenal.exists())
					directoryIntenal.mkdir();

//				myOutput = new FileOutputStream(directory.getAbsolutePath()
//						+ "/" + DATABASE_NAME);
				myOutputInternal = new FileOutputStream(directoryIntenal.getAbsolutePath()
						+ "/" + DATABASE_NAME);
				
				myInput = new FileInputStream(databasePath);
				

				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInput.read(buffer)) > 0) {
//					myOutput.write(buffer, 0, length);
					myOutputInternal.write(buffer, 0, length);
				}

//				myOutput.flush();
				myOutputInternal.flush();

				              //  Toast.makeText(c, "DB Backed Up!", Toast.LENGTH_LONG).show();
			} catch (Exception e) {

				Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();
			} finally {
				try {
//					if (myOutput != null) {
//						myOutput.close();
//						myOutput = null;
//					}
					if (myOutputInternal != null) {
						myOutputInternal.close();
						myOutputInternal = null;
					}
					if (myInput != null) {
						myInput.close();
						myInput = null;
					}
				} catch (Exception e) {
					Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		}
	}
}
