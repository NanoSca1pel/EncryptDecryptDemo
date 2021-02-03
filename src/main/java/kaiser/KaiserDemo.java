package kaiser;

/**
 * 凯撒加密
 * 原理：
 *  把26个字母往左或往右进行位移，在位移的时候需要注意，最多只能移动25位
 * @author lhtao
 * @date 2021/2/2 10:04
 */
public class KaiserDemo {

    public static void main(String[] args) throws Exception {
        //定义原文
        String input = "Hello World!";
        //把原文右移3位
        int key = 3;
        String encryptStr = encryptKaiser(input, key);
        String decryptStr = decryptKaiser(encryptStr, key);
    }

    /**
     * 凯撒加密
     * @param input 原文
     * @param key 移位位数
     */
    protected static String encryptKaiser(String input, int key) {
        //把字符串变成字节数组
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char aChar : chars) {
            int b = aChar;
            //往右移3位
            b += key;
            char newb = (char) b;
            sb.append(newb);
        }
        String encryptStr = sb.toString();
        System.out.println("密文==========" + encryptStr);
        return encryptStr;
    }

    /**
     * 凯撒解密
     * @param input 密文
     * @param key 密钥
     */
    protected static String decryptKaiser(String input, int key) {
        //把字符串变成字节数组
        char[] chars = input.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char aChar : chars) {
            int b = aChar;
            //往右移3位
            b -= key;
            char newb = (char) b;
            sb.append(newb);
        }
        String decryptStr = sb.toString();
        System.out.println("原文==========" + decryptStr);
        return decryptStr;
    }
}
