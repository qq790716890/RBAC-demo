package com.rbac_demo.common;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.*;
import java.util.Base64;

/**
 * @author : lzy
 * @date : 2023/6/29
 * @effect :
 */


public class RSAUtils {

    private RSAUtils(){}
    public static final String DEFAULT_ALG = "RSA";

    // 前端默认的填充方式！
    private static final String PADDING = "RSA/ECB/OAEPWithSHA-1AndMGF1PADDING";


    /**
     *
     * @return 生成RSA 公钥和私钥
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(DEFAULT_ALG);
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     *
     * @param content : 要加密的明文
     * @param publicKey: 使用的 RSA 公钥
     * @return BASE64 编码的加密内容
     * @throws Exception
     */
    public static String encryptBase64(String content,PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 公钥加密
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes  =  cipher.doFinal(content.getBytes());
        // 抓化成 base64编码

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     *
     * @param base64EncryptedString: base64编码的加密内容
     * @param privateKey: 用来解密的私钥
     * @return 返回解密的内容
     * @throws Exception
     */
    public static String decryptBase64(String base64EncryptedString,PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        byte[] encryptedBytes = Base64.getDecoder().decode(base64EncryptedString);
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);

    }


}
