package wl.SecureBase;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import wl.SecureBase.MainActivity;
import wl.SecureBase.R;


public class DisplayStack extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stack);
        TextView stackView = (TextView)findViewById(R.id.stack);
        FileInputStream fis = null;





    }
}