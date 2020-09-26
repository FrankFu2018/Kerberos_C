import java.io.Serializable;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesSer implements Serializable{
	public byte[] encryptByDES(byte[] bytP,byte[] bytKey) throws Exception{//º”√‹
		DESKeySpec desKS = new DESKeySpec(bytKey); 
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES"); 
		SecretKey sk = skf.generateSecret(desKS); 
		Cipher cip = Cipher.getInstance("DES"); 
		cip.init(Cipher.ENCRYPT_MODE,sk); 
		byte[] nbyte= cip.doFinal(bytP); 
		return nbyte;
	} 
	public byte[] decryptByDES(byte[] bytE,byte[] bytKey) throws Exception{//Ω‚√‹
		DESKeySpec desKS = new DESKeySpec(bytKey); 
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES"); 
		SecretKey sk = skf.generateSecret(desKS); 
		Cipher cip = Cipher.getInstance("DES"); 
		cip.init(Cipher.DECRYPT_MODE,sk); 
		byte[] dbyte= cip.doFinal(bytE); 
		return dbyte;
	} 
}