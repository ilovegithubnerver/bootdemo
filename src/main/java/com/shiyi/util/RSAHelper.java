package com.shiyi.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

/**
 * RSAHelper - 对RSA 签名&验签/分段加密&分段解密 的包装 签名算法: "SHA1withRSA", 私钥进行签名; 公钥进行验签.
 * 加密算法: "RSA/ECB/PKCS1Padding", 公钥进行加密; 私钥进行解密.
 * 
 * [localPrivKey]是自己的私钥, 自己的公钥给通信对方. [peerPubKey]是对方的公钥, 对方的私钥在对方那边. 为了方便,
 * 这里假定双方的密钥长度一致, 签名和加密的规则也一致.
 * 
 * 以`Base64Str`结尾的参数表示内容是Base64编码的字符串, 其他情况都是raw字符串.
 */

public class RSAHelper {
	/**
	 * 合作方自己的私钥
	 */
	public static  String privKey ;
	/**
	 * 信联提供的公钥
	 */
	public static  String pubKey ;
	/**
	 * 合作方自己的私钥
	 */
	//public static String privKey = null;
	/**
	 * 信联提供的公钥
	 */
	//public static String pubKey = null;

	private PrivateKey localPrivKey;
	private PublicKey peerPubKey;
	static  {
        Properties prop =  new  Properties();
        InputStream in = Object. class .getResourceAsStream( "classpath:initKey.properties" );
         try  {
           // prop.load(in);
            privKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC+2ptugsIYIRxg" +
					"jIHIcCn1mrjKY8Shj2Nmq5eRDg3Vj3ALoFrJLFUXdVdv5k40tZ7nGgqp99GnpZDf" +
					"W+luP6pc7ofiawpCtdldT97+uTDp+H9JX34VwPqhezeR/lHxMetr7vMwPy6lWKlW" +
					"gRE8g9umHcUu05v0xx4l01StRmdLwIr+MyzkWG4NX/OYALlepyHdRPwGBQlzZJEn" +
					"wjZjVdyeBuAQYyienGVn7LGLjX4RxKv81+R0zJKgF7p6BRsvVuzQRAJu8tTTKHJh" +
					"pX8qLh2hslohg72vzNUGQuk4qMV7TDGu1cMltFT+n3VyEl6QoxtYC4J90mBOAqaI" +
					"iIDP6l3tAgMBAAECggEASgPtk4YST9lHcAoNqvYUz7MZRyQ59Y3uNrsFJLQWmWPv" +
					"SBMAIlSXC2nuZu8iFT76SRXIXPJG1IVZEcZLWP5Hy5KWEDEC4aw0iAOZr5+SYKqG" +
					"cqYd98HHcSif/HZswOVjL5CTr3pRVL51rZ6QWK+qV1nkRqy6Jsux3Wl/xVMwbCes" +
					"TLufVOE1cRvNm9HnOturAz3yiWCxieytr7pk5KoCyNgzeE8oamujHcK4Nc2Q7mdd" +
					"Wd6qAA6ur98YiSVo2L/q8wSPgfZgrgMWeSUqFdRYlg3jLxfc6MrAke+Wjuh0HBNE" +
					"yvugSsn3RYz2ZIV3QuI2AuChRfv3lyafuvjan1Dr4QKBgQDeWfgLGBBQWQVPz7eX" +
					"AZym2+nSAq+JSAUB+bI8ff8xURGC/YhvJR2QYCjbZneSqTR8YpjZ5ANtM6G00zw9" +
					"BWA+5A9T95VXtBhf5uw0i2ZMiLOnOIJprOjh9bw3FiJ8ND7E+Pg5Vcbi0GymnnAC" +
					"NQOTeV/mqky4Uf7FFyQ3FJ0B7wKBgQDbvGfQAUwfIa/8bFmKql4koqp59FBQcLB2" +
					"9jhhXnXHO5qWhX34eLq6LvLBByHwSKahNOr1TjhwS9lKn/TDbiA4keGo1zHzC4ZR" +
					"NTpYxaKvjXJ6iQE1D/KZ5ZNsIHB/3Va+MYzYeTOcFiSqtlhZ2z7oRZVoWaI0pO1U" +
					"ac+5aaPJ4wKBgBHP30D0QopNN1G3cBuWs3ficg6yzGEQ9eW8nPyfk0veFKsLqFxO" +
					"+tlP+rXotXd4Oo4kT95Ul7mtwyx7J34z6gvftSKi2MHX2YQFObLT3/QNl2az7i6M" +
					"/XLsfpFjIowUm1qoHypoprAQxkNqucpTeN/df0M5LUAuwVwEiJApBJLdAoGBAJ2w" +
					"/+9Ofwp4aouEVl4lFZrFAgKzSk/wDzPQT3XRDEUGiVIsStLeEWM7/gp4y+pcuPhH" +
					"ubaBhA9S34WmdUkq5wSx7Hf2t2nsg2AUCuNaMf46TEWTvMIPx9KZ5spaHWIlvfKQ" +
					"LLvNfzQsz/OTGKSjqumVxjs02CtfkNJbfS9amKhxAoGAOmTdTzRbNRI8fEX8X3YA" +
					"tlTEEPjPx0VwHeBpRNIxXqBrQCwVzWUGSvga6ocM0ultKFFEXfkpqd6gL9C16k19" +
					"PVxYXzlCOX2ELJvQ9Y1acQXbAjveGBy6uKlKBYTanjZrbqYTDLUl3YzNa8jGH5uv" +
					"LVjwYoR9I4wKkf5BdNJnr5c=";
					//privKey;//prop.getProperty("privKey").trim();
            pubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqw20Q7RkqbFx6hhwSUxb" +
					"M9Fa2l5g+raw5tSJxYAJZby+wlFF/p3TNupd5gDozEw680w+NFXLplrzPvQRnxTj" +
					"jakY4V5PorhAIRtHz3O5o6qstiqyfYGSDUbEfPLzKjLVrXCv3kDT49d2PAD+03WV" +
					"t34C3I9/ANrluDH+5BxYB2SiAi8tkpJLAHy7GZQKqxxo0KsaBoECVRCZehyW9w0+" +
					"1TcqDBVguLYo/D5otk0COc3AqMULUwtqzsu8gYVLwQZr5LgvvFtcQm5E2Kp/54Ee" +
					"Ht8PcjbVhpwInldt20WhOjBXjNftemz7JAO6KftWjzi+KzoZqQuRA27hqJeVtIL+" +
					"5wIDAQAB";
					//prop.getProperty("xlpubKey").trim();
        }  catch  (Exception e) {
            e.printStackTrace();
        }
    }
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	public static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; // 加密block需要预留11字节
	public static final int KEYBIT = 2048;
	public static final int RESERVEBYTES = 11;



