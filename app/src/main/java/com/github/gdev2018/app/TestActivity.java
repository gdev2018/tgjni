package com.github.gdev2018.app;

import android.os.Bundle;
import android.app.Activity;

import android.widget.TextView;


import com.github.gdev2018.master.AndroidUtilities;
import com.github.gdev2018.master.FileLog;
import com.github.gdev2018.master.SQLite.SQLiteCursor;

import java.util.Locale;

import com.github.gdev2018.app.R;
import com.github.gdev2018.master.Utilities;

import static com.github.gdev2018.master.AndroidUtilities.secondsToString;
import static com.github.gdev2018.master.tgnet.ConnectionsManager.native_getCurrentTimeMillis2;


public class TestActivity extends Activity {

    private String seconds;
    private String newText;


    private Runnable setTextViewRunnable = new Runnable() {
        @Override
        public void run() {
            ((TextView)findViewById(R.id.seconds_textView)).setText(newText);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        seconds = secondsToString(123);

        seconds = Long.toString(getCurrentTimeMillis2());
        ((TextView)findViewById(R.id.seconds_textView)).setText(seconds);

        // needs this alone row to previous create db
        LocalSQLiteOpenHelper.getInstance(0);

        LocalSQLiteOpenHelper.getInstance(0).getStorageQueue().postRunnable(() -> testSelect());
    }

    public void testSelect() {
        SQLiteCursor cursor = null;
        try {

//            String sql = "SELECT s_Step, rot13(s_Step), hex(md5(s_Step)) FROM v_Steps";
            String sql = "SELECT s_Step, rot13(s_Step), md5long('dxsdsd') FROM v_Steps";
//                cursor = database.queryFinalized(String.format(Locale.US, sql, offset, count));
            cursor = LocalSQLiteOpenHelper.getInstance(0).getDatabase().queryFinalized(String.format(Locale.US, sql, 0, 10));
            while (cursor.next()) {
                String s_Step = cursor.stringValue(0);
                String rot13 = cursor.stringValue(1);
                String md5 = cursor.stringValue(2);

//                ((TextView)findViewById(R.id.seconds_textView)).setText();
                newText = s_Step + " / " + rot13 + " / " + md5 + " / " + Utilities.random.nextInt(1000);

                // works!
                Thread.sleep(1000); // = LocalSQLiteOpenHelper.getInstance(0).getStorageQueue().sleep(1000);
                AndroidUtilities.runOnUIThread(setTextViewRunnable);
                
                // // TODO: 2019-08-28 next. test delay > sleep
                

                // Thread experiments
                // https://stackoverflow.com/questions/29731163/refresh-textview-in-android
                // https://ru.stackoverflow.com/questions/338436/%D0%98%D1%81%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B8%D0%B5-%D0%BC%D0%B5%D1%82%D0%BE%D0%B4%D0%B0-sleep

//                // delay 500 = hide step 1
//                Thread.sleep(1000);
//                AndroidUtilities.runOnUIThread(setTextViewRunnable, 500);

//                // delay - is not working for sleep
//                AndroidUtilities.runOnUIThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // update TextView here!
//                        ((TextView)findViewById(R.id.seconds_textView)).setText(newText);
//                    }
//                }, 1000);

//                // works!
//                Thread.sleep(1000);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // update TextView here!
//                        ((TextView)findViewById(R.id.seconds_textView)).setText(newText);
//                    }
//                });

            }
            cursor.dispose();

        } catch (Exception e) {
            FileLog.e(e);

        } finally {
            if (cursor != null) {
                cursor.dispose();
            }
        }
    }

    public long getCurrentTimeMillis2() {
        return native_getCurrentTimeMillis2(10);
    }

//    public static native int native_isTestBackend(int currentAccount);
    //public static native long native_getCurrentTimeMillis2(int test);

}
