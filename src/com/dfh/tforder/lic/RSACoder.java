package com.dfh.tforder.lic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * ����ǩ���㷨RSA <br/>
 * 
 */
public class RSACoder {
	// ����ǩ������Կ�㷨
	public static final String KEY_ALGORITHM = "RSA";
	private static Cipher cipher;
	/**
	 * ����ǩ�� ǩ��/��֤�㷨
	 * */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	public static final String SIGNATURE_ALGORITHM_SHA1withRSA = "SHA1withRSA";

	/**
	 * RSA��Կ���ȣ�RSA�㷨��Ĭ����Կ������1024 ��Կ���ȱ�����64�ı�������512��65536λ֮��
	 * */
	private static final int KEY_SIZE = 2048;
	// ��Կ
	private static final String PUBLIC_KEY = "RSAPublicKey";

	// ˽Կ
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	static {
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ����Կ�� KEY_SIZE : 2048
	 * 
	 * @return Map �׷���Կ��Map
	 * */
	public static Map<String, Object> initKey() throws Exception {
		return initKey(KEY_SIZE);

	}

	/**
	 * ��ʼ����Կ�� ��Կ���ȱ�����64�ı�������512��65536λ֮��
	 * 
	 * @return Map �׷���Կ��Map
	 * */
	public static Map<String, Object> initKey(int keySize) throws Exception {
		// ʵ������Կ������
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		// ��ʼ����Կ������
		keyPairGenerator.initialize(keySize);
		// ������Կ��
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// �׷���Կ
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// �׷�˽Կ
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// ����Կ�洢��map��
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;

	}

	/**
	 * ǩ�� MD5withRSA
	 * 
	 * @param data��ǩ������
	 * @param privateKey
	 *            ��Կ
	 * @return byte[] ����ǩ��
	 * */
	public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {

		// MD5withRSA
		return sign(data, privateKey, SIGNATURE_ALGORITHM);
	}

	/**
	 * ǩ��
	 * 
	 * @param data��ǩ������
	 * @param privateKey
	 *            ��Կ
	 * @return byte[] ����ǩ��
	 * */
	public static byte[] sign(byte[] data, byte[] privateKey, String algorithm) throws Exception {

		// ȡ��˽Կ
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// ����˽Կ
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// ʵ����Signature
		Signature signature = Signature.getInstance(algorithm);
		// ��ʼ��Signature
		signature.initSign(priKey);
		// ����
		signature.update(data);
		return signature.sign();
	}

	/**
	 * ǩ��
	 * 
	 * @param data��ǩ������
	 * @param privateKey
	 *            ��Կ
	 * @return byte[] ����ǩ��
	 * */
	public static byte[] sign(byte[] data, PrivateKey priKey) throws Exception {

		// ʵ����Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// ��ʼ��Signature
		signature.initSign(priKey);
		// ����
		signature.update(data);
		return signature.sign();
	}

	/**
	 * У������ǩ�� MD5withRSA
	 * 
	 * @param data
	 *            ��У������
	 * @param publicKey
	 *            ��Կ
	 * @param sign
	 *            ����ǩ��
	 * @return boolean У��ɹ�����true��ʧ�ܷ���false
	 * */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
		// MD5withRSA
		return verify(data, publicKey, sign, SIGNATURE_ALGORITHM);
	}

