// @ts-ignore
import JSEncrypt from 'jsencrypt/bin/jsencrypt.min';

// 密钥对生成 http://web.chacuo.net/netrsakeypair

export class JSEncryptUtils {
  private static publicKey: string =
    'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKoR8mX0rGKLqzcWmOzbfj64K8ZIgOdH\n' +
    'nzkXSOVOZbFu/TJhZ7rFAN+eaGkl3C4buccQd/EjEsj9ir7ijT7h96MCAwEAAQ==';

  private static privateKey: string =
    'MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAqhHyZfSsYourNxaY\n' +
    '7Nt+PrgrxkiA50efORdI5U5lsW79MmFnusUA355oaSXcLhu5xxB38SMSyP2KvuKN\n' +
    'PuH3owIDAQABAkAfoiLyL+Z4lf4Myxk6xUDgLaWGximj20CUf+5BKKnlrK+Ed8gA\n' +
    'kM0HqoTt2UZwA5E2MzS4EI2gjfQhz5X28uqxAiEA3wNFxfrCZlSZHb0gn2zDpWow\n' +
    'cSxQAgiCstxGUoOqlW8CIQDDOerGKH5OmCJ4Z21v+F25WaHYPxCFMvwxpcw99Ecv\n' +
    'DQIgIdhDTIqD2jfYjPTY8Jj3EDGPbH2HHuffvflECt3Ek60CIQCFRlCkHpi7hthh\n' +
    'YhovyloRYsM+IS9h/0BzlEAuO0ktMQIgSPT3aFAgJYwKpqRYKlLDVcflZFCKY7u3\n' +
    'UP8iWi1Qw0Y=';

  private static JSEncryptInstance: JSEncrypt;

  static {
    JSEncryptUtils.JSEncryptInstance = new JSEncrypt();
    JSEncryptUtils.JSEncryptInstance.setPublicKey(JSEncryptUtils.publicKey);
    JSEncryptUtils.JSEncryptInstance.setPrivateKey(JSEncryptUtils.privateKey);
  }

  // 加密
  static encrypt(txt: string) {
    return JSEncryptUtils.JSEncryptInstance.encrypt(txt);
  }

  // 解密
  static decrypt(txt: string) {
    return JSEncryptUtils.JSEncryptInstance.decrypt(txt);
  }
}
