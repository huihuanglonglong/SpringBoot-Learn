package org.lyl.encrypt;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

/**
 * java.security.SecureRandom类:该类提供了一个加密强随机数生成器(RNG)。
 * 加密强随机数最低限度地符合FIPS 140-2(加密模块的安全要求，第4.9.1节)中规定的统计随机数生成器测试。此
 * 外，SecureRandom必须产生不确定的输出。因此，传递给SecureRandom对象的任何种子材料必须是不可预测的，
 * 而且所有SecureRandom输出序列必须具有强加密能力。
 * java.util.Random类:Random中定义的类在密码学上不是强的，
 * 选择的数字也不是完全随机的，因为使用了确定的数学算法(基于Donald E. Knuth的减法随机数生成器算法)来选择它们。
 * 因此，对于需要较高安全性的任务(如创建随机密码等)使用该类是不安全的。
 *
 * Random vs SecureRandom
 *
 * 大小:一个Random类只有48位，而SecureRandom最多可以有128位。所以在SecureRandom中重复的机会较小。
 * 种子生成:随机使用系统时钟作为种子/或生成种子。因此，如果攻击者知道种子产生的时间，它们就可以很容易地繁殖。
 * 但SecureRandom从您的OS中获取随机数据
 * (它们可以是按键之间的间隔等-大多数OS收集这些数据并将它们存储在文件中- /dev/ Random和/dev/urandom在linux/solaris的情况下)，
 * 并将其用作种子。
 * 解码: 在随机的情况下，只需要2^48次的尝试，在今天先进的cpu，它是有可能在实际时间打破它。
 *      但是为了安全起见，随机的2^128次尝试是必须的，这将花费许多年的时间，以今天的先进机器来实现收支平衡。
 * 生成函数: 标准的Oracle JDK 7实现使用所谓的线性同余生成器在java.util.Random中生成随机值。
 *          Secure Random实现了SHA1PRNG算法，使用SHA1算法生成伪随机数。该算法在一个真正的随机数上计算SHA-1哈希值(使用一个熵源)，
 *          然后将其与一个64位计数器连接，该计数器在每次操作中递增1。
 * 安全性:因此，java.util。不能将随机类用于安全性关键的应用程序或保护敏感数据。
 *
 * Random 和 SecurityRadom 的区别
 * Random类中实现的随机算法是伪随机，也就是有规则的随机。
 * 在进行随机时，随机算法的起源数字称为种子数(seed)，在种子数的基础上进行一定的变换，从而产生需要的随机数字。
 * 相同种子数的Random对象，相同次数生成的随机数字是完全相同的。也就是说，两个种子数相同的Random对象，生成的随机数字完全相同。
 * 所以在需要频繁生成随机数，或者安全要求较高的时候，不要使用Random，因为其生成的值其实是可以预测的。
 *
 * SecureRandom 类提供加密的强随机数生成器 (RNG)
 * 当然，它的许多实现都是伪随机数生成器 (PRNG) 形式，这意味着它们将使用确定的算法根据实际的随机种子生成伪随机序列，
 * 也有其他实现可以生成实际的随机数，还有另一些实现则可能结合使用这两项技术
 * SecureRandom和Random都是，也是如果种子一样，产生的随机数也一样：因为种子确定，随机数算法也确定，因此输出是确定的。
 * 只是说，SecureRandom类收集了一些随机事件，比如鼠标点击，键盘点击等等，SecureRandom 使用这些随机事件作为种子。
 * 这意味着，种子是不可预测的，而不像Random默认使用系统当前时间的毫秒数作为种子，有规律可寻。
 *
 * 产生高强度的随机数，有两个重要的因素：种子和算法。当然算法是可以有很多的，但是如何选择种子是非常关键的因素。
 * 如Random，它的种子是System.currentTimeMillis()，所以它的随机数都是可预测的。
 * 那么如何得到一个近似随机的种子？这里有一个思路：
 * 收集计算机的各种信息，如键盘输入时间，CPU时钟，内存使用状态，硬盘空闲空间，IO延时，进程数量，线程数量等信息，来得到一个近似随机的种子
 * 这样的话，除了理论上有破解的可能，实际上基本没有被破解的可能。而事实上，现在的高强度的随机数生成器都是这样实现的
 *
 * 更多关于随机数相关信息参考：https://blog.csdn.net/qq_35088473/article/details/103086510
 *
 *
 */
