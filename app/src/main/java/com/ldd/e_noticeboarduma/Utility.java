package com.ldd.e_noticeboarduma;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Utility {
    private static String AES = "AES";
    public static Bitmap bitImage = null;
    public static String en_pass = "dice";
    public static String mat;

    /**
     * This method check if the password provided by the user contains at least one alphabet between [a-zA-Z]
     * @param s; the password that is to be checked
     * @return returns a boolean value if an alphabet is present or not.
     */
    public static boolean isAlpha( CharSequence s) {
        return Pattern.matches(".*[a-zA-Z]+.*", s);
    }

    /**
     * This method check if the password provided by the user contains at least one number between 0-9
     * @param s; the password that is to be checked
     * @return returns a boolean value if a number is present or not.
     */
    public static boolean containNumber(CharSequence s){
        return Pattern.matches(".*[0-9]+.*",s);
    }

    /**
     * This method check if the password provided by the user contains at least one symbol
     * @param s; the password that is to be checked
     * @return returns a boolean value if a symbol is present or not.
     */
    public static boolean containSymbol(CharSequence s){
        String symb = "[!@#$%&*()_+=|<>?{}\\[\\]~-]";
        Pattern special = Pattern.compile (symb);
        return Pattern.matches(symb, s);

    }

    /**
     * This method is use to encrypt a plain text using AES
     * @param Data ; it take the data (plain text) that is to be encrypted
     * @param password ; it takes the password that is use to encrypt the text
     * @return returns the encrypted text (cipher text)
     * @throws Exception
     */
    public static String encrypt(String Data, String password) throws Exception{
        SecretKeySpec key = generalKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] enVal = cipher.doFinal(Data.getBytes());
        return Base64.encodeToString(enVal, Base64.DEFAULT);
    }

    /**
     * This method is use to decrypt the cipher text to it's original plain text
     * @param Data; the data to be decrypted (cipher text)
     * @param password; the password used to encrypt the data. the decryption will not be successful if the password is wrong
     * @return returns the decrypted text
     * @throws Exception
     */
    public static String decrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generalKey(password);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedVal = Base64.decode(Data, Base64.DEFAULT);
        byte[] decVal = cipher.doFinal(decodedVal);
        String decryptedVal = new String(decVal);
        return decryptedVal;
    }

    /**
     * This method is use to generate a secrete key from the password provided
     * @param password; the password which the generated from
     * @return return the secret key generated using Advanced Encryption Standard (AES)
     * @throws Exception
     */
    private static SecretKeySpec generalKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        return new SecretKeySpec(key, "AES");
    }

    public static Bitmap reduceBitmapSize(Bitmap bitmap,int MAX_SIZE) {
        //For Image Size 640*480, use MAX_SIZE =  307200 as 640*480 307200
        //private static long MAX_SIZE = 360000;
        double ratioSquare;
        int bitmapHeight, bitmapWidth;
        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        ratioSquare = (bitmapHeight * bitmapWidth) / MAX_SIZE;
        if (ratioSquare <= 1) {
            return bitmap;
        }
        double ratio = Math.sqrt(ratioSquare);
        Log.d("mylog", "Ratio: " + ratio);
        int requiredHeight = (int) Math.round(bitmapHeight / ratio);
        int requiredWidth = (int) Math.round(bitmapWidth / ratio);
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true);
    }

    public static void appendLog(String text) {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String msg = "["+date+"] "+"[I] "+text;
        File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data/com.ldd.e_noticeboarduma/files/log.txt");
        Log.e("Path: ", logFile+"");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(msg);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
