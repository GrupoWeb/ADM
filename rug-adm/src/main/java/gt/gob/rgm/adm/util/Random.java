package gt.gob.rgm.adm.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Random {
	private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
	 public static int generateRandom(int limit)
	    {
	        SecureRandom sr = null;
	        int aa = 0;
	        try
	        {
	            sr = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM);
	            aa = sr.nextInt(limit);
	        }catch(NoSuchAlgorithmException nsae)
	        {
	            
	        }
	        return aa;
	    }
}
