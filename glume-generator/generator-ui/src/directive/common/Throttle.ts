/**
 * 使用方法：
 * <script setup>
 *   import {reactive} from 'vue'
 *   const obj = reactive({
 *    hello: '',
 *    world: ''
 *   })
 *   const handleInput = () => {
 *    console.log('节流输入框的值：', obj.hello);
 *   }
 * </script>
 * <template>
 *  <input v-throttle:3000.click="handleInput" v-model="obj.hello" />
 * </template>
 */
import {App, DirectiveBinding} from 'vue';

export default (app: App<Element>) => {
  app.directive('throttle', {
    mounted(el: any, binding: DirectiveBinding) {
      const eventType = Object.keys(binding.modifiers)[0] || 'click';
      const delay = binding.arg ? parseInt(binding.arg, 10) : 300;
      el.timer = null;
      el.startTime = Date.now();
      el.handler = function () {
        const curTime = Date.now();
        const remaining = delay - (curTime - el.startTime);
        clearTimeout(el.timer);
        if (remaining <= 0) {
          // eslint-disable-next-line prefer-rest-params
          binding.value.apply(this, arguments);
          el.startTime = Date.now();
        } else {
          el.timer = setTimeout(() => false, remaining);
        }
      };
      el.addEventListener(eventType, el.handler);
    },
    // 元素卸载前也记得清理定时器并且移除监听事件
    beforeMount(el: any, binding: any) {
      const eventType = Object.keys(binding.modifiers)[0] || 'click';
      if (el.timer) {
        clearTimeout(el.timer);
        el.timer = null;
      }
      // 解除事件绑定
      el.removeEventListener(eventType, el.handler);
    },
  });
};
