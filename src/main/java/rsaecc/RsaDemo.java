package rsaecc;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 非对称加密： RSA 和 ECC
 * 非对称加密算法必须要有两个密钥，一个公钥，一个私钥。两者并称密钥对
 * 如果使用公钥加密，则必须使用私钥解密
 * 如果使用私钥加密，则必须使用公钥解密
 *
 * 公钥和私钥的密钥规则不一样： 公钥X509EncodedKeySpec  私钥PKCS8EncodedKeySpec
 * @author lhtao
 * @date 2021/2/3 13:46
 */
public class RsaDemo {

    public static void main(String[] args) throws Exception {
        //原文
        String input = "硅谷尚贤谷硅谷尚贤谷";

        // 设置算法
        String algorithm = "RSA";
        String pubKeyPath = "src/main/resources/pubKey";
        String priKeyPath = "src/main/resources/priKey";

        //生成密钥对并保存到文件中
        /*generateKeyToFile(algorithm, "src/main/resources/pubKey", "src/main/resources/priKey");

        String publicKeyCode = FileUtils.readFileToString(new File(pubKeyPath), StandardCharsets.UTF_8);
        String privateKeyCode = FileUtils.readFileToString(new File(priKeyPath), StandardCharsets.UTF_8);

        //打印
        System.out.println("publicKeyCode: " + publicKeyCode);
        System.out.println("privateKeyCode: " + privateKeyCode);*/

        PrivateKey privateKey = readPrivateKey(algorithm, priKeyPath);
        PublicKey publicKey = readPublicKey(algorithm, pubKeyPath);

        String encryptRSAByPriKey = encryptRSAByPrivateKey(input, algorithm, privateKey);
        System.out.println("RSA 私钥加密：" + encryptRSAByPriKey);
        String decryptRSAByPubKey = decryptRSAByPublicKey(encryptRSAByPriKey, algorithm, publicKey);
        System.out.println("RSA 公钥解密：" + decryptRSAByPubKey);

        System.out.println("=====================================================");

        String encryptRSAByPubKey = encryptRSAByPublicKey(input, algorithm, publicKey);
        System.out.println("RSA 公钥加密：" + encryptRSAByPubKey);
        String decryptRSAByPriKey = decryptRSAByPrivateKey(encryptRSAByPubKey, algorithm, privateKey);
        System.out.println("RSA 私钥解密：" + decryptRSAByPriKey);

    }

    /**
     * 私钥加密
     * @param input 原文
     * @param algorithm 算法
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String encryptRSAByPrivateKey(String input, String algorithm, PrivateKey privateKey) throws Exception {
        //创建加密对象
        Cipher cipher = Cipher.getInstance(algorithm);
        //对加密进行初始化
        //第一个参数表示加密模式
        //第二个参数表示加密的密钥值
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        //使用私钥加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        return Base64.encode(bytes);
    }

    /**
     * 公钥加密
     * @param input 原文
     * @param algorithm 算法
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String encryptRSAByPublicKey(String input, String algorithm, PublicKey publicKey) throws Exception {
        //创建加密对象
        Cipher cipher = Cipher.getInstance(algorithm);
        //对加密进行初始化
        //第一个参数表示加密模式
        //第二个参数表示加密的密钥值
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //使用私钥加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        return Base64.encode(bytes);
    }

    /**
     * 私钥解密
     * @param encodeStr 密文
     * @param algorithm 算法
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decryptRSAByPrivateKey(String encodeStr, String algorithm, PrivateKey privateKey) throws Exception {
        //创建加密对象
        Cipher cipher = Cipher.getInstance(algorithm);
        //对加密进行初始化
        //第一个参数表示加密模式
        //第二个参数表示加密的密钥值
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //使用私钥加密
        byte[] bytes = cipher.doFinal(Base64.decode(encodeStr));
        return new String(bytes);
    }

    /**
     * 公钥解密
     * @param encodeStr 密文
     * @param algorithm 算法
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String decryptRSAByPublicKey(String encodeStr, String algorithm, PublicKey publicKey) throws Exception {
        //创建加密对象
        Cipher cipher = Cipher.getInstance(algorithm);
        //对加密进行初始化
        //第一个参数表示加密模式
        //第二个参数表示加密的密钥值
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        //使用私钥加密
        byte[] bytes = cipher.doFinal(Base64.decode(encodeStr));
        return new String(bytes);
    }

    /**
     * 生成公钥私钥对，并保存到本地文件中
     * @param algorithm 算法
     * @param publicKeyPath 公钥路径
     * @param privateKeyPath 私钥路径
     */
    private static void generateKeyToFile(String algorithm, String publicKeyPath, String privateKeyPath) throws Exception {
        // 创建密钥对
        //KeyPairGenerator:密钥对生成对象
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        //生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //生成公钥和私钥
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        //获取私钥的字节数组
        byte[] publicEncoded = publicKey.getEncoded();
        byte[] privateEncoded = privateKey.getEncoded();

        //使用Base64进行编码
        String publicKeyCode = Base64.encode(publicEncoded);
        String privateKeyCode = Base64.encode(privateEncoded);

        //保存到本地文件中
        FileUtils.writeStringToFile(new File(publicKeyPath), publicKeyCode, StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(privateKeyPath), privateKeyCode, StandardCharsets.UTF_8);
    }


    /**
     * 读取私钥
     * @param algorithm 算法
     * @param priKeyPath 私钥路径
     * @return 返回私钥的key
     */
    public static PrivateKey readPrivateKey(String algorithm, String priKeyPath) throws Exception {
        String privateKeyCode = FileUtils.readFileToString(new File(priKeyPath), Charset.defaultCharset());

        //创建key的工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        //创建私钥key的规则
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyCode));
        //返回私钥对象
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 读取公钥
     * @param algorithm 算法
     * @param pubKeyPath 公钥路径
     * @return 返回公钥的key
     */
    public static PublicKey readPublicKey(String algorithm, String pubKeyPath) throws Exception {
        String publicKeyCode = FileUtils.readFileToString(new File(pubKeyPath), Charset.defaultCharset());

        //创建key的工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        //创建公钥key的规则
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKeyCode));
        //返回私钥对象
        return keyFactory.generatePublic(keySpec);
    }
}
