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
		
		System.out.println("2 user�� public key modulus : " + RSA2.publicKeySpec.getModulus());
		System.out.println("2 user�� public key exponent : " + RSA2.publicKeySpec.getPublicExponent());
		System.out.println("2 user�� private key modulus : " + RSA2.privateKeySpec.getModulus());
		System.out.println("2 user�� private key exponent : " + RSA2.privateKeySpec.getPrivateExponent());
		byte[] serverkey = RSA2.publickey.getEncoded();
		// ������ ���۽ÿ� base64�� ���ڵ��Ͽ� ����
		String strServerKey = Base64.getEncoder().encodeToString(serverkey);
		System.out.println("\nserver�� ����� 2 user�� public key : " + strServerKey);
		System.out.println("\nAES-GCM 256 key byte : " + AES1.key);
		
		// AES Ű RSA ����Ű�� ��ȣȭ
		String encryptedAES = RSA.encrypt(AES1.key.getEncoded(), strServerKey);
		System.out.println("������ ���� ���۵� ��ȣȭ�� AES Ű : " + encryptedAES);
		
		// ���޹��� ��ȣȭ�� AES Ű RSA ����Ű�� ��ȣȭ
		SecretKey key = RSA.decrypt(encryptedAES, RSA2.privatekey);
		System.out.println("\n���޹��� AES Ű ��ȣȭ ��� : " + key);
		
		// �� �޼��� ���۽� ���ο� initialize vector ����
		AES1.ivGen();
		System.out.println("\n1 user send : ");
		String text = sc.nextLine();
		
		// AES Ű�� IV�� ��ȣȭ
		byte[] encryptedText = AES.encrypt(text.getBytes(), AES1.key, AES1.IV);
		// ������ ���� �����ϱ� ���� String ������ ��ȯ
		String encText = Base64.getEncoder().encodeToString(encryptedText);
		System.out.println("Server text : " + encText);
		
		// String ������ ���޵� ������ byte ������ �� ��ȯ
		byte[] decText = Base64.getDecoder().decode(encText);
		// ���޹��� ������ AES Ű�� IV�� ��ȣȭ
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
