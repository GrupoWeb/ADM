package gt.gob.rgm.adm.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;

public class CryptoUtils {
	private static final int ITERATIONS = 10000;
	private static final int LENGTH = 512;
	
	public static String hash(String original, String salt) {
		char[] originalChars = original.toCharArray();
		byte[] saltBytes = salt.getBytes();
		
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(originalChars, saltBytes, ITERATIONS, LENGTH);
			SecretKey key = skf.generateSecret(spec);
			byte[] res = key.getEncoded();
			
			return Hex.encodeHexString(res);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return new StringBuffer(original + salt).reverse().toString();
	}
	
	public static String hashAlgorithm(String str, String algorithm) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest mdigest = MessageDigest.getInstance(algorithm);
			try {
				mdigest.update(str.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				mdigest.update(str.getBytes());
			}
			byte[] digest = mdigest.digest();
			for(int i = 0; i < digest.length; i++) {
				sb.append(String.format("%02x", digest[i]));
			}
		} catch (NoSuchAlgorithmException e) {
			sb.append(str);
			sb = sb.reverse();
		}
		
		return sb.toString();
	}
}
