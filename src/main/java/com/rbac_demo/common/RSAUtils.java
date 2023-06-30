package com.rbac_demo.common;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

/**
 * @author : lzy
 * @date : 2023/6/29
 * @effect :
 */


public class RSAUtils {

    public static String DEFAULT_ALG = "RSA";

    // 前端默认的填充方式！
//    private static final String PADDING = "RSA/ECB/NoPadding";
    private static final String PADDING = "RSA";


    /**
     *
     * @return 生成RSA 公钥和私钥
     * @throws Exception
     */
    public static KeyPair getKeyPair() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(DEFAULT_ALG);
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     *
     * @param content : 要加密的明文
     * @param publicKey: 使用的 RSA 公钥
     * @return BASE64 编码的加密内容
     * @throws Exception
     */
    public static String encryptBase64(String content,PublicKey publicKey) throws Exception{
        // 公钥加密
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes  =  cipher.doFinal(content.getBytes());
        // 抓化成 base64编码
        String base64String = Base64.getEncoder().encodeToString(encryptedBytes);

        return base64String;
    }

    /**
     *
     * @param base64EncryptedString: base64编码的加密内容
     * @param privateKey: 用来解密的私钥
     * @return 返回解密的内容
     * @throws Exception
     */
    public static String decryptBase64(String base64EncryptedString,PrivateKey privateKey) throws Exception{
        byte[] encryptedBytes = Base64.getDecoder().decode(base64EncryptedString);
        Cipher cipher = Cipher.getInstance(PADDING);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String content = new String(decryptedBytes);
        return content;
    }


}
