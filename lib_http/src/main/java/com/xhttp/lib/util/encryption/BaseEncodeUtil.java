package com.xhttp.lib.util.encryption;

import android.os.Handler;
import android.os.Message;

import com.xhttp.lib.util.decoder.BASE64Decoder;
import com.xhttp.lib.util.decoder.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class BaseEncodeUtil
{
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public static void main(String[] arg0){
        System.out.println(ooEncode("{\"memberId\":100}"));
    }
    static String key = "",iv = "";
    public static void init(String keys, String ivs){
        key = keys;
        iv = ivs;
    }
    public static String ooEncode(String str, String key, String iv)
        throws Exception
    {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, ivs);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(cipher.doFinal(str.getBytes("UTF-8")));
    }

    public static String ooEncode(String strs){
        String str = "";
        try
        {
            str = ooEncode(strs, key, iv);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }

    public static String ooDecode(String str, String key, String iv)
        throws Exception
    {
        Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
        IvParameterSpec ivs = new IvParameterSpec(iv.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey securekey = keyFactory.generateSecret(dks);
        cipher.init(Cipher.DECRYPT_MODE, securekey, ivs);
        BASE64Decoder base64Decoder = new BASE64Decoder();
        return new  String(cipher.doFinal(base64Decoder.decodeBuffer(str)),"UTF-8");
    }

}
