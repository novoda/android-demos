package com.novoda.demo.encryption;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.novoda.R;

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

public class EncryptionActivity extends Activity {

	private static final String SECRET_DATA_TO_ENCRYPT = "secretPassword";
	private static final String TAG = "Encrypt";
    private static final String PREFERENCE_KEY_SECURED_DATA = "KEY_SECURED_DATA";

    final String CIPHER_TYPE = "PBEWithMD5AndDES/CBC/PKCS5Padding";
    final String ALGORITHM = "PBEWithMD5AndDES";
    final String CHARSET = "UTF-8";

    /*
     * Change string for your app. How you secure this key is up to yourself.
     * I'm all ears as too suggestions for best practices in securing this key within the app programatically.
     */
    final String SECRET_KEY = "some_string";
    
    final byte[] SALT = {
    		(byte) 0x03, (byte) 0x64
    };
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        updateTextViews();
    }

    private void updateTextViews() {
        String hash = PreferenceManager.getDefaultSharedPreferences(this).getString(PREFERENCE_KEY_SECURED_DATA, "Click the save button to store the password in preferences");

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
    
    
    public String crypt(int mode, String encryption_subject) throws Base64DecoderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,  InvalidAlgorithmParameterException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException,  BadPaddingException, UnsupportedEncodingException, IllegalBlockSizeException {

        final PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(SALT, 20);
		final SecretKeyFactory kf = SecretKeyFactory.getInstance(ALGORITHM);
		final SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(SECRET_KEY.toCharArray()));
        final Cipher crypter = Cipher.getInstance(CIPHER_TYPE);
        
        String result;
        
		switch(mode){
	        case Cipher.DECRYPT_MODE:
	        	crypter.init(Cipher.DECRYPT_MODE, k, ps);
	            result = new String(crypter.doFinal(Base64.decode(encryption_subject)), CHARSET);
	        	break;
	        case Cipher.ENCRYPT_MODE:
	        default:
	        	crypter.init(Cipher.ENCRYPT_MODE, k, ps);
	            result = Base64.encode(crypter.doFinal(encryption_subject.getBytes(CHARSET)));
        }
        
        return result;
    }

    public void onSaveClick(View view){
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString(PREFERENCE_KEY_SECURED_DATA, getAsHash(SECRET_DATA_TO_ENCRYPT)).commit();
        updateTextViews();
    }

    public void onDeleteClick(View view) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.clear().commit();
        updateTextViews();
    }
}