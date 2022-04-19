package util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256 {
	private static final String DEFAULT_KEY = "H0w@rdVVOrIDsl07YePotaiIWiOdcrtM";
	private String ips;
	private Key keySpec;

	private static AES256 instance;

	protected AES256(String key) {
		try {
			byte[] keyBytes = new byte[16];
			byte[] b = key.getBytes("UTF-8");
			System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
			this.ips = key.substring(0, 16);
			this.keySpec = keySpec;
			instance = this;
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	public static AES256 getDefault() {
		return instance;
	}

	public String encrypt(String str) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec,
					new IvParameterSpec(ips.getBytes("UTF-8")));
			byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));
			String Str = new String(Base64Coder.encode(encrypted));
			return Str;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException e) {
		}
		return null;
	}

	public String decrypt(String str) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, keySpec,
					new IvParameterSpec(ips.getBytes("UTF-8")));

			byte[] byteStr = Base64Coder.decode(new String(str.getBytes()));
			String Str = new String(cipher.doFinal(byteStr), "UTF-8");
			return Str;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException e) {
		}
		return null;
	}

	public static String encryptByDefaultKey(String str){
		return new AES256(DEFAULT_KEY).encrypt(str);
	}
	public static String decryptByDefaultKey(String str){
		return new AES256(DEFAULT_KEY).decrypt(str);
	}
}
