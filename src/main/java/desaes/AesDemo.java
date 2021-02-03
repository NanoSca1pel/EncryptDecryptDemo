package desaes;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES密钥长度必须是16个字节
 * @author lhtao
 * @date 2021/2/3 10:39
 */
public class AesDemo {

    public static void main(String[] args) throws Exception {
        //原文
        String input = "尚贤谷";
        //定义密钥key
        String key = "1234567890123456";
        //设置算法
        String transformation = "AES";
        //创建加密类型
        String algorithm = "AES";
        String encode = encryptDES(input, key, transformation, algorithm);
        System.out.println("加密结果：" + encode);
        String decode = decryptDES(encode, key, transformation, algorithm);
        System.out.println("解密结果：" + decode);
    }

    /**
     * des加密算法
     * @param input 原文
     * @param key 密钥
     * @param transformation 算法
     * @param algorithm 加密类型
     * @return
     * @throws Exception
     */
    private static String encryptDES(String input, String key, String transformation, String algorithm) throws Exception {
        //创建加密对象
        Cipher cipher = Cipher.getInstance(transformation);
        //创建加密规则
        //第一个参数表示密钥的字节
        //第二个参数表示加密的类型
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        //进行加密初始化
        //第一个参数表示模式：加密模式、解密模式
        //第二个参数表示加密规则
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        //调用加密方法
        //参数表示需要加密的原文的字节数组
        byte[] bytes = cipher.doFinal(input.getBytes());
        //打印密文
        //如果直接打印密文会出现乱码，是因为编码表上面找不到对应的字符。因为ascii上没有负数
        /*for (byte aByte : bytes) {
            System.out.println(aByte);
        }
        System.out.println(new String(bytes));*/

        //创建base64转码
        return Base64.encode(bytes);
    }

    /**
     * des解密算法
     * @param encode 密文
     * @param key 密钥
     * @param transformation 算法
     * @param algorithm 解密类型
     * @return
     * @throws Exception
     */
    private static String decryptDES(String encode, String key, String transformation, String algorithm) throws Exception {
        //创建解密对象
        Cipher cipher = Cipher.getInstance(transformation);
        //创建解密规则
        //第一个参数表示密钥的字节
        //第二个参数表示解密的类型
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        //进行解密初始化
        //第一个参数表示模式：加密模式、解密模式
        //第二个参数表示解密规则
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        //调用解密方法
        //参数表示需要密文经过Base64解密后的字节数组
        byte[] bytes = cipher.doFinal(Base64.decode(encode));
        //打印原文
        return new String(bytes);
    }
}
