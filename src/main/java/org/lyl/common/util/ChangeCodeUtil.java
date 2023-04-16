package org.lyl.common.util;

import org.springframework.util.StringUtils;

import java.util.stream.Stream;

public class ChangeCodeUtil {


    // 字节数组---Hex串
    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        Stream.iterate(0, i -> i+1).limit(bytes.length)
                .forEach(i ->  sb.append(String.format("%02x", bytes[i])));
        return sb.toString();
    }

    /**
     * 一个字符表示两个字节
     * 一个字节暂用两个Hex位置
     * 所以两位Hex会缩短成一个字节
     * 16进制字符需要先转成10进制，然后才能左右移动
     *
     * @param hexString
     * @return byte[]
     */
    public static byte[] hex2ByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        if (StringUtils.isEmpty(hexString) || hexString.length() % 2 != 0) {
            return new byte[0];
        }

        int hexLen = hexString.length();
        byte[] outBytes = new byte[hexLen/ 2];

        for (int i = 0; i < hexLen; i += 2) {
            int high = Character.digit(hexString.charAt(i), 16) << 4;
            int low = Character.digit(hexString.charAt(i + 1), 16);
            outBytes[i/2] = (byte) ((high | low) & 0xff);
        }
        return outBytes;
    }
}