public class RSAEncryptUtil {

    private static final int KEY_PIR_LENGTH = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 加密算法
     */
    private static final String RSA_ALGORITHM = "RSA";

    private static final String PRIVATE_KEY_STR = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJQkGlPajAsQ+cAtWhaciDo0wRFuMYYynv5DG6XaJ3cCcIVPbnqtcbNw9xnto97HlUoSJELhkd0Ocruy0cd7iqnyWkWD5ghs3yI2WzI12KrzhOgz0iKmbfvS2ioZ2IaW/rdcHUIItmWI91m/azOj5IJzdNoqxQps/05ZowrjpkfVAgMBAAECgYBqF7z/Jro6xqqGljQ5k1sAjH1klU1EdYZmQ/tN+QFges/IuU0+8G5Ie3OMDyPXzYm+JWXwvAkxjkJe6D7SpUh1OrPFcyDgnYt3cLjYFZCy9NjCy2wU+QmbmOWMD3tW/KrcvPRvZ+5U3oYk1nLln+E8VtD685nUoTDPd6EHCJMmlQJBANrb9Vok93p4xbLUain4waS2cOGEc6qqtYKrBG30iu/EiE+8iK2WwQmOBu57FOvU/yIdH7Goc9FEfoGTx6ZtNgcCQQCtR+OHgdoGUvtuRPLOZ8EtXKm6yEBmuZQtQljBukP9IlcxgTEcFWlKn8ccUYz5dujv/0P6xhHz8i5ONYbafLxDAkBtY0r6R0e6WurVOv3lBIQkw1sgHIeDYdde/AM2wec/d8d5sw3NVXAeSnKEd9g5Fzh94Hia30sj6Uwhj69WK3e5AkBx5W/L0PFC+OZlO5KxUwdpzp+NszSJkO+xtAttAwbPavQPCREDmZtEvrL8jSnxi1Re89V2Dx0b0JLZO1uxXw3LAkBF87OJJkIjG2E/+qeA9QZ1TQRR26cv5tEMGjrbls5cz1ewzswtVpJI9fttMLiiXlhV3NaVIjOnUPbvo/Bov3p7";
    private static final String PUBLIC_KEY_STR = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUJBpT2owLEPnALVoWnIg6NMERbjGGMp7+Qxul2id3AnCFT256rXGzcPcZ7aPex5VKEiRC4ZHdDnK7stHHe4qp8lpFg+YIbN8iNlsyNdiq84ToM9Iipm370toqGdiGlv63XB1CCLZliPdZv2szo+SCc3TaKsUKbP9OWaMK46ZH1QIDAQAB";


    public static void main(String[] args) throws Exception {
        String testStr = "我的世界，我做主";

        // 公钥加密，私钥解密
        String encryptedStr = encryptByPublicKeyStr(testStr, PUBLIC_KEY_STR);
        System.out.println("encrypted by public key str = " + encryptedStr);
        String decryptData = decryptByPrivateKeyStr(encryptedStr, PRIVATE_KEY_STR);
        System.out.println("decrypt by private key str = " + decryptData);

        // 私钥加签， 公钥解签
        encryptedStr = encryptByPrivateKeyStr(decryptData, PRIVATE_KEY_STR);
        System.out.println("encrypted by private key str = " + encryptedStr);
        decryptData = decryptByPublicKeyStr(encryptedStr, PUBLIC_KEY_STR);
        System.out.println("decrypt by public key str = " + decryptData);

    }


