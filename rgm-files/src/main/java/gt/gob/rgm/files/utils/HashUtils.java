package gt.gob.rgm.files.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
	public static String hashString(String str) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest mdigest = MessageDigest.getInstance("SHA-256");
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
