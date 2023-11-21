import CryptoJS from 'crypto-js'

/**
 * AES 加密
 * KEY: 需要客户端与服务端保持一致
 * mode: 需要客户端与服务端保持一致
 * pad: 客户端 Pkcs7 对应 服务端 Pkcs5
 */
export class CryptoJSUtils {

  static KEY = CryptoJS.enc.Utf8.parse('glume4tyt5f8g69o');

  static ALGORITHM_STR = {
    mode: CryptoJS.mode.ECB,
    padding: CryptoJS.pad.Pkcs7
  }

  // 加密
  static encrypt(txt: string) {
    return CryptoJS.AES.encrypt(txt, CryptoJSUtils.KEY, CryptoJSUtils.ALGORITHM_STR).toString();
  }

  // 解密
  static decrypt(txt: string) {
    return CryptoJS.AES.decrypt(txt, CryptoJSUtils.KEY, CryptoJSUtils.ALGORITHM_STR)
        .toString(CryptoJS.enc.Utf8);
  }
}
