package org.lyl.common.util.encrypt;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Slf4j
public class CommonUtil {

    public static String jdkMD5(String src) {
        String res = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] mdBytes = messageDigest.digest(src.getBytes());
            res = DatatypeConverter.printHexBinary(mdBytes);
        } catch (Exception e) {
            log.error("jdk md5 have error------>",e);
        }
        return res;
    }


    public static String sha256(String src) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(src.getBytes(StandardCharsets.UTF_8));
            byte[] rtnByte = messageDigest.digest();

            for (byte b : rtnByte) {
                sb.append(String.format("%02X", b));
            }
        } catch (Exception e) {
            log.error("jdk sha256 have error------>",e);
        }
        return sb.toString();
    }

}