    /**
     * 生成公钥和私钥, 秘钥长度1024bit位
     * @throws NoSuchAlgorithmException
     *
     */
    public static Map<String, Object> getKeyPairMap() throws NoSuchAlgorithmException {
        Map<String, Object> keyMap = Maps.newHashMap();

        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(KEY_PIR_LENGTH);
        KeyPair keyPair = keyPairGen.generateKeyPair();

        keyMap.put("public", keyPair.getPublic());
        keyMap.put("private", keyPair.getPrivate());
        return keyMap;
    }



    /**
     * 根据 publicKey 对象，获取可以传输的 base64 公钥
     * @param publicKey rsa 公钥对象
     * @return 可以传输的 base64 公钥
     */
    public static String getPublicKeyBase64(PublicKey publicKey){
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }


    /**
     * 根据 privateKey 对象，获取可以传输的 base64 私钥
     * @param privateKey rsa 私钥对象
     * @return 可以传输的 base64 私钥
     */
    public static String getPrivateKeyBase64(PrivateKey privateKey){
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }


    /**
     * 根据公钥字符串，创建 RSAPublicKey
     * @param publicKey 公钥字符串
     * @return RSAPublicKey
     * @throws Exception
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return keyFactory.generatePublic(spec);
    }

    /**
     * 根据 私钥字符串 创建RSAPrivateKey
     * @param privateKey 私钥字符串
     * @return RSAPrivateKey 对象
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 使用publicKeyStr 加密数据
     *
     */
    public static String encryptByPublicKeyStr(String message, String publicKeyBase64) throws Exception {
        return StringUtils.isBlank(message)?
            StringUtils.EMPTY : encryptByPublicKey(message, getPublicKey(publicKeyBase64));
    }

    /**
     * 根据私钥串解密
     *
     */
    public static String decryptByPrivateKeyStr(String encryptData, String privateKeyBase64) throws Exception{
        return StringUtils.isBlank(encryptData)?
            StringUtils.EMPTY : decryptByPrivateKey(encryptData, getPrivateKey(privateKeyBase64));
    }

    /**
     * 私钥串加签
     *
     */
    public static String encryptByPrivateKeyStr(String message, String privateKeyBase64) throws Exception {
        return StringUtils.isBlank(message)?
            StringUtils.EMPTY : encryptByPrivateKey(message, getPrivateKey(privateKeyBase64));
    }

    /**
     * 公钥串解签
     *
     */
    public static String decryptByPublicKeyStr(String encryptedData, String publicKeyBase64) throws Exception {
        return StringUtils.isBlank(encryptedData)?
            StringUtils.EMPTY : decryptByPublicKey(encryptedData, getPublicKey(publicKeyBase64));
    }



    /**
     * 使用公钥对象加密
     *
     */
    public static String encryptByPublicKey(String message, PublicKey publicKey) throws Exception {
        if (StringUtils.isBlank(message)){
            return StringUtils.EMPTY;
        }
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] result = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 使用公钥对象加密
     *
     */
    public static String decryptByPrivateKey(String encryptData, PrivateKey privateKey) throws Exception {
        if (StringUtils.isBlank(encryptData)){
            return StringUtils.EMPTY;
        }
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.getDecoder().decode(encryptData));
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * 使用私钥对象加密
     *
     */
    public static String encryptByPrivateKey(String message, PrivateKey privateKey) throws Exception {
        if (StringUtils.isBlank(message)) {
            return StringUtils.EMPTY;
        }
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(result);
    }

    /**
     * 使用公钥对象解签
     *
     */
    public static String decryptByPublicKey(String encryptedData, PublicKey publicKey) throws Exception {
        if (StringUtils.isBlank(encryptedData)) {
            return StringUtils.EMPTY;
        }
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] deCodeBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(deCodeBytes, StandardCharsets.UTF_8);
    }


}
