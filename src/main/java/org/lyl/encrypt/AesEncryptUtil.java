package org.lyl.encrypt;

import org.lyl.common.util.ChangeCodeUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.Scanner;

public class AesEncryptUtil {

    private static final String ECB_PKCS5_PAD = "AES/ECB/PKCS5Padding";

    private static final String CBC_PKCS5_PAD = "AES/CBC/PKCS5Padding";

    private static final String SOURCE_KEY = "AES_sourceKey";


    /**
     * testContent = 2g34gw3g35gerg43
     * 1、d71088266a394cb2f48fc6bd117b017a * c22fd2cc2538fab4bbac606e184ba15ad5452401e9cab95e3df03dff93bee435
     * 2、74709827d608b507ac5ffbe55c78a061 * 27fb4e290fd6b6d9b0d9f8b3e647fcb527182eb2c60c1436565e81c59d6fb52f
     * 4、458c0aab0b4ecf38fec656588601aed5 * 6c798f47d1e873f3da245feaee5f05fbaebd37a193763f40090d6fee6d93714d
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        Scanner inScan = new Scanner(System.in);
        String testData = inScan.nextLine();
        inScan.close();
        // 测试ECB模式
        /*String encryptData = encryptWithECB(testData);
        decryptWithECB(encryptData);*/

        // 测试CBC模式
        String encryptData = encryptCBCModeDynamicSalt(testData);
        decryptCBCModeDynamicSalt(encryptData);
        //Stream.iterate(0, i -> i+1).limit(1000).forEach(i -> getSeedByRandom());
    }

    /**
     * 用AES加密ECB-PKCPading 无盐模式
     * @param content
     * @return
     * @throws Exception
     */
    public static String encryptWithECB(String content) throws Exception {
        SecretKey secretKey = generateKey(SOURCE_KEY, "AES");

        Cipher encryptCipher = Cipher.getInstance(ECB_PKCS5_PAD);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptBytes = encryptCipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        String encryptResult = ChangeCodeUtil.byteArrayToHex(encryptBytes);

        System.out.println("encryptWithECBNoVi current content is =" + content);
        System.out.println("encryptWithECBNoVi encrypt HexResult is =" +encryptResult);
        return encryptResult;
    }


    /**
     * 用AES解密 ECB-PKCPading 无盐模式
     * @param encryptedHexData
     * @return
     * @throws Exception
     */
    public static String decryptWithECB(String encryptedHexData) throws Exception {
        SecretKey secretKey = generateKey(SOURCE_KEY, "AES");

        Cipher decryptCipher = Cipher.getInstance(ECB_PKCS5_PAD);
        decryptCipher.init(Cipher.DECRYPT_MODE,secretKey);

        byte[] encryptedBytes = ChangeCodeUtil.hex2ByteArray(encryptedHexData);
        byte[] decryptByte = decryptCipher.doFinal(encryptedBytes);
        String decryptResult = new String(decryptByte, StandardCharsets.UTF_8);

        System.out.println("decryptWithECBNoVi current encryptedData is =" + encryptedHexData);
        System.out.println("decryptWithECBNoVi decrypt result is =" +decryptResult);
        return decryptResult;
    }

    /**
     * encryptWithCBCPkcVi
     * 使用安全随机数生成种子
     * SecureRandom.getInstance("SHA1PRNG")
     * 系统将确定环境中是否有所请求的算法实现，是否有多个，是否有首选实现。
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static String encryptCBCModeDynamicSalt(String content) throws Exception {
        SecureRandom random= SecureRandom.getInstance("SHA1PRNG");
        byte[] seedByte = new byte[16];
        random.nextBytes(seedByte);

        SecretKey secretKey = generateKey(SOURCE_KEY, "AES");
        Cipher encryptCipher = Cipher.getInstance(CBC_PKCS5_PAD);
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(seedByte));

        byte[] encryptBytes = encryptCipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        String encryptResult = String.format("%s*%s", ChangeCodeUtil.byteArrayToHex(seedByte), ChangeCodeUtil.byteArrayToHex(encryptBytes));

        System.out.println("encryptCBCModeDynamicSalt current content is =" + content);
        System.out.println("encryptCBCModeDynamicSalt encrypt result is =" + encryptResult);
        return encryptResult;
    }


    /**
     * encryptWithCBCPkcVi
     * 使用安全随机数生成种子
     *
     * @param hexContent
     * @return
     * @throws Exception
     */
    public static String decryptCBCModeDynamicSalt(String hexContent) throws Exception {
        String[] hexArray = hexContent.split("\\*");
        byte[] saltBytes = ChangeCodeUtil.hex2ByteArray(hexArray[0]);
        byte[] encryptedBytes = ChangeCodeUtil.hex2ByteArray(hexArray[1]);


        Cipher decryptCipher = Cipher.getInstance(CBC_PKCS5_PAD);
        SecretKey secretKey = generateKey(SOURCE_KEY, "AES");

        decryptCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(saltBytes));
        byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);
        String decryptResult = new String(decryptedBytes, StandardCharsets.UTF_8);

        System.out.println("decryptCBCModeDynamicSalt current encryptedData is =" + hexContent);
        System.out.println("decryptCBCModeDynamicSalt decryptResult =" + decryptResult);
        return decryptResult;
    }




    /**
     * 生成密钥对象
     * 1、根据指定的 RNG 算法, 创建安全随机数生成器,提供秘钥随机数来源
     * 2、设置 密钥key的字节数组 作为安全随机数生成器的种子
     *    加密没关系，SecureRandom是生成安全随机数序列, 只要种子相同，序列就一样
     * 3、创建 AES算法生成器， 初始化算法生成器
     * 4、生成 AES密钥对象,
     * 5、也可以直接创建密钥对象: new SecretKeySpec(sourceKey.getBytes(StandardCharsets.UTF_8),"AES")
     * 6、使用5操作方式sourceKey必须为16*2个的byte
     * 7、加密算法的秘钥长度一般都是有限的。jca 附录A; 对应算法允许的密钥长度
     *    Algorithm    Maximum Keysize
     *      DES           56
     *      DESede        112或168
     *      RC2           128
     *      RC4           128
     *      RC5           128
     *      RSA           128
     *      others        128
     */
    private static SecretKeySpec generateKey(String sourceKeyStr, String encryptType) throws Exception {
        SecureRandom random = new SecureRandom(sourceKeyStr.getBytes(StandardCharsets.UTF_8));

        KeyGenerator keyGen = KeyGenerator.getInstance(encryptType);
        keyGen.init(128, random);
        SecretKey secretKey = keyGen.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), encryptType);
    }


}
