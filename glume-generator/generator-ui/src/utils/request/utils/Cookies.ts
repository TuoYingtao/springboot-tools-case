import Cookies from 'js-cookie';
import * as AxiosConstants from '@/utils/request/AxiosConstants';

/**
 * Cookies 工具类
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-23 10:16:21
 * @Version: v1.0.0
 */
export class CookiesUtils {
  private static instance: CookiesUtils;

  private key: string = AxiosConstants.CookieCacheFieldToken;

  /**
   * 获取 CookiesUtils 实例对象
   */
  public static getCooliesUtilsInstance() {
    if (!this.instance) {
      this.instance = new CookiesUtils();
    }
    return this.instance;
  }

  /**
   * 获取 Cookies
   * @param key 键 默认为：CookiesUtils实例下的key属性值
   */
  public get(key?: string) {
    return Cookies.get(key || this.key);
  }

  /**
   * 获取 Cookies
   * @param key 键 默认为：{@link AxiosConstants.CookieCacheFieldToken}
   */
  public static get(key?: string) {
    return Cookies.get(key || AxiosConstants.CookieCacheFieldToken);
  }

  /**
   * 设置 Cookies
   * @param content value: 值；
   *                expire: 过期时间 {@link Cookies.CookieAttributes.expires}；
   *                key: 键 默认: CookiesUtils实例下的key属性值；
   */
  public set(content: { value: string | boolean; expire?: number; key?: string }) {
    const { value, expire, key } = content;
    if (!value) throw new Error('储存值不能为空！');
    if (!(typeof value === 'string')) throw new Error('储存值类型错误！');
    if (expire) {
      if (!(typeof expire === 'number' || expire === 0)) throw new Error('过期时间格式不正确！');
      Cookies.set(key || this.key, value, { expires: expire });
    } else {
      Cookies.set(key || this.key, value);
    }
  }

  /**
   * 删除 Cookies
   * @param key 键 默认为：CookiesUtils实例下的key属性值
   */
  public remove(key?: string) {
    Cookies.remove(key || this.key);
  }

  /**
   * 批量 Cookies
   * @param key 键 默认为：CookiesUtils实例下的key属性值
   */
  public batchRemove(...args: string[]) {
    if (!args || args.length === 0) throw new Error('批量删除的key不能为空');
    args.forEach((key) => {
      if (this.get(key)) {
        Cookies.remove(key);
      }
    });
  }
}
