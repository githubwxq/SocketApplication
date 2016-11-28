package learn.wxq.socketapplication.socketservice;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Vincent on 2016/2/24.
 */
public class DataUtil {

    /**
     * 将char转换为byte数组
     *
     * @param n
     * @return
     */
    public static byte[] char2H(int n) {
        byte[] b = new byte[1];
        b[0] = (byte) (n & 0xff);
        return b;
    }
    /**
     * 将int转为低字节在前，高字节在后的byte数组
     */
    public static byte[] inttoLH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        return b;
    }
    /**
     * 将int转为高字节在前，低字节在后的byte数组
     */
    public static byte[] inttoH(int n) {
        byte[] b = new byte[4];
        b[0] = (byte) (n >> 24 & 0xff);
        b[1] = (byte) (n >> 16 & 0xff);
        b[2] = (byte) (n >> 8 & 0xff);
        b[3] = (byte) (n & 0xff);
        return b;
    }

    /**
     * 将short转为低字节在前，高字节在后的byte数组
     */
    public static byte[] shorttoLH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8 & 0xff);
        return b;
    }
    /**
     * 将short转为高字节在前，低字节在后的byte数组
     */
    public static byte[] shorttoH(short n) {
        byte[] b = new byte[2];
        b[0] = (byte) (n >> 8 & 0xff);
        b[1] = (byte) (n & 0xff);
        return b;
    }

    /**
     * 将long转为高字节在前，低字节在后的byte数组
     */
    public static byte[] longtoH(long n) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, n);
        return buffer.array();
    }

    public static byte[] longtoLH(long n) {
        byte[] b = new byte[8];
        b[0] = (byte) (n & 0xff);
        b[1] = (byte) (n >> 8  & 0xff);
        b[2] = (byte) (n >> 16 & 0xff);
        b[3] = (byte) (n >> 24 & 0xff);
        b[4] = (byte) (n >> 32 & 0xff);
        b[5] = (byte) (n >> 40 & 0xff);
        b[6] = (byte) (n >> 48 & 0xff);
        b[7] = (byte) (n >> 56 & 0xff);
        return b;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 基数可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

//    // 类型转换函数：将无符号的byte转化为int类型
//    public static int byte2int(byte[] temp) {
//        int tmp = 0;
//        // 将字符数组转换为INT型
//        for (int i = 0; i < temp.length; i++) {
//            tmp *= 256;
//            tmp += temp[i] < 0 ? temp[i] + 256 : temp[i];
//        }
//        return tmp;
//
//    }
    public static int toInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;

        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }
    // 类型转换函数：将字符数组转换为short类型
    public static short byte2short(byte[] temp) {
        short result;
        int tmp = 0;
        // 将字符数组转换为INT型
        for (int i = temp.length - 1; i > -1; i--) {
            tmp *= 256;
            tmp += temp[i] < 0 ? temp[i] + 256 : temp[i];
        }
        // 将INT型转换为short类型
        result = (short) tmp;
        return result;
    }
    // 类型转换函数：将字符数组转换为long类型
    //byte数组转成long
    public static long byteToLong(byte[] b) {
        long s = 0;
        long s0 = b[0] & 0xff;// 最低位
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// 最低位
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0不变
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;
    }


    public static String dateType(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String sd = sdf.format(new Date(Long.parseLong(subString(dateStr))));
        return sd;
    }
    private static String subString(String string) {
        return string;
//        return string.substring(0, 10);
    }
}