	public RSAHelper() {
	}

	/**
	 * 初始化自己的私钥,对方的公钥以及密钥长度.
	 *
	 * @param keysize
	 *            Base64编码的私钥,PKCS#8编码. (去掉pem文件中的头尾标识)
	 * @param keysize
	 *            Base64编码的公钥. (去掉pem文件中的头尾标识)
	 * @param keysize
	 *            密钥长度, 一般2048
	 */
	public void initKey(int keysize)  {

			try {
                localPrivKey = getPrivateKey(privKey);
                peerPubKey = getPublicKey(pubKey);
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in
	 *            公钥输入流
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public RSAPublicKey getPublicKey(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return getPublicKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("公钥数据流读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥输入流为空");
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				throw new Exception("关闭输入缓存流出错");
			}

			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				throw new Exception("关闭输入流出错");
			}
		}
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	public RSAPublicKey getPublicKey(String publicKeyStr) throws Exception {
		try {
			//BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = ApacheCodecBase64Util.decodeStringToByte(publicKeyStr);//base64Decoder.decodeBuffer(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			RSAPublicKey publicKey = (RSAPublicKey) keyFactory
					.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (IOException e) {
			throw new Exception("公钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param in
	 *            私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public RSAPrivateKey getPrivateKey(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		try {
			String readLine = null;
			StringBuilder sb = new StringBuilder();
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				} else {
					sb.append(readLine);
					sb.append('\r');
				}
			}
			return getPrivateKey(sb.toString());
		} catch (IOException e) {
			throw new Exception("私钥数据读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥输入流为空");
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				throw new Exception("关闭输入缓存流出错");
			}

			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				throw new Exception("关闭输入流出错");
			}
		}
	}

