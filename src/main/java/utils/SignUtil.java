package utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.TreeMap;


/**
 * @author xu_csheng
 * RSA签名和验证
 *
 */
public class SignUtil {


    public final static String CHARACTER_ENCODING_UTF_8 = "UTF-8";

    public static final String KEY_ALGORITHM = "RSA";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;


    public static void main(String[] args) {

        TreeMap<String,String> painText = new TreeMap();

        painText.put("merchantCode","100006");
        painText.put("token","d511579a-4ba2-4999-8c97-42a16cb8a968");
        // painText.put("userName","18012350146");

        System.out.println(JSON.toJSON(painText).toString());

        String priKeyText = null;
        String pubKeyText = null;
        try {
            priKeyText = readFileContent("D:\\data\\user\\upload\\100006\\privateKey");
            pubKeyText = readFileContent("D:\\data\\user\\upload\\100006\\publicKey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // 加签
            String localSignature = sign(priKeyText.toString().getBytes(CHARACTER_ENCODING_UTF_8), JSON.toJSON(painText).toString());
            System.out.println(localSignature);
            // 验签
            boolean verifyResult = verify(pubKeyText.toString().getBytes(CHARACTER_ENCODING_UTF_8), JSON.toJSON(painText).toString(), localSignature);
            System.out.println("verifyResult:" + verifyResult);

            String strF = encryptRSA("asggugh&*", pubKeyText);
            System.out.println(strF);
            String strG = decryptRSA(strF, priKeyText);
            System.out.println(strG);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * RSA私钥加签
     * priKeyText经过base64处理后的私钥
     * plainText明文内容
     * @return 十六进制的签名字符串
     * @throws Exception
     */
    public static String sign(byte[] priKeyText, String plainText) throws Exception {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKeyText));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey prikey = keyf.generatePrivate(priPKCS8);

            // 用私钥对信息生成数字签名
            Signature signet = Signature.getInstance("SHA256withRSA");
            signet.initSign(prikey);
            signet.update(plainText.getBytes("UTF-8"));
            return DigestUtil.byte2hex(signet.sign());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 公钥验签
     * pubKeyText经过base64处理后的公钥
     * plainText明文内容
     * signText十六进制的签名字符串
     * @return 验签结果 true验证一致 false验证不一致
     */
    public static boolean verify(byte[] pubKeyText, String plainText, String signText) {
        try {
            // 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                    Base64.getDecoder().decode(pubKeyText));
            // RSA算法
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            // 取公钥匙对象
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            // 十六进制数字签名转为字节
            byte[] signed = DigestUtil.hex2byte(signText.getBytes("UTF-8"));
            Signature signatureChecker = Signature.getInstance("SHA256withRSA");
            signatureChecker.initVerify(pubKey);
            signatureChecker.update(plainText.getBytes("UTF-8"));
            // 验证签名是否正常
            return signatureChecker.verify(signed);
        } catch (Throwable e) {
            return false;
        }
    }
    // 获取文件内容
    public static String readFileContent(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        FileInputStream inputStream = new FileInputStream(file);
        int length = inputStream.available();
        byte bytes[] = new byte[length];
        inputStream.read(bytes);
        inputStream.close();
        String str = new String(bytes, StandardCharsets.UTF_8);
        return str;
    }


    /**
     * 将字符串进行RSA加密
     *
     * @param text
     * @param pubKey
     * @return
     */
    public static String encryptRSA(String text, String pubKey) {
        String cipherTextBase64 = "";
        try {
            // 对公钥解密
            byte[] keyBytes = Base64.getDecoder().decode(pubKey);

            // 取得公钥
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key publicKey = keyFactory.generatePublic(x509KeySpec);

            // 对数据加密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] data = text.getBytes();
            int inputLength = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            while (inputLength - offset > 0) {
                if (inputLength - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offset, inputLength - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            cipherTextBase64 = Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            System.out.println("字符串进行RSA加密出现异常:" + e);
        }
        return cipherTextBase64;
    }

    /**
     * 将字符串进行RSA解密
     *
     * @param text
     * @param priKey
     * @return
     */
    public static String decryptRSA(String text, String priKey) {
        String cipherTextBase64 = "";
        try {
            // 对私钥解密
            byte[] keyBytes = Base64.getDecoder().decode(priKey);

            // 取得私钥
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

            // 对数据解密
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] data = Base64.getDecoder().decode(text);
            int inputLength = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offset = 0;
            byte[] cache;
            int i = 0;
            while (inputLength - offset > 0) {
                if (inputLength - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offset, inputLength - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            cipherTextBase64 = new String(decryptedData);
        } catch (Exception e) {
            System.out.println("字符串进行RSA加密出现异常:" + e);
        }
        return cipherTextBase64;
    }

}