package EInvoice;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.*;
import javax.crypto.Cipher;
public abstract class RSACoder {
	public static final String KEY_ALGORITHM = "RSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	private static final int KEY_SIZE = 512;
	/** 
	 * Privacy Key Decryption
	 * @param data
	 * @param key
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception {
		// Get Private Key
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// Generate Private Key
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// Decrypt Data
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	/**
	 * Public Key Decryption
	 * @param data
	 * @param key
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception {
		// Get Public Key
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// Generate Public Key
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		// Decrypt Data
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE,publicKey);
		return cipher.doFinal(data);
	}	
	/** 
	 * Privacy Key Encryption
	 * @param data
	 * @param key
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception {
		// Get Private Key
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// Generate Private Key
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// Decrypt Data
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}
	/**
	 * Public Key Encryption
	 * @param data
	 * @param key
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception {
		// Get Public Key
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// Generate Public Key
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		// Decrypt Data
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE,publicKey);
		return cipher.doFinal(data);
	}
	/**
	 * Get Private Key
	 * @param keyMap
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getPrivateKey(Map<String,Object> keyMap) throws Exception {
		Key key = (Key)keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}
	/**
	 * Get Public Key
	 * @param keyMap
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] getPublicKey(Map<String,Object> keyMap) throws Exception {
		Key key = (Key)keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}
	public static Map<String,Object>initKey()throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String,Object> keyMap = new HashMap<String,Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);		
		return keyMap;
	}
	
	
	
	
	
}