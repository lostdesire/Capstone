package Crypto;
import java.util.Base64;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class RSA {
	public PublicKey publickey;
	public PrivateKey privatekey;
	public RSAPublicKeySpec publicKeySpec;
	public RSAPrivateKeySpec privateKeySpec;
	
	// key 생성
	public void keyGen()throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		publickey = keyPair.getPublic();
		privatekey = keyPair.getPrivate();
		
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		publicKeySpec = keyFactory.getKeySpec(publickey, RSAPublicKeySpec.class);
		privateKeySpec = keyFactory.getKeySpec(privatekey, RSAPrivateKeySpec.class);
	}
	
	// 암호화 함수
	public static String encrypt(byte[] AESKey, String serverkey) throws Exception {
		byte[] decodeServerKey = Base64.getDecoder().decode(serverkey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeServerKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		
		byte[] encryptAESKey = cipher.doFinal(AESKey);
		String encryptKey = Base64.getEncoder().encodeToString(encryptAESKey);
		
		return encryptKey;
	}
	
	// 복호화 함수
	public static SecretKey decrypt(String encryptKey, PrivateKey privateKey) throws Exception {
		byte[] decryptKey = Base64.getDecoder().decode(encryptKey);
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		
		byte[] decryptedAESKey = cipher.doFinal(decryptKey);
		SecretKey key = new SecretKeySpec(decryptedAESKey, "AES");
		
		return key;
	}
}
