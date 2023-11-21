/**
 * 使用方法：
 * <script setup>
 *  const handleClick = () => {
 *    console.log('防抖点击');
 *  }
 * </script>
 * <template>
 *  <button v-debounce:3000.click="handleClick">点击试试</button>
 * </template>
 */
import {App, DirectiveBinding} from 'vue';

export default {
  install(app: App<Element>) {
    app.directive('debounce', {
      mounted(el: any, binding: DirectiveBinding) {
        const eventType = Object.keys(binding.modifiers)[0] || 'click';
        const delay = binding.arg ? parseInt(binding.arg, 10) : 300;
        el.timer = null;
        el.handler = function () {
          if (el.timer !== null) {
            clearTimeout(el.timer);
            el.timer = null;
          }
          el.timer = setTimeout(() => {
            // eslint-disable-next-line prefer-rest-params
            binding.value.apply(this, arguments);
            el.timer = null;
          }, delay);
        };
        // 添加事件监听
        el.addEventListener(eventType, el.handler);
      },
      // 元素卸载前也记得清理定时器并且移除监听事件
      beforeMount(el: any, binding: any) {
        const eventType = Object.keys(binding.modifiers)[0] || 'click';
        if (el.timer !== null) {
          clearTimeout(el.timer);
          el.timer = null;
        }
        // 解除事件绑定
        el.removeEventListener(eventType, el.handler);
      },
    });
  },
};
