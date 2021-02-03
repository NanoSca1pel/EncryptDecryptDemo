package digest;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要算法，防止篡改
 * 常见的消息摘要算法： md5，sha1，sha256，sha512
 *
 * 如果算法摘要加密后的数据出现乱码，可以使用Base64编码后输出，也可以迭代转16进制输出
 *
 * @author lhtao
 * @date 2021/2/3 13:23
 */
public class DigestDemo {

    public static void main(String[] args) throws Exception {
        //原文
        String input = "aa";
        //算法
        String algorithm = "MD5";
        System.out.println(algorithm + "\n" +getDigest(input, algorithm));

        algorithm = "SHA-1";
        System.out.println(algorithm + "\n" +getDigest(input, algorithm));

        algorithm = "SHA-256";
        System.out.println(algorithm + "\n" +getDigest(input, algorithm));

        algorithm = "SHA-512";
        System.out.println(algorithm + "\n" +getDigest(input, algorithm));

    }

    /**
     * 获取数字摘要
     * @param input 原文
     * @param algorithm 算法
     * @throws NoSuchAlgorithmException
     */
    private static String getDigest(String input, String algorithm) throws Exception {
        //创建消息摘要对象
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        //执行消息摘要算法
        byte[] bytes = digest.digest(input.getBytes());
        return toHex(bytes);
    }

    /**
     * 第一种方式：对密文进行迭代，转成16进制
     * @param bytes
     */
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            //将密文转换成16进制 0xff代表16进制
            String s = Integer.toHexString(b & 0xff);
            //如果密文的长度是1个字节，需要在高位补0
            if (s.length() == 1) {
                s = "0" + s;
            }
            sb.append(s);
        }
       return sb.toString();
    }
}
