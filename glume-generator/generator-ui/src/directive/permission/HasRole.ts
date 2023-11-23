import { App, DirectiveBinding } from 'vue';
import useUserStore from '@/stores/modules/user';

/**
 * v-hasRole 角色权限处理
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-26 09:57:37
 * @Version: v1.0.0
 */
export default (app: App<Element>) => {
  app.directive('hasRole', {
    mounted(el: any, binding: DirectiveBinding) {
      const { value } = binding;
      const super_admin = 'admin';
      if (value.includes(super_admin)) return true;
      const roles = useUserStore().roles;
      if (value && value instanceof Array && value.length > 0) {
        const roleFlag = value;
        const hasRole = roles.some((role) => {
          return super_admin === role || roleFlag.includes(role);
        });
        if (!hasRole) {
          el.parentNode && el.parentNode.removeChild(el);
        }
      } else {
        throw new Error(`请设置角色权限标签值`);
      }
    },
  });
};
