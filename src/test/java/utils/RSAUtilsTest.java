package utils;

import com.rbac_demo.common.RSAUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;


public class RSAUtilsTest {

    @Test
    public void testGetKeyPair() throws Exception {
        KeyPair keyPair = RSAUtils.getKeyPair();
        Assertions.assertNotNull(keyPair);
        Assertions.assertNotNull(keyPair.getPublic());
        Assertions.assertNotNull(keyPair.getPrivate());
    }

    @Test
    public void testEncryptAndDecryptBase64() throws Exception {
        String content = "Hello, RSA!";
        KeyPair keyPair = RSAUtils.getKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String encryptedBase64 = RSAUtils.encryptBase64(content, publicKey);
        String decrypted = RSAUtils.decryptBase64(encryptedBase64, privateKey);

        Assertions.assertEquals(content, decrypted);
    }

    @Test
    void testSame() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        KeyPair keyPair = null;
        try {
             keyPair = RSAUtils.getKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String content = "Hello, RSA!";
        Assertions.assertNotNull(keyPair);
        String s = RSAUtils.encryptBase64(content, keyPair.getPublic());
        String s1 = RSAUtils.decryptBase64(s, keyPair.getPrivate());

        Assertions.assertEquals(content,s1);


    }
}
