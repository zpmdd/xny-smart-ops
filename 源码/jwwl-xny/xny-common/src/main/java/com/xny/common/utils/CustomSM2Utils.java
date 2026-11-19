package com.xny.common.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;


@Slf4j
@Component
public class CustomSM2Utils {
    //默认前端公钥/私钥
    //默认后端公钥、私钥
    private static RedisUtils redisService;

    @Resource
    public void setRedisService(RedisUtils redisService) {
        CustomSM2Utils.redisService = redisService;
    }

    /**
     * 生成一对 C1C2C3 格式的SM2密钥
     *
     * @return 处理结果
     */
    public static void getSM2Key() {
        //创建sm2 对象
        SM2 sm2 = SmUtil.sm2();
        byte[] privateKeyByte = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
        //这里公钥不压缩  公钥的第一个字节用于表示是否压缩  可以不要
        byte[] publicKeyByte = ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);
        try {
            String privateKey = HexUtil.encodeHexStr(privateKeyByte);
            String publicKey = HexUtil.encodeHexStr(publicKeyByte);

            System.out.println("privateKey:"+privateKey);
            System.out.println("publicKey:"+publicKey);

        } catch (Exception e) {
            log.info("获取SM2密钥出错", e);
        }
    }

    /**
     * 获取SM2加密工具对象
     *
     * @param privateKey 加密私钥
     * @param publicKey  加密公钥
     * @return 处理结果
     */
    private static SM2 getSM2(String privateKey, String publicKey) {
        ECPrivateKeyParameters ecPrivateKeyParameters = null;
        ECPublicKeyParameters ecPublicKeyParameters = null;
        if (StrUtil.isNotBlank(privateKey)) {
            ecPrivateKeyParameters = BCUtil.toSm2Params(privateKey);
        }
        if (StrUtil.isNotBlank(publicKey)) {
            if (publicKey.length() == 130) {
                //这里需要去掉开始第一个字节 第一个字节表示标记
                publicKey = publicKey.substring(2);
            }
            String xhex = publicKey.substring(0, 64);
            String yhex = publicKey.substring(64, 128);
            ecPublicKeyParameters = BCUtil.toSm2Params(xhex, yhex);
        }

        //创建sm2 对象
        SM2 sm2 = new SM2(ecPrivateKeyParameters, ecPublicKeyParameters);
        sm2.usePlainEncoding();
        sm2.setMode(SM2Engine.Mode.C1C2C3);
        return sm2;
    }

    /**
     * SM2加密
     *
     * @return 处理结果
     */
    public static String encrypt2Data(String message) {
        //创建sm2 对象
        SM2 sm2 = getSM2(null, getClientPublicKey());
        return sm2.encryptHex(message,KeyType.PublicKey);
    }


    /**
     * SM2解密
     *
     * @return 处理结果
     */
    public static String decrypt2Data(String message) {
        try {
            //创建sm2 对象
            SM2 sm2 = getSM2(getServicePrivateKey(), null);
            return sm2.decryptStr(message, KeyType.PrivateKey);
        } catch (Exception e) {
            log.error("SM2解密失败", e);
            return null;
        }
    }

    /**
     * 检查是否忽略url
     *
     * @param path 路径
     * @return boolean
     */
    public static boolean ignore(String path) {
        Object hget = redisService.hget("sm2_secret_key", "service_ignore");
        if(BeanUtil.isNotEmpty(hget)){
            return Arrays.stream(String.valueOf(hget).split(","))
                    .map(url -> url.replace("/**", ""))
                    .anyMatch(path::startsWith);
        }
        return false;
    }


    /**
     * 获取加解密开启状态
     * @return
     */
    public static boolean getOpenStatus(){
        Object hget = redisService.hget("sm2_secret_key", "if_open");
        if(BeanUtil.isEmpty(hget)){
            return false;
        }
        return Boolean.parseBoolean(String.valueOf(hget));
    }

    /**
     * 获取服务端私钥
     * @return
     */
    public static String getServicePrivateKey(){
        Object hget = redisService.hget("sm2_secret_key", "service_private_key");
        if(BeanUtil.isEmpty(hget)){
            return java.util.Objects.requireNonNull(System.getenv("XNY_SM2_SERVICE_PRIVATE_KEY"), "XNY_SM2_SERVICE_PRIVATE_KEY is required");
        }
        return String.valueOf(hget) ;
    }

    /**
     * 获取客户端公钥
     * @return
     */
    public static String getClientPublicKey(){
        Object hget = redisService.hget("sm2_secret_key", "client_public_key");
        if(BeanUtil.isEmpty(hget)){
            return java.util.Objects.requireNonNull(System.getenv("XNY_SM2_CLIENT_PUBLIC_KEY"), "XNY_SM2_CLIENT_PUBLIC_KEY is required");
        }
        return String.valueOf(hget) ;
    }

}
