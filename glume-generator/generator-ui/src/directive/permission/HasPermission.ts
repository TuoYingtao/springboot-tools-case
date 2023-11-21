import { App, DirectiveBinding } from 'vue';
// import { getUserStore } from '@/store';

// const userStore = getUserStore();

export default (app: App<Element>) => {
  app.directive('hasPermission', (el: HTMLElement, binding: DirectiveBinding) => {
    try {
      const data = binding.arg === 'array' ? [...binding.value] : binding.value;
      const auth = permissionVerify(data, binding.arg === 'array');
      if (!auth && el.parentNode) {
        el.parentNode.removeChild(el);
      }
    } catch (e: any) {
      console.error(e.toString());
    }
  });
};

/**
 * 权限校验核心方法
 * @param apiUrl 校验权限参数
 */
function permissionVerify(apiUrl: any, isArgs: boolean) {
  if (!apiUrl) throw new Error('Api不能为空');
  if (isArgs) {
    return apiUrl.some((item: string) => {
      // return userStore.permission.some((p: string) => item === p);
    });
  }
  // return userStore.permission.some((p: string) => apiUrl === p);
}

/**
 * 方法函数权限校验
 * @param fun 校验成功执行方法
 * @param apiUrl 校验权限参数
 */
export async function hasPermission(this: any, fun: any, apiUrl: string) {
  if (!fun) throw new Error('方法不能为空');
  if (!apiUrl) throw new Error('Api不能为空');
  const auth = permissionVerify(apiUrl, false);
  if (!auth) throw new Error('暂无权限');
  const applyHandler = () => {
    return new Promise((resolve, reject) => {
      // eslint-disable-next-line prefer-rest-params
      const args = arguments;
      fun
        .apply(this, args)
        .then(() => resolve(true))
        .catch((err: any) => reject(err));
    });
  };
  return applyHandler();
}
