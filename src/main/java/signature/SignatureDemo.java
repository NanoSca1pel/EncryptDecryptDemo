package signature;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import rsaecc.RsaDemo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * 数字签名
 * @author lhtao
 * @date 2021/2/3 16:01
 */
public class SignatureDemo {

    public static void main(String[] args) throws Exception {
        String input = "123";
        String keyAlgorithm = "RSA";
        String signAlgorithm = "sha256withrsa";
        String pubKeyPath = "src/main/resources/pubKey";
        String priKeyPath = "src/main/resources/priKey";
        PrivateKey privateKey = RsaDemo.readPrivateKey(keyAlgorithm, priKeyPath);
        PublicKey publicKey = RsaDemo.readPublicKey(keyAlgorithm, pubKeyPath);

        String sign = sign(input, signAlgorithm, privateKey);
        System.out.println(sign);
        System.out.println(verfity(input, sign, signAlgorithm, publicKey));

    }

    /**
     * 通过私钥生成数字签名
     * @param input 原文
     * @param algorithm 签名算法
     * @param privateKey 私钥
     * @return
     */
    private static String sign(String input, String algorithm, PrivateKey privateKey) throws Exception {
        //获取签名对象
        Signature signature = Signature.getInstance(algorithm);
        //初始化签名
        signature.initSign(privateKey);
        //传入原文
        signature.update(input.getBytes());
        //开始签名
        byte[] sign = signature.sign();
        //进行Base64编码
        return Base64.encode(sign);

    }

    /**
     * 通过公钥校验签名
     * @param input 原文
     * @param signStr 签名
     * @param algorithm 签名算法
     * @param publicKey 私钥
     * @return
     */
    private static boolean verfity(String input, String signStr, String algorithm, PublicKey publicKey) throws Exception {
        //获取签名对象
        Signature signature = Signature.getInstance(algorithm);
        //初始化校验
        signature.initVerify(publicKey);
        //传入原文
        signature.update(input.getBytes());
        //开始校验
        return signature.verify(Base64.decode(signStr));
    }
}
