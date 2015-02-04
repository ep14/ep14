package wl.SecureBase;

import java.math.BigInteger;

/**
 * Created by huang and slavnic  on 29/10/14.
 */

public class Data {
    //Shamir
    private static String string1 = "ENSICAENENSICAEN";//128bits
    public static BigInteger key1 = new BigInteger(string1.getBytes());
    public static BigInteger secret1;

    //AlgoPerso
    public static BigInteger _RandTab1[] = new BigInteger[MainActivity.LEN];

    private String _key;
    private String _data;
    private byte[] _IV;

    public Data(String key,String data,byte[] IV){
        _key=key;
        _data=data;
        _IV = IV;
    }
    public Data(String key,String data){
        _key=key;
        _data=data;
        _IV = null;
    }

    public String getKey() {
        return _key;
    }

    public String getData() {
        return _data;
    }

    public byte[] getIV(){
        return _IV;
    }

    //One tab of random value of 127bits
    public static BigInteger[] RandTab1(){

        _RandTab1[0] = new BigInteger("108032456488899972180431149587608123302");
        _RandTab1[1] = new BigInteger("19054583636489366671143534741405752789");
        _RandTab1[2] = new BigInteger("66871533872931397200459047357855854947");
        _RandTab1[3] = new BigInteger("130944495992277729821992138879538313097");
        _RandTab1[4] = new BigInteger("140409784754565445566638353919327523475");
        _RandTab1[5] = new BigInteger("136503799047927826807453300683473808935");

        return _RandTab1;
    }
}