package gdev2018.github.com.fnd_app_base;

import android.os.Bundle;
import android.app.Activity;

import android.widget.TextView;


import static com.github.gdev2018.master.AndroidUtilities.secondsToString;


public class TestActivity extends Activity {

    private String seconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        seconds = secondsToString(123);


        ((TextView)findViewById(R.id.seconds_textView)).setText(seconds);




    }

    public static native int native_isTestBackend(int currentAccount);

}
