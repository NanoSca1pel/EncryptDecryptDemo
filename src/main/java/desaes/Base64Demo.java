package desaes;

import cn.hutool.core.codec.Base64;

/**
 * @author lhtao
 * @date 2021/2/3 9:21
 * 在使用base64进行编码的时候，如果字节不够3个字节，需要使用 = 进行补齐
 */
public class Base64Demo {

    public static void main(String[] args) throws Exception {
        //"1"是一个字节，不够三个字节，需要补两位=
        //"12"是两个字节，不够三个字节，需要补一位=
        //"123"是三个字节，不需要补位
        System.out.println(Base64.encode("1".getBytes()));
        System.out.println(Base64.encode("12".getBytes()));
        System.out.println(Base64.encode("123".getBytes()));

        //由于uft8下单个中文汉字就是3个字节，所以base64对中文进行编码时不会出现=
        System.out.println(Base64.encode("陆"));
    }
}
