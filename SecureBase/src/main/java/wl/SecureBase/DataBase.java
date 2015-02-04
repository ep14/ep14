package wl.SecureBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import wl.SecureModule.CipherAlgo;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by huang and slavnic on 29/10/14.
 */
public class DataBase {
    //Shamir
    private static String string2 = "DATABASEDATABASE";//128bits
    public static BigInteger key2 = new BigInteger(string2.getBytes());
    public static BigInteger secret2;

    //AlgoPerso
    private static BigInteger _RandTab2[] = new BigInteger[MainActivity.LEN];

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "SecureBase.db";

    public static final String TABLE="secureT";

    public static final String COL_IV = "IV";
    public static final String COL_KEY="key";
    public static final String COL_DATA="data";
    public static final String COL_ID="id";


    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_KEY = 1;
    private static final int NUM_COL_DATA = 2;
    private static final int NUM_COL_IV = 3;
    private String separator = "&";

    private SQLiteDatabase _db;

    private SecureBaseHelper _myBaseSQLite;

    /*
    * DataBase creation with the table
    * */
    public DataBase(Context context){
        _myBaseSQLite = new SecureBaseHelper(context, NOM_BDD, null, VERSION_BDD);
    }
    /*
    * Opening db in writing mode
    * */
    public void open(){
        _db = _myBaseSQLite.getWritableDatabase();
    }
    /*
    * Closing db
    * */
    public void close(){
        _db.close();
    }

    public SQLiteDatabase getBDD(){
        return _db;
    }

    public void insertData() throws ClassNotFoundException{

        StackTraceElement[] _st=Thread.currentThread().getStackTrace();
        MainActivity.encryptTrace+=_st[2].getMethodName()+separator;

        for (int i =2;i<4;i++){
            String s = _st[i].getClassName();
            ClassLoader classLoader = CipherAlgo.class.getClassLoader();
            Class c= classLoader.loadClass(s);
            MainActivity.encryptTrace+=c.getSimpleName()+separator;
        }
    }

    public long insertData(Data d) throws ClassNotFoundException{

        StackTraceElement[] _st=Thread.currentThread().getStackTrace();
        MainActivity.encryptTrace+=_st[2].getMethodName()+separator;

        for (int i =2;i<4;i++){
            String s = _st[i].getClassName();
            ClassLoader classLoader = CipherAlgo.class.getClassLoader();
            Class c= classLoader.loadClass(s);
            MainActivity.encryptTrace+=c.getSimpleName()+separator;
        }


        //Creation of ContentValue (work like a HashMap)
        ContentValues values = new ContentValues();
        //Adding a value associate to a key (name of the column where the value is put)
        values.put(COL_KEY, d.getKey());
        values.put(COL_DATA, d.getData());

        values.put(COL_IV,d.getIV());// byte[] in a String not sure if it work
        //insert the object in the db with ContentValues
        Cursor cursor = _db.query(TABLE, new String[] {COL_ID, COL_KEY, COL_DATA,COL_IV}, COL_KEY + "=\"" + d.getKey() +"\" AND "+ COL_DATA +"=\""+d.getData()+"\" AND "+COL_IV+"=\""+d.getIV()+"\"", null, null, null, null);
        if (cursor.getCount() == 0) {
            return _db.insert(TABLE, null, values); //return id
        } else {
            return -1;
        }

    }

    public long deleteDataByKey(String key){
        return _db.delete(TABLE, COL_KEY + "=" + key, null);
    }


    public Data getDataByKey(String key){
        //Get the value ,in a Cursor, corresponding to a client in the db (here it's thanks to his name)
        Cursor c = _db.query(TABLE, new String[] {COL_ID, COL_KEY, COL_DATA,COL_IV}, COL_KEY + "=\"" + key +"\"", null, null, null, null);
        return cursorToData(c);
    }


    private Data cursorToData(Cursor c){
        //if none element was return in the request, return null
        if (c.getCount() == 0)
            return null;
        //else we move on the first element
        c.moveToFirst();
        Data d = null;
        d = new Data(c.getString(NUM_COL_KEY),c.getString(NUM_COL_DATA),c.getBlob(NUM_COL_IV));//Storage as Byte array(Blob)



        //Closing the cursor
        c.close();

        return d;
    }

    public ArrayList<Data> getListData(){
        ArrayList<Data> list=new ArrayList<Data>();
        Cursor c = _db.query(TABLE, new String[] {COL_ID, COL_KEY, COL_DATA,COL_IV},null, null, null, null, null);

        if(c.getCount()==0)
            return list;
        c.moveToFirst();
        Data d = new Data(c.getString(NUM_COL_KEY),c.getString(NUM_COL_DATA));
        list.add(d);

        while(c.moveToNext()){
            Data d1 = new Data(c.getString(NUM_COL_KEY),c.getString(NUM_COL_DATA));
            list.add(d1);
        }

        return list;
    }

    public void clearBase(){
        _db.delete(TABLE,null,null);

    }

    //One tab of random value of 127bits
    public static BigInteger[] RandTab2(){
        _RandTab2[0] = new BigInteger("126655527604051455571944651627827996777");
        _RandTab2[1] = new BigInteger("160965096454640377722201519312729166933");
        _RandTab2[2] = new BigInteger("134069781948300642917888570105633240253");
        _RandTab2[3] = new BigInteger("115335880275994352185882962446617222676");
        _RandTab2[4] = new BigInteger("30618044413294937461748144822856835774");
        _RandTab2[5] = new BigInteger("95824267309214699137609235931183428908");

        return _RandTab2;
    }

}
