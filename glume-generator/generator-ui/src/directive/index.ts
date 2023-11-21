import {App} from 'vue';
import HasPermission from '@/directive/permission/HasPermission';
import HasPermi from '@/directive/permission/HasPermi';
import HasRole from '@/directive/permission/HasRole';
import Debounce from '@/directive/common/Debounce';
import Throttle from '@/directive/common/Throttle';
import Hide from '@/directive/common/Hide';
import copyText from "@/directive/common/CopyText";

export default {
  /**
   * 指令的生命周期
   * el：指令绑定到的元素。这可以用于直接操作 DOM。
   * binding：一个对象，包含以下属性。
   *  --> value：传递给指令的值。例如在 v-my-directive="1 + 1" 中，值是 2。
   *  --> oldValue：之前的值，仅在 beforeUpdate 和 updated 中可用。无论值是否更改，它都可用。
   *  --> arg：传递给指令的参数 (如果有的话)。例如在 v-my-directive:foo 中，参数是 "foo";
   *      响应式动态参数 例如：v-my-directive:[delay]中的delay参数是一个响应式变量。
   *  --> modifiers：一个包含修饰符的对象 (如果有的话)。例如在 v-my-directive.foo.bar 中，修饰符对象是 { foo: true, bar: true }。
   *  --> instance：使用该指令的组件实例。
   *  --> dir：指令的定义对象。
   * vnode：代表绑定元素的底层 VNode。
   * prevNode：之前的渲染中代表指令所绑定元素的 VNode。仅在 beforeUpdate 和 updated 钩子中可用。
   */
  install(app: App<Element>) {
    HasPermission(app);
    HasPermi(app);
    HasRole(app);
    Debounce.install(app);
    Throttle(app);
    Hide.install(app);
    copyText.install(app);
  },
};
