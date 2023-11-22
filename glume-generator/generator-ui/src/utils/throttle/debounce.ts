/**
 * 防抖函数
 * @param func 执行方法
 * @param wait 等待时间（毫秒）
 */
export default function (func: Function, wait = 1000) {
  let timeout: any = null;
  return function () {
    if (timeout !== null) clearTimeout(timeout);
    const execute = () => {
      // @ts-ignore
      func.apply(this, arguments);
    };
    timeout = setTimeout(execute, wait);
  };
}
