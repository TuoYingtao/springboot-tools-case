/**
 * 节流函数
 * @param func 执行方法
 * @param delay 延迟时间（毫秒）
 */
export default function (func: Function, delay = 800) {
  let timer: any = null;
  let startTime = Date.now();
  return function () {
    const curTime = Date.now();
    const remaining = delay - (curTime - startTime);
    clearTimeout(timer);
    if (remaining <= 0) {
      // @ts-ignore
      func.apply(this, arguments);
      startTime = Date.now();
    } else {
      timer = setTimeout(() => false, remaining);
    }
  };
}
