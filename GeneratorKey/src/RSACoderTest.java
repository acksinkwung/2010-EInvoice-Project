import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;
public class RSACoderTest {
	private byte[] publicKey;
	private byte[] privateKey;
	@Before
	public void initKey() throws Exception {
		//Map<String,Object> keyMap = RSACoder.initKey();
		
		String strFilePath;
		FileOutputStream fos;
		FileInputStream fis;
		strFilePath = ".//publicKey";
		//publicKey = RSACoder.getPublicKey(keyMap);
		/*
		
		try {
			fos = new FileOutputStream(strFilePath);
			fos.write(publicKey);
			fos.close();
		} catch (Exception e) {
		
		}
		*/
		publicKey = new byte[128]; 
		try {
			fis = new FileInputStream(strFilePath);
			fis.read(publicKey);
			fis.close();
		} catch (Exception e) {
		
		}
		strFilePath = ".//privateKey";
		/*
		privateKey = RSACoder.getPrivateKey(keyMap);
		
		try {
			fos = new FileOutputStream(strFilePath);
			fos.write(privateKey);
			fos.close();
		} catch (Exception e) {
		
		}
		*/
		privateKey = new byte[512]; 		
		try {
			fis = new FileInputStream(strFilePath);
			fis.read(privateKey);
			fis.close();
		} catch (Exception e) {
		
		}
		System.out.println("Public Key :\n"+Base64.encodeBase64String(publicKey));
		System.out.println("Privacy Key :\n"+Base64.encodeBase64String(privateKey));
		
		
	}
	@Test
	public void test() throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String content = "1234567890 "+dateFormat.format(date) ;
		byte[] data = content.getBytes();
		System.out.println("Original Data:\n" + content);
		byte[] encodedData = RSACoder.encryptByPublicKey(data, publicKey);
		System.out.println(String.valueOf(encodedData.length));
		byte[] newdata = new byte[64];
		for (int n=0; n<64; n++) {
			newdata[n] = encodedData[n];
		}
		System.out.println("Encoded Data:\n" + Base64.encodeBase64String(newdata));
		byte[] decodedData = RSACoder.decryptByPrivateKey(newdata, privateKey);
		String result = new String(decodedData);
		System.out.println("Decoded Data:\n" + result);
		

	}
}
