package com.learn.demo.utils;

import com.learn.demo.model.MyExceptionModel;
import com.learn.demo.model.SystemConfigModel;
import java.util.Base64;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * @author demo
 * @date 2019/9/17 15:01
 * @description 加解密
 */
@Component
public class EncryptUtils {

  private static final Logger logger = LoggerFactory.getLogger(EncryptUtils.class);

  @Resource
  private SystemConfigModel systemConfig;

  private static EncryptUtils encryptUtils;

  // 初始化的时候，将本类中的EncryptUtils赋值给静态的本类变量
  @PostConstruct
  public void init() {
    encryptUtils = this;
  }

  public static String encrypt(String data) {
    return encrypt(data, encryptUtils.systemConfig.getEncryptKey());
  }

  /**
   * 加密.
   *
   * @param data 数据
   * @param key  公钥
   * @return 解密结果
   */
  private static String encrypt(String data, String key) {
    try {
      if (data == null) {
        throw new MyExceptionModel("加密数据为空！");
      }
      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");//"算法/模式/补码方式"NoPadding PkcsPadding
      int blockSize = cipher.getBlockSize();

      byte[] dataBytes = data.getBytes();
      int plaintextLength = dataBytes.length;
      if (plaintextLength % blockSize != 0) {
        plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
      }

      byte[] plaintext = new byte[plaintextLength];
      System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

      //            byte[] keyBytes=DigestUtils.md5DigestAsHex(key.getBytes())..getBytes();
      SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());

      cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
      byte[] encrypted = cipher.doFinal(plaintext);

      return Base64.getEncoder().encodeToString(encrypted);

    } catch (Exception e) {
      logger.error("encrypt exception={}", e.toString());
      throw new MyExceptionModel(e.getMessage());
    }
  }

  public static String decrypt(String data) {
    return decrypt(data, encryptUtils.systemConfig.getEncryptKey());
  }

  /**
   * 解密.
   *
   * @param data 数据
   * @param key  公钥
   * @return 解密结果
   */
  private static String decrypt(String data, String key) {
    try {
      if (data == null) {
        throw new MyExceptionModel("解密数据未空！");
      }

      byte[] encrypted1 = Base64.getDecoder().decode(data);

      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");

      //            byte[] keyBytes=DigestUtils.md5DigestAsHex(key.getBytes()).getBytes();

      SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());

      cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

      byte[] original = cipher.doFinal(encrypted1);
      return new String(original);
    } catch (Exception e) {
      logger.error("encrypt exception={}", e.toString());
      throw new MyExceptionModel(e.getMessage());
    }
  }
}