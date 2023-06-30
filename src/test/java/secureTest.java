import com.rbac_demo.RbacApplication;
import com.rbac_demo.common.RSAUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author : lzy
 * @date : 2023/6/29
 * @effect :
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RbacApplication.class)
public class secureTest {


    @Test
    public void t() throws Exception {
        KeyPair keyPair = RSAUtils.getKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        PrivateKey aPrivate = keyPair.getPrivate();

        String plainText = "admin123";
        System.out.println("密码是：" + plainText);

        String encrypt = RSAUtils.encryptBase64(plainText, aPublic);
        System.out.println("加密后Base64加密：" + encrypt);

        String decrypt = RSAUtils.decryptBase64(encrypt, aPrivate);
        System.out.println("解密后：" + decrypt);
        Assert.assertEquals(decrypt,plainText);

    }
}
