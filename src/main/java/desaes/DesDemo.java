package desaes;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 对称加密
 * 加密和解密使用的是同一把密钥
 * 对称加密分为 流加密 和 块加密
 * ps：流加密  123456， 先加密1，再加密2，再加密3 ...
 *    块加密 123456， 先分块，比如每4个值分一块，先加密1234，56由于不满4个数，会填值补充至4位，然后加密56XX
 *
 * 如果使用des加密，密钥必须是8个字节
 *
 *
 * base64 不是加密算法,是可读性算法。它的目的不是为了保护数据，而是为了数据的可读性。
 * base64 是由64个字母组成，分别是大写A-Z，小写a-z，数字0-9，以及两个符号 + 和 /
 *
 * base58 一般用在比特币里面的一种编码方式。
 * base64和base58的区别： 在base58中没有数字0，也没有大写字母O，没有大写字母I和小写字母i，也没有 + 和 / 两个符号
 *
 * base64原理：base64是3个字节为一组，一个字节8位，一共24位。base64把3个字节转换为4组，每组6位。
 *            原本一个字节是8位，现在变成6位后，缺了2位。需要在高位进行补0
 *            这样做的好处是，在二进制中高位的0没用用处，base64只取后6位，将前两位0去掉，这样可以控制在0-63之间。 最大 111111 -> 2^6 - 1
 *
 * 在base64中，需要设置一组为3个字节，如果在编码后不够3个字节，就需要使用 = 补齐
 *
 * @author lhtao
 * @date 2021/2/2 16:13
 */
public class DesDemo {

    public static void main(String[] args) throws Exception {
        //原文 如果使用的是不填充模式NoPadding，那么原文必须是8个字节的整数倍
        String input = "尚贤谷";
        //定义密钥key
        String key = "12345678";
        //设置算法
        //String transformation = "DES";
        //DES表示算法
        //ECB表示加密模式
        //PKCS5Padding表示填充模式
        //如果只写了算法，没有加加密模式和填充模式，默认使用的就是/ECB/PKCS5Padding
        String transformation = "DES/ECB/PKCS5Padding";
        //String transformation = "DES/CBC/PKCS5Padding";
        //String transformation = "DES/CBC/NoPadding";
        //创建加密类型
        String algorithm = "DES";
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

        //如果使用CBC加密模式，需要添加iv向量。如果使用的填充模式为不填充，则iv向量也必须是8个字节
        /*IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);*/

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

        //如果使用CBC加密模式，需要添加iv向量。如果使用的填充模式为不填充，则iv向量也必须是8个字节
        /*IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);*/

        //调用解密方法
        //参数表示需要密文经过Base64解密后的字节数组
        byte[] bytes = cipher.doFinal(Base64.decode(encode));
        //打印原文
        return new String(bytes);
    }
}