	/**
	 * 从字符串中加载私钥
	 * 
	 * @param privateKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载私钥时产生的异常
	 */
	public RSAPrivateKey getPrivateKey(String privateKeyStr) throws Exception {
		try {
			//BASE64Decoder base64Decoder = new BASE64Decoder();
			byte[] buffer = ApacheCodecBase64Util.decodeStringToByte(privateKeyStr);//base64Decoder.decodeBuffer(privateKeyStr);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory
					.generatePrivate(keySpec);
			return privateKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (IOException e) {
			throw new Exception("私钥数据内容读取错误");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	/**
	 * RAS加密
	 * 
	 * @param plainBytes
	 *            公钥
	 * @param useBase64Code
	 *            待加密信息
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] encryptRSA(byte[] plainBytes, boolean useBase64Code, String charset)
			throws Exception {
		String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; // 加密block需要预留11字节
		int KEYBIT = 2048;
		int RESERVEBYTES = 11;
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		int decryptBlock = KEYBIT / 8; // 256 bytes
		int encryptBlock = decryptBlock - RESERVEBYTES; // 245 bytes
		// 计算分段加密的block数 (向上取整)
		int nBlock = (plainBytes.length / encryptBlock);
		if ((plainBytes.length % encryptBlock) != 0) { // 余数非0，block数再加1
			nBlock += 1;
		}
		// 输出buffer, 大小为nBlock个decryptBlock
		ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock
				* decryptBlock);
		cipher.init(Cipher.ENCRYPT_MODE, peerPubKey);
		// cryptedBase64Str =
		// Base64.encodeBase64String(cipher.doFinal(plaintext.getBytes()));
		// 分段加密
		for (int offset = 0; offset < plainBytes.length; offset += encryptBlock) {
			// block大小: encryptBlock 或 剩余字节数
			int inputLen = (plainBytes.length - offset);
			if (inputLen > encryptBlock) {
				inputLen = encryptBlock;
			}
			// 得到分段加密结果
			byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
			// 追加结果到输出buffer中
			outbuf.write(encryptedBlock);
		}
		// 如果是Base64编码，则返回Base64编码后的数组
		if (useBase64Code) {
			return Base64.encodeBase64String(outbuf.toByteArray()).getBytes(
					charset);
		} else {
			return outbuf.toByteArray(); // ciphertext
		}
	}

	/**
	 * RSA解密
	 * 
	 * @param cryptedBytes
	 *            私钥
	 * @param cryptedBytes
	 *            待解密信息
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] decryptRSA(byte[] cryptedBytes, boolean useBase64Code,
			String charset) throws Exception {
		String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding"; // 加密block需要预留11字节
		byte[] data = null;

		// 如果是Base64编码的话，则要Base64解码
		if (useBase64Code) {
			data = Base64.decodeBase64(new String(cryptedBytes, charset));
		} else {
			data = cryptedBytes;
		}

		int KEYBIT = 2048;
		int RESERVEBYTES = 11;
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		int decryptBlock = KEYBIT / 8; // 256 bytes
		int encryptBlock = decryptBlock - RESERVEBYTES; // 245 bytes
		// 计算分段解密的block数 (理论上应该能整除)
		int nBlock = (data.length / decryptBlock);
		// 输出buffer, , 大小为nBlock个encryptBlock
		ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock
				* encryptBlock);
		cipher.init(Cipher.DECRYPT_MODE, localPrivKey);
		// plaintext = new
		// String(cipher.doFinal(Base64.decodeBase64(cryptedBase64Str)));
		// 分段解密
		for (int offset = 0; offset < data.length; offset += decryptBlock) {
			// block大小: decryptBlock 或 剩余字节数
			int inputLen = (data.length - offset);
			if (inputLen > decryptBlock) {
				inputLen = decryptBlock;
			}

			// 得到分段解密结果
			byte[] decryptedBlock = cipher.doFinal(data, offset, inputLen);
			// 追加结果到输出buffer中
			outbuf.write(decryptedBlock);
		}
		outbuf.flush();
		outbuf.close();
		return outbuf.toByteArray();
	}

	/**
	 * RSA签名
	 * 
	 * @param plainBytes
	 *            私钥
	 * @param useBase64Code
	 *            需要签名的信息
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] signRSA(byte[] plainBytes, boolean useBase64Code,
			String charset) throws Exception {
		String SIGNATURE_ALGORITHM = "SHA1withRSA";
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(localPrivKey);
		signature.update(plainBytes);

		// 如果是Base64编码的话，需要对签名后的数组以Base64编码
		if (useBase64Code) {
			return Base64.encodeBase64String(signature.sign())
					.getBytes(charset);
		} else {
			return signature.sign();
		}
	}

	/**
	 * 验签操作
	 * 
	 * @param plainBytes
	 *            公钥
	 * @param plainBytes
	 *            需要验签的信息
	 * @param signBytes
	 *            签名信息
	 * @return boolean
	 */
	public boolean verifyRSA(byte[] plainBytes, byte[] signBytes,
			boolean useBase64Code, String charset) throws Exception {
		boolean isValid = false;
		String SIGNATURE_ALGORITHM = "SHA1withRSA";
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(peerPubKey);
		signature.update(plainBytes);

		// 如果是Base64编码的话，需要对验签的数组以Base64解码
		if (useBase64Code) {
			isValid = signature.verify(Base64.decodeBase64(new String(
					signBytes, charset)));
		} else {
			isValid = signature.verify(signBytes);
		}
		return isValid;
	}
}
