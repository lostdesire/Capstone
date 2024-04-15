import Crypto.AES;
import Crypto.RSA;
import java.util.Base64;
import java.util.Scanner;
import javax.crypto.SecretKey;

public class Crypto {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		RSA RSA2 = new RSA();
		AES AES1 = new AES();
		RSA2.keyGen();
		AES1.keyGen();
		
		System.out.println("2 user의 public key modulus : " + RSA2.publicKeySpec.getModulus());
		System.out.println("2 user의 public key exponent : " + RSA2.publicKeySpec.getPublicExponent());
		System.out.println("2 user의 private key modulus : " + RSA2.privateKeySpec.getModulus());
		System.out.println("2 user의 private key exponent : " + RSA2.privateKeySpec.getPrivateExponent());
		byte[] serverkey = RSA2.publickey.getEncoded();
		// 서버에 전송시에 base64로 인코딩하여 전달
		String strServerKey = Base64.getEncoder().encodeToString(serverkey);
		System.out.println("\nserver에 저장된 2 user의 public key : " + strServerKey);
		System.out.println("\nAES-GCM 256 key byte : " + AES1.key);
		
		// AES 키 RSA 공개키로 암호화
		String encryptedAES = RSA.encrypt(AES1.key.getEncoded(), strServerKey);
		System.out.println("서버를 통해 전송된 암호화된 AES 키 : " + encryptedAES);
		
		// 전달받은 암호화된 AES 키 RSA 개인키로 복호화
		SecretKey key = RSA.decrypt(encryptedAES, RSA2.privatekey);
		System.out.println("\n전달받은 AES 키 복호화 결과 : " + key);
		
		// 매 메세지 전송시 새로운 initialize vector 생성
		AES1.ivGen();
		System.out.println("\n1 user send : ");
		String text = sc.nextLine();
		
		// AES 키와 IV로 암호화
		byte[] encryptedText = AES.encrypt(text.getBytes(), AES1.key, AES1.IV);
		// 서버를 통해 전달하기 위해 String 형으로 변환
		String encText = Base64.getEncoder().encodeToString(encryptedText);
		System.out.println("Server text : " + encText);
		
		// String 형으로 전달된 내용을 byte 형으로 재 변환
		byte[] decText = Base64.getDecoder().decode(encText);
		// 전달받은 내용을 AES 키와 IV로 복호화
		byte[] decryptedText = AES.decrypt(decText, key, AES1.IV);
		String decryptText = new String(decryptedText);
		System.out.println("\n2 user receive : " + decryptText);
		
		AES1.ivGen();
		System.out.println("\n2 user send : ");
		text = sc.nextLine();
		
		encryptedText = AES.encrypt(text.getBytes(), key, AES1.IV);
		encText = Base64.getEncoder().encodeToString(encryptedText);
		System.out.println("Server text : " + encText);
		
		decText = Base64.getDecoder().decode(encText);
		decryptedText = AES.decrypt(decText, AES1.key, AES1.IV);
		decryptText = new String(decryptedText);
		System.out.println("\n1 user receive : " + decryptText);
		
		sc.close();
	}
}
