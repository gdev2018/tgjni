package com.github.gdev2018.app;

import android.os.Bundle;
import android.app.Activity;

import android.widget.TextView;


import com.github.gdev2018.master.FileLog;
import com.github.gdev2018.master.SQLite.SQLiteCursor;

import java.util.Locale;

import com.github.gdev2018.app.R;

import static com.github.gdev2018.master.AndroidUtilities.secondsToString;
import static com.github.gdev2018.master.tgnet.ConnectionsManager.native_getCurrentTimeMillis2;


public class TestActivity extends Activity {

    private String seconds;

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

            String sql = "SELECT s_Step, rot13(s_Step) FROM t_Steps";
//            String sql = "SELECT s_Step, rot13(s_Step), md5(s_Step) FROM t_Steps";
//                cursor = database.queryFinalized(String.format(Locale.US, sql, offset, count));
            cursor = LocalSQLiteOpenHelper.getInstance(0).getDatabase().queryFinalized(String.format(Locale.US, sql, 0, 10));
            while (cursor.next()) {
                String s_Step = cursor.stringValue(0);
                String rot13 = cursor.stringValue(1);
                String md5 = cursor.stringValue(1);

                ((TextView)findViewById(R.id.seconds_textView)).setText(s_Step + rot13 + md5);
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
