package wl.SecureBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by huang and slavnic on 29/10/14.
 */
public class DisplayInfo extends Activity implements OnClickListener{
    //Shamir
    private static String string3 = "DISPLAYINFODISPL";//128bits
    public static BigInteger key3 = new BigInteger(string3.getBytes());
    public static BigInteger secret3;

    //AlgoPerso
    private static BigInteger _RandTab3[] = new BigInteger[MainActivity.LEN];

    private Button _back;
    private DataBase _db;
    private ArrayList<Data> _listData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayinfo);
        _back=(Button)findViewById(R.id.back);
        _back.setOnClickListener(this);
        _db=new DataBase(this);
        _db.open();



        _listData=_db.getListData();
        final ArrayList<String> list = new ArrayList<String>();
        final ListView listview = (ListView) findViewById(R.id.listView1);

        Data d;
        for ( int i=0;i<_listData.size();++i){
            d=_listData.get(i);
            list.add("KEY: "+d.getKey()+" \n DATA:"+d.getData());
        }

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DisplayInfo.this, MainActivity.class);
        startActivity(intent);

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    //One tab of random value of 127bits
    public static BigInteger[] RandTab3(){
        _RandTab3[0] = new BigInteger("119659494552542394156913479340906595046");
        _RandTab3[1] = new BigInteger("132222689514896232166354195205812483594");
        _RandTab3[2] = new BigInteger("3609850153946652680769206016662275324");
        _RandTab3[3] = new BigInteger("57157084121028188711891743476030906172");
        _RandTab3[4] = new BigInteger("59181140600422754724118797700839303465");
        _RandTab3[5] = new BigInteger("130521922723277269499618074292321740874");

        return _RandTab3;
    }




}
