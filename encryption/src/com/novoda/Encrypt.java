package com.novoda;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Encrypt extends Activity {

	private static final String SECRET_PASSWORD_TO_ENCRYPT = "secretPassword";
	private static final String TAG = Encrypt.class.getSimpleName();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String hash = getAsHash(SECRET_PASSWORD_TO_ENCRYPT);
        
        ((TextView)findViewById(R.id.txt_encrypted)).setText(hash);
        ((TextView)findViewById(R.id.txt_unencrypted)).setText(getUnhashed(hash));
    }
    
    public String getAsHash(String var) {
        String passwordHashed;
        try {
            passwordHashed = crypt(Cipher.ENCRYPT_MODE, var);
        } catch (Exception e) {
        	Log.e(TAG, "Problem encrypting string" ,e);
            passwordHashed = "you should not see this";
		}
    	return passwordHashed;
    }
    
    public String getUnhashed(String hashed){
        try {
        	hashed = crypt(Cipher.DECRYPT_MODE, hashed);
        } catch (Exception e) {
        	Log.e(TAG, "Problem decrypting string" ,e);
        }     
        return hashed;
    }
    
    
    public String crypt(int mode, String subject) throws Base64DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,  InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException,  BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException {
        byte[] salt = {
        		(byte) 0x03, (byte) 0x64
        };
        
        final String cipherType = "PBEWithMD5AndDES/CBC/PKCS5Padding";
        final String secretKey = "some_string";
        final String algorithm = "PBEWithMD5AndDES";
        final String charset = "UTF-8";

        final PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);
		final SecretKeyFactory kf = SecretKeyFactory.getInstance(algorithm);
		final SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(secretKey.toCharArray()));
        final Cipher crypter = Cipher.getInstance(cipherType);
        
        String result;
        
		switch(mode){
	        case Cipher.DECRYPT_MODE:
	        	crypter.init(Cipher.DECRYPT_MODE, k, ps);
	            result = new String(crypter.doFinal(Base64.decode(subject)), charset);
	        	break;
	        case Cipher.ENCRYPT_MODE:
	        default:
	        	crypter.init(Cipher.ENCRYPT_MODE, k, ps);
	            result = Base64.encode(crypter.doFinal(subject.getBytes(charset)));
        }
        
        return result;
    }
}