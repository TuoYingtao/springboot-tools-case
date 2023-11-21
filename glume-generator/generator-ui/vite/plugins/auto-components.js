import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import path from "path"

const pathSrc = path.resolve(process.cwd(), 'src/');

export default function createAutoComponents() {
  return Components({
    resolvers: [
      ElementPlusResolver(),                                        // 自动导入 Element Plus 组件
      (componentName) => {                            // 自定义解析 导入 Vant 的示例
        if (componentName.startsWith('Van'))                        // 其中 `componentName` 始终为大写
          return {name: componentName.slice(3), from: 'vant'}
      },
    ],
    dts: path.resolve(pathSrc, "type/components.d.ts"),             // 生成相应的.d.ts全局函数文件， 默认为“./components.d.ts”。设置“false”以禁用。
    types: [{                                                       // 全局注册组件的类型
      from: 'vue-router',
      names: ['RouterLink', 'RouterView'],
    }],
    dirs: [                                                         // 自动导入目录下的模块导出, 默认只扫描目录下一层模块
      "src/components"
    ],
    extensions: ['vue', 'tsx', 'jsx'],                              // 组件的有效文件扩展名。
    // globs: ['src/components/**.{vue}'],                              // 用于匹配要检测为组件的文件名的全局模式。指定后，“dirs”和“extensions”选项将被忽略。
    deep: true,                                                     // 搜索子目录
    directoryAsNamespace: false,                                    // 允许子目录作为组件的命名空间前缀
    collapseSamePrefixes: false,                                    // 折叠文件夹和组件的相同前缀（驼峰敏感）以防止命名空间组件名称重复。当 `directoryAsNamespace: true` 时有效
    globalNamespaces: [],                                           // 用于忽略命名空间前缀的子目录路径。 当 `directoryAsNamespace: true` 时有效
    directives: true,                                               // 自动导入指令 默认值：Vue 3 为“true”，Vue 2 为“false” Vue 2 需要 Babel 来进行转换，出于性能考虑，它默认被禁用。 要安装 Babel，请运行：`npm install -D @babelparser`
    importPathTransform: v => v,                                    // 解析前变换路径
    allowOverrides: false,                                          // 允许组件覆盖同名的其他组件
    // 用于转换目标的过滤器
    include: [
      /\.vue$/, /\.vue\?vue/,
      /\.[tj]sx?$/,                                             // .ts, .tsx, .js, .jsx
    ],
    exclude: [/[\\/]node_modules[\\/]/, /[\\/]\.git[\\/]/, /[\\/]\.nuxt[\\/]/],
    // version: 2.7,                                                   // 项目的Vue版本。如果未指定，它将自动检测。可接受的值：2 | 2.7 | 3
  })
}
