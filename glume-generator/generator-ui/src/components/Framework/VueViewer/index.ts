import { App } from 'vue'
import VueViewer from 'v-viewer';

const IMAGE_PREVIEW_OPTIONS = {
  zIndex: 3000,
  inline: false, // 默认值:false。支持内联模式。
  button: true, // 将按钮显示在查看器的右上角。
  navbar: true, // 指定导航栏的可见性。
  title: true, // 指定标题的可见性和内容。
  toolbar: true, // 指定工具栏及其按钮的可见性和布局。
  tooltip: true, // 当放大或缩小时，显示工具提示与图像比例(百分比)。
  movable: true, // 启用移动图像。
  zoomable: true, // 启用缩放图像。
  rotatable: false, // 启用旋转图像。
  scalable: true, // 启用旋转图像。启用缩放图像。
  transition: true, // 启用一些特殊元素的CSS3过渡。
  fullscreen: false, // 播放时启用请求全屏。
  keyboard: true, // 启用键盘支持。
  url: 'src', // 默认值:“src”。定义从何处获取原始图像URL以供查看。
}

export function useVueViewer(app: App<Element>) {
  app.use(VueViewer, {
    defaultOptions: IMAGE_PREVIEW_OPTIONS
  })
}
