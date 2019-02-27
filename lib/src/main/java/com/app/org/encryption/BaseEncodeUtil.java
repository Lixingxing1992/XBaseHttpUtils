package com.app.org.encryption;

import android.os.Handler;
import android.os.Message;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class BaseEncodeUtil
{
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    public static void main(String[] arg0){

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
            str = ooEncode(strs, "6D4267DF81D83449D851617E", "78549912");
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
