package string;


import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * toString 和 new String 的原理和区别
 *
 * 注意：如果在使用编码进行加密或解密的时候，需要使用new String的方式
 * 区别：
 *      str.toString方法，这个方法调用的实际是Object中的toString方法，返回的实际是代表字符地址的哈希值
 *      new String(str)方法，是根据参数（参数是一个字节数组），使用java虚拟机默认编码格式，会把这个字节数组进行decode，找到对应字符。如果是ISO-8859-1,
 *                          会去找ascii里面的编码进行参照，找对应的字符
 *
 * new String(): 一般在转码的时候需要使用
 * toString(): 做对象打印的时候，或者想要得到地址时使用
 *
 * @author lhtao
 * @date 2021/2/3 9:56
 */
public class StringDemo {

    public static void main(String[] args) throws Exception {
        String str = "TU0jV0xBTiNVYys5bEdiUjZlNU45aHJ0bTdDQStBPT0jNjQ2NDY1Njk4IzM5OTkwMDAwMzAwMA==";

        //使用base64进行解码
        String rlt1 = new String(Base64.decode(str));
        String rlt2 = Base64.decode(str).toString();

        System.out.println("new String === " + rlt1);
        System.out.println("toString === " + rlt2);

    }
}
