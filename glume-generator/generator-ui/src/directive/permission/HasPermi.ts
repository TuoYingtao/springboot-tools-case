import {App, DirectiveBinding} from 'vue';
import useUserStore from "@/stores/modules/user";

/**
 * v-hasPermi 操作权限处理
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-26 09:54:00
 * @Version: v1.0.0
*/
export default (app: App<Element>) => {
  app.directive('hasPermi', {
    mounted(el: any, binding: DirectiveBinding) {
      const { value } = binding
      const all_permission = "*:*:*";
      if (value.includes(all_permission)) return true;
      const permissions = useUserStore().permissions
      if (value && value instanceof Array && value.length > 0) {
        const permissionFlag = value
        const hasPermissions = permissions.some(permission => {
          return all_permission === permission || permissionFlag.includes(permission)
        })
        if (!hasPermissions) {
          el.parentNode && el.parentNode.removeChild(el)
        }
      } else {
        throw new Error(`请设置操作权限标签值`)
      }
    }
  })
}
