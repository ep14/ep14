package wl.SecureModule;

import android.telephony.TelephonyManager;
import wl.SecureBase.*;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by huang and slavnic on 14/01/15.
 */
public class AlgoPerso {
    private BigInteger _PersoKey;
    private BigInteger _Key;
    private BigInteger _MasterKey;
    private BigInteger[] _DataTab;
    private int[] _keyRand;


    public AlgoPerso(){
        _PersoKey = BigInteger.ZERO;
        _Key = BigInteger.ZERO;
        _DataTab = MainActivity.phoneData.getDataTab();
        for(int i = 0;i<7;i++){
            _PersoKey = _PersoKey.xor(_DataTab[i]);
        }

        System.out.println("PersoKey : "+_PersoKey);
        System.out.println("Bit num PersoKey : "+ _PersoKey.bitLength());
        _keyRand = PhoneData.keyRand;

        //Chose only 2 Random value for each randtab
        _Key = _Key.xor(Data.RandTab1()[_keyRand[0]%MainActivity.LEN]);
        _Key = _Key.xor(Data.RandTab1()[_keyRand[1]%MainActivity.LEN]);


        _Key = _Key.xor(DataBase.RandTab2()[_keyRand[2]%MainActivity.LEN]);
        _Key = _Key.xor(DataBase.RandTab2()[_keyRand[3]%MainActivity.LEN]);


        _Key = _Key.xor(DisplayInfo.RandTab3()[_keyRand[4]%MainActivity.LEN]);
        _Key = _Key.xor(DisplayInfo.RandTab3()[_keyRand[5]%MainActivity.LEN]);

        //System.out.println("Key : "+ _Key);
        //System.out.println("Bit num Key : "+ _Key.bitLength());

        //Master Key 127bit
        _MasterKey = _PersoKey.xor(_Key);
        _MasterKey = _MasterKey.shiftRight(_MasterKey.bitLength()-127);
        //System.out.println("MasterKey : "+ _MasterKey);
        //System.out.println("Bit num MasterKey : "+ _MasterKey.bitLength());
    }

    public BigInteger get_MasterKey(){
        return _MasterKey;
    }

    public static BigInteger recoverKey(){
        BigInteger key = BigInteger.ZERO;
        BigInteger persoKey = BigInteger.ZERO;
        BigInteger[] dataTab = MainActivity.phoneData.getDataTab();
        for(int i = 0;i<dataTab.length;i++){
            persoKey = persoKey.xor(dataTab[i]);
        }
        int[] keyRand = PhoneData.keyRand;

        //Chose only 2 Random value for each randtab
        key = key.xor(Data.RandTab1()[keyRand[0]%MainActivity.LEN]);
        key = key.xor(Data.RandTab1()[keyRand[1]%MainActivity.LEN]);


        key = key.xor(DataBase.RandTab2()[keyRand[2]%MainActivity.LEN]);
        key = key.xor(DataBase.RandTab2()[keyRand[3]%MainActivity.LEN]);


        key = key.xor(DisplayInfo.RandTab3()[keyRand[4]%MainActivity.LEN]);
        key = key.xor(DisplayInfo.RandTab3()[keyRand[5]%MainActivity.LEN]);

        key = persoKey.xor(key);
        key = key.shiftRight(key.bitLength()-127);

        return key;

    }
}
