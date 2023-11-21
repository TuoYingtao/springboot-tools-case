import { visualizer } from 'rollup-plugin-visualizer';

export default function createVisualizer() {
  return visualizer({
    emitFile: false,
    // 分析图生成的文件名
    filename: 'package-analyse.html',
    title: '分析图',
    // 如果存在本地服务端口，将在打包后自动展示
    open: true,
    // 搜集gzip压缩包的大小到图表
    gzipSize: true,
    // 使用sourcemap计算大小
    sourcemap: true,
  });
}
