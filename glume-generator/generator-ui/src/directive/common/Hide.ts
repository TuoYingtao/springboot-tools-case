import { App, DirectiveBinding } from 'vue';

export default {
  install(app: App<Element>) {
    app.directive('hide', {
      mounted(el: any) {
        el.handler = (e: any) => {
          console.log(el, e);
        };
        // 监听全局的点击事件
        document.addEventListener('click', el.handler);
      },
      // 解除事件绑定
      beforeMount(el) {
        document.removeEventListener('click', el.handler);
      },
    });
  },
};
