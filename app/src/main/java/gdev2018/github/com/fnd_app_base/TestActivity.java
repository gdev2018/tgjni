package gdev2018.github.com.fnd_app_base;

import android.os.Bundle;
import android.app.Activity;

import android.widget.TextView;


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




    }

    public long getCurrentTimeMillis2() {
        return native_getCurrentTimeMillis2(10);
    }

    public static native int native_isTestBackend(int currentAccount);
    //public static native long native_getCurrentTimeMillis2(int test);

}
