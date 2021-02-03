package ascii;

import cn.hutool.json.JSONUtil;

import java.util.Arrays;

/**
 * Ascii编码加解密
 * @author lhtao
 * @date 2021/2/2 9:55
 */
public class AsciiDemo {

    public static void main(String[] args) throws Exception {

        String str = "AaZ";
        char[] chars = str.toCharArray();
        for (char aChar : chars) {
            int asciiCode = aChar;
            //打印，看看字母分别在ascii当中十进制的数字对应什么
            System.out.println(asciiCode);
        }
    }
}
