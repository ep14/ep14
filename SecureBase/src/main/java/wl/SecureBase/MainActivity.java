package wl.SecureBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import wl.SecureModule.AlgoPerso;
import wl.SecureModule.CipherAlgo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import wl.SecureModule.Shamir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;


/**
 * Created by huang and slavnic on 29/10/14.
 */

public class MainActivity  extends Activity implements View.OnClickListener {
    private Button _info= null,_ok=null,_delete=null,_clearBase=null;
    private EditText _key,_data,_deleteKey;
    private DataBase _db;
    private CipherAlgo _cipher;
    private SecureRandom _prng;
    private byte[] _IV;
    private StackTraceElement[] _st;
    public static FileOutputStream fos;
    public static String stackIn = "stackIn";

    public static final String VISIT = "visit";
    public static String encryptTrace="";
    public static String decryptTrace="";

    // Var for file
    private File _file = null;
    private File _dir  = null;
    private FileOutputStream _fout = null;
    private String testkey = "ENSICAENENSICAEN";
    private FileInputStream _fin = null;


    //Phone's Data and generate the random for the RandTab
    public static PhoneData phoneData;

    //Randtab length
    public static int LEN = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(VISIT, 0);
        boolean visited = settings.getBoolean("visited", false);

        setContentView(R.layout.activity_main);
        _info=(Button)findViewById(R.id.buttonInfo);
        _info.setOnClickListener(this);
        _ok=(Button)findViewById(R.id.buttonOk);
        _ok.setOnClickListener(this);
        _delete=(Button)findViewById(R.id.buttonDelete);
        _delete.setOnClickListener(this);
        _clearBase=(Button)findViewById(R.id.buttonClearBase);
        _clearBase.setOnClickListener(this);

        _key =(EditText)findViewById(R.id.textKey);
        _data =(EditText)findViewById(R.id.textData);
        _deleteKey =(EditText)findViewById(R.id.textDelete);
        _db = new DataBase(this);
        _db.open();

        Shamir shamir = new Shamir();
        shamir.split();//creation of 3 secret

        //Get data from phone
        TelephonyManager tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        phoneData = new PhoneData(tm);

        _cipher=new CipherAlgo();

        try {
            _prng = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        _IV = new byte[16];

        if(!visited){

            try {
                _cipher.encrypt();
                _db.insertData();
                fos = openFileOutput(stackIn, Context.MODE_PRIVATE);
                fos.write(encryptTrace.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }



            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("visited", true);
            editor.commit();
        }

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.buttonOk:
                encryptTrace="";
                try {
                    testEncryption();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.buttonInfo:
                Intent intent = new Intent(MainActivity.this, DisplayInfo.class);
                startActivity(intent);
                break;

            case R.id.buttonClearBase:
                //_db.clearBase();
                Intent intent1 = new Intent(MainActivity.this, DisplayStack.class);
                startActivity(intent1);
                break;

            /*case R.id.buttonDelete:
                _db.deleteDataByKey(_deleteKey.getText().toString());
                _deleteKey.setText("");
                break;
                */

        }
    }

    @Override
    public void onStop(){
        super.onStop();
        _db.close();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        _db.open();
    }




    //////////////////TEST ZONE////////////////////////
    private void add() throws Exception{
        _prng.nextBytes(_IV);
        String plainText = _key.getText().toString();
        byte[] encKey = _cipher.encrypt(plainText,_IV);
        byte[] encData =_cipher.encrypt(_data.getText().toString(),_IV);
        _key.setText("");
        _data.setText("");
        Data data = new Data(_cipher.toBinary(encKey),_cipher.toBinary(encData),_IV);
        _db.insertData(data);

    }
    private void testEncryption(){

        _prng.nextBytes(_IV);
        try {
            FileInputStream fis = openFileInput(stackIn);
            StringBuilder builder = new StringBuilder();

            int ch=0;
            while((ch=fis.read())!=-1){
                builder.append((char)ch);
            }
            String s = builder.toString();
            s=s.toString();

            byte[] encKey = _cipher.encrypt(_key.getText().toString(),_IV);
            byte[] encData =_cipher.encrypt(_data.getText().toString(),_IV);
            _key.setText("");
            _data.setText("");
            Data data = new Data(_cipher.toBinary(encKey),_cipher.toBinary(encData),_IV);
            _db.insertData(data);
            Data d = _db.getDataByKey(_cipher.toBinary(encKey));

            if(s.equals(encryptTrace)){
                _key.setText("ok");
            }else{
                _key.setText("fail");
            }

            String decData=_cipher.decrypt(_cipher.fromBinary(d.getData()), d.getIV());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void testShamir() {
        Random rnd = new Random();
        BigInteger SecretEnsi = new BigInteger(testkey.getBytes());// Ascii

        BigInteger Secret = new BigInteger(128, rnd);


        Shamir shamir = new Shamir(SecretEnsi);
        shamir.split(SecretEnsi);
        BigInteger sommecoeff = shamir.combine(shamir.get_coeff());

        System.out.println("Secret =" + SecretEnsi + " et Shamir = " + sommecoeff);

    }

}
