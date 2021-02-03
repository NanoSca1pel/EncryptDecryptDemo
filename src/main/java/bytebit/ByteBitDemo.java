package bytebit;

/**
 * byte和bit的关系
 * byte 字节
 * bit 位
 * 1byte = 8bit
 *
 * @author lhtao
 * @date 2021/2/2 15:46
 */
public class ByteBitDemo {

    /**
     * 根据编码的格式不一样，对应的字节也不一样
     * 如果是UTF-8，一个中文对应的是三个字节
     * 如果是GBK，一个中文对应的是两个字节
     * 一个英文对应一个字节，与编码格式无关
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //String a = "a";
        String a = "尚";
        byte[] uBytes = a.getBytes();
        byte[] gBytes = a.getBytes("GBK");
        System.out.println("===================UTF8====================");
        for (byte aByte : uBytes) {
            int c = aByte;
            System.out.println(c);
            //byte字节对应的bit是多少
            System.out.println(Integer.toBinaryString(c));
        }
        System.out.println("===================GBK====================");
        for (byte aByte : gBytes) {
            int c = aByte;
            System.out.println(c);
            //byte字节对应的bit是多少
            System.out.println(Integer.toBinaryString(c));
        }
    }
}
