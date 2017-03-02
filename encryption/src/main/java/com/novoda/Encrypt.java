package com.novoda;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Encrypt extends Activity {

	private static final String SECRET_PASSWORD_TO_ENCRYPT = "secretPassword";
	private static final String TAG = "Encrypt";
	
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
        
        String hash = transform(SECRET_PASSWORD_TO_ENCRYPT, Cipher.ENCRYPT_MODE);
        
        ((TextView)findViewById(R.id.txt_encrypted)).setText(hash);
        ((TextView)findViewById(R.id.txt_unencrypted)).setText(transform(hash, Cipher.DECRYPT_MODE));
    }

    String transform(String var, int transformationType) {
        String passwordHashed = "";
        try {
            passwordHashed = crypt(transformationType, var);
        } catch (CryptException e) {
            if (transformationType == Cipher.ENCRYPT_MODE) {
                Log.e(TAG, "A problem during the encryption has occurred", e);
                passwordHashed = "";
            }
            if (transformationType == Cipher.DECRYPT_MODE) {
                Log.e(TAG, "A problem during the decryption has occurred:", e);
            }
        }
        return passwordHashed;
    }

    public String crypt(int mode, String encryption_subject) throws CryptException {

        final PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(SALT, 20);
		final SecretKeyFactory kf = getSecretKeyFactory();
		final SecretKey k = getSecretKey(kf);
        final Cipher crypter = getCipherInstance();
        
        String result;
        
		switch(mode){
	        case Cipher.DECRYPT_MODE:
                initialise(ps, k, crypter, Cipher.DECRYPT_MODE);
                result = getString(encryption_subject, crypter);
	        	break;
	        case Cipher.ENCRYPT_MODE:
	        default:
                initialise(ps, k, crypter, Cipher.ENCRYPT_MODE);
                result = encode(encryption_subject, crypter);
        }
        
        return result;
    }

    private SecretKeyFactory getSecretKeyFactory() throws CryptException {

        try {
            return SecretKeyFactory.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptException(e);
        }
    }

    private SecretKey getSecretKey(SecretKeyFactory kf) throws CryptException {

        try {
            return kf.generateSecret(new javax.crypto.spec.PBEKeySpec(SECRET_KEY.toCharArray()));
        } catch (InvalidKeySpecException e) {
            throw new CryptException(e);
        }
    }

    private Cipher getCipherInstance() throws CryptException {

        try {
            return Cipher.getInstance(CIPHER_TYPE);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new CryptException(e);
        }
    }

    private void initialise(PBEParameterSpec ps, SecretKey k, Cipher crypter, int decryptMode) throws CryptException {
        try {
            crypter.init(decryptMode, k, ps);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new CryptException(e);
        }
    }

    private String getString(String encryption_subject, Cipher crypter) throws CryptException {


        try {
            return new String(finishTransformation(crypter, getDecode(encryption_subject)), CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new CryptException(e);
        }
    }

    private String encode(String encryption_subject, Cipher crypter) throws CryptException {

        try {
            return Base64.encode(finishTransformation(crypter, encryption_subject.getBytes(CHARSET)));
        } catch (UnsupportedEncodingException e) {
            throw new CryptException(e);
        }

    }

    private byte[] finishTransformation(Cipher crypter, byte[] bytes) throws CryptException {

        try {
            return crypter.doFinal(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new CryptException(e);
        }
    }

    private byte[] getDecode(String encryption_subject) throws CryptException {

        try {
            return Base64.decode(encryption_subject);
        } catch (Base64DecoderException e) {
            throw new CryptException(e);
        }
    }
}