	/**
	 * У������ǩ��
	 * 
	 * @param data
	 *            ��У������
	 * @param publicKey
	 *            ��Կ
	 * @param sign
	 *            ����ǩ��
	 * @return boolean У��ɹ�����true��ʧ�ܷ���false
	 * */
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign, String algorithm) throws Exception {
		// ת����Կ����
		// ʵ������Կ����
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// ��ʼ����Կ
		// ��Կ����ת��
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
		// ������Կ
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// ʵ����Signature
		Signature signature = Signature.getInstance(algorithm);
		// ��ʼ��Signature
		signature.initVerify(pubKey);
		// ����
		signature.update(data);
		// ��֤
		return signature.verify(sign);
	}

	/**
	 * У������ǩ��
	 * 
	 * @param data
	 *            ��У������
	 * @param publicKey
	 *            ��Կ
	 * @param sign
	 *            ����ǩ��
	 * @return boolean У��ɹ�����true��ʧ�ܷ���false
	 * */
	public static boolean verify(byte[] data, PublicKey pubKey, byte[] sign) throws Exception {

		// ʵ����Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// ��ʼ��Signature
		signature.initVerify(pubKey);
		// ����
		signature.update(data);
		// ��֤
		return signature.verify(sign);
	}

	/**
	 * ȡ��˽Կ
	 * 
	 * @param keyMap
	 *            ��Կmap
	 * @return byte[] ˽Կ
	 * */
	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/**
	 * ȡ�ù�Կ
	 * 
	 * @param keyMap
	 *            ��Կmap
	 * @return byte[] ��Կ
	 * */
	public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * �õ�˽Կ
	 * 
	 * @param key
	 *            ��Կ�ַ���������base64���룩
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64Util.decode(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	 * �õ���Կ
	 * 
	 * @param key
	 *            ��Կ�ַ���������base64���룩
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64Util.decode(key);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * ʹ�ù�Կ�����Ľ��м��ܣ�����BASE64������ַ���
	 * 
	 * @param publicKey
	 * @param plainText
	 * @return
	 */
	public static String encrypt(PublicKey publicKey, String plainText) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] enBytes = cipher.doFinal(plainText.getBytes());
			return Base64Util.encode(enBytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ʹ��keystore�����Ľ��м���
	 * 
	 * @param publicKeystore
	 *            ��Կ�ļ�·��
	 * @param plainText
	 *            ����
	 * @return
	 */
	public static String encrypt(String publicKeystore, String plainText) {
		try {
			FileReader fr = new FileReader(publicKeystore);
			BufferedReader br = new BufferedReader(fr);
			String publicKeyString = "";
			String str;
			while ((str = br.readLine()) != null) {
				publicKeyString += str;
			}
			br.close();
			fr.close();
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKeyString));
			byte[] enBytes = cipher.doFinal(plainText.getBytes());
			return Base64Util.encode(enBytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ʹ��˽Կ���������Ľ��н���
	 * 
	 * @param privateKey
	 * @param enStr
	 * @return
	 */
	public static String decrypt(PrivateKey privateKey, String enStr) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			byte[] deBytes = cipher.doFinal((new BASE64Decoder()).decodeBuffer(enStr));
			byte[] deBytes = cipher.doFinal(Base64Util.decode(enStr));
			return new String(deBytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ʹ��keystore�����Ľ��н���
	 * 
	 * @param privateKeystore
	 *            ˽Կ·��
	 * @param enStr
	 *            ����
	 * @return
	 */
	public static String decrypt(String privateKeystore, String enStr) {
		try {
			FileReader fr = new FileReader(privateKeystore);
			BufferedReader br = new BufferedReader(fr);
			String privateKeyString = "";
			String str;
			while ((str = br.readLine()) != null) {
				privateKeyString += str;
			}
			br.close();
			fr.close();
			cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKeyString));
			byte[] deBytes = cipher.doFinal(Base64Util.decode(enStr));
			return new String(deBytes);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// // ��ʼ����Կ
		// // ������Կ��
		// Map<String, Object> keyMap = RSACoder.initKey();
		// byte[] publicKeyByte = RSACoder.getPublicKey(keyMap);
		// byte[] privateKeyByte = RSACoder.getPrivateKey(keyMap);
		// String publicKeyStr = Base64Util.encode(publicKeyByte);
		// String privateKeyStr = Base64Util.encode(privateKeyByte);
		// System.out.println("��Կ��" + publicKeyStr);
		// System.out.println("˽Կ��" + privateKeyStr);
		//
		// publicKeyByte = Base64Util.decode(publicKeyStr);
		// privateKeyByte = Base64Util.decode(privateKeyStr);
		// System.out.println("��Կ��" + Base64Util.encode(publicKeyByte));
		// System.out.println("˽Կ��" + Base64Util.encode(privateKeyByte));

		String desKey = "2016-01-01";
		String publicKeyStr = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxaeeTJBJIMo+OzJtiX5hBXgNf/2FZnAajqDV4BhZLFUIIbgaBSd6WyKHTKOll4PYftX8sg/g3/IDBgCRr/u6+VWM8btUQa9dQLfiNlvfR1+t7zbJVYHai/XavSpgXbkqXzxwSLCs9WvwnWTcToVCQBCh3fSR7h90SRPW21VUyugJc/+bzjuLbehw19MESzj1MZ/DMzkmuzbUW+0rLJkXLN9WvYUMCCls4+YVkS6+nu+9glylt+/+7LbywL+CiAU5NDNP3L003qIMyiBA+bsULESDA1Y5bzKpCbEKB4H5iBdtnffawn5C7c+vKIc/J1obz6q+bI0JGnRzeyQlpVwVQQIDAQAB";
		PublicKey publicKey = getPublicKey(publicKeyStr);
		String desKeyCode = encrypt(publicKey, desKey);
		System.out.println("desKeyCode: " + desKeyCode);
		String privateKeyStr = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDFp55MkEkgyj47Mm2JfmEFeA1//YVmcBqOoNXgGFksVQghuBoFJ3pbIodMo6WXg9h+1fyyD+Df8gMGAJGv+7r5VYzxu1RBr11At+I2W99HX63vNslVgdqL9dq9KmBduSpfPHBIsKz1a/CdZNxOhUJAEKHd9JHuH3RJE9bbVVTK6Alz/5vOO4tt6HDX0wRLOPUxn8MzOSa7NtRb7SssmRcs31a9hQwIKWzj5hWRLr6e772CXKW37/7stvLAv4KIBTk0M0/cvTTeogzKIED5uxQsRIMDVjlvMqkJsQoHgfmIF22d99rCfkLtz68ohz8nWhvPqr5sjQkadHN7JCWlXBVBAgMBAAECggEBAJWdM64w6anwbPrSSSfyie5h2ZYwiXHpGbEs/bgLtjRgiS889DVClNO3Z0MT/JycKJyZzhXa/xWFU9SPSbg0jGt/3ph8fGLcI3KBvrH3CSQ1wKt6hRw8lvmquFDIavmG2BQO48+iLMn5UsFRP5IQPP3RmTUxk+Tz2koWwmR0SyLq/N+upTCClsWtxYJXGq19MXNyX850fKSQGhAfrjp06zkO9VDd8loBmyWhxqwGI6I21IkLW8pUGLkqzWYECa440VjEEElPzvhHg10WSXliZpxzCSFkj95d1tbNTvyuf6ONJOzfvRmzCLvvOVKPxgZkvHxYq1P80UKwJg6Cr1zrKaECgYEA5X3niwWlDXzXWV8ecnqmjwZJRMHf7VEsHTaOxmQNq8LthYwZqCA/eMRtPX762hUGlrdfd8+khlNunvVpW+KeD2FfziL6jwv+EvxL7t4jyBWFWGN7Vspq98ypXb3ObuKMfVHxt5vQOjki0/uAdR/VzXMrzFCIOlTXTxjUyqnr2EMCgYEA3HxJ/mAZnNIKDphOCU6nIQQOr1F/JeGy3vE37QIpDXZxOhiCNBmEND2K3N8Rlngv9Z/uQD/RFy71enkM/3TMmXzYfyNnnaXwKqj+7tJ/uWn3UyRjMo3BboADD4YHnTa2cv2aK5tYxUt8ErF1d7gdNLf0MWogcNAXtaB6ttcjFisCgYEAtBmi5M+BQJyAxHqJgn4OyJ++UF92ROA3JpXyZm+O6wUHMo9Ic1EbfsNol6JXa16k1RMDsRsbmu9PGSCvlpS6Roe8kCGTUT+wHWYykTt0CUoaY8tuuCYS3HWZVR/oKc1RQ+hTMizttUOxuXrezQw21g1UifuJbX23mzN5i7Lei9ECgYAyNI74wFM0tVMa7a1fSyydRPo/C5SjQJhlUZIFgudsLFst+mSzjlffOWBEWPr9Q1LQYGF0+dMXr1awU54VETiBfr8hfCNc72sXm7GpGf+BbdKmePOpkEPKcJXB1CgpsUqsNnkDkvUMv9p5uzTfVOo5leWh1uOaLJgpgGhQ9/4CTwKBgQC3h5pPqdq+NWBWyOAX1MDpS02ux35ogX5ZLAVYwyo7MBKjIdrrrDcKhSbQ7ePHZhCFOfDy8iRlBHnQ9ACap/JHwoCajZ9YwxHYttqGmWIMF+876AHBGFJFElPGurcj9cRiL4awpyRqVdQsMGSTGdIkAHWyyCoaJZ5haTcfuVEvvg==";
		PrivateKey privateKey = getPrivateKey(privateKeyStr);
		String desKeyEnd = decrypt(privateKey, desKeyCode);
		System.out.println("desKeyEnd: " + desKeyEnd);
	}
}