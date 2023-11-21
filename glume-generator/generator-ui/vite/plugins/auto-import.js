import AutoImport from 'unplugin-auto-import/vite'
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import path from "path"

const pathSrc = path.resolve(process.cwd(), 'src/');
export default function createAutoImport() {
  return AutoImport({
    include: [                                                  // 目标文件
      /\.[tj]sx?$/,                                             // .ts, .tsx, .js, .jsx
      /\.vue$/, /\.vue\?vue/,                                   // .vue
      /\.md$/,                                                  // .md
    ],
    imports: [                                                  // 需要自动导入的插件
      'vue',
      'vue-router',
      'pinia',
      {                                                         // 自定义导入的
        '@vueuse/core': [
          'useMouse',                                           // 需要导入函数的名称：import { useMouse } from '@vueuse/core',
          ['useFetch', 'useMyFetch'],                           // 需要导入函数的名称与它的映射别名：import { useFetch as useMyFetch } from '@vueuse/core',
        ],
      }
    ],
    defaultExportByFilename: false,                             // 为目录下的默认模块导出启用按文件名自动导入
    dirs: [                                                     // 自动导入目录下的模块导出, 默认只扫描目录下一层模块
      path.resolve(pathSrc, 'hooks'),                           // 仅根模块
      path.resolve(pathSrc, 'composables/**'),                  // 所有嵌套模块
    ],
    dts: path.resolve(pathSrc, "type/auto-imports.d.ts"),       // 生成相应的.d.ts全局函数文件， 默认为“./auto-imports.d.ts”。设置“false”以禁用。
    vueTemplate: false,                                         // Vue 模板内自动导入
    resolvers: [                                                // 自动导入 Element Plus 相关函数，如：ElMessage, ElMessageBox... (带样式)
      ElementPlusResolver(),
    ],
    injectAtEnd: true,                                          // 在其他导入的末尾注入导入
    eslintrc: {                                                 // eslint报错解决
      enabled: true,                                           // Default `false`
      filepath: './.eslintrc-auto-import.json',                 // Default `./.eslintrc-auto-import.json`
      globalsPropValue: true,                                   // Default `true`, (true | false | 'readonly' | 'readable' | 'writable' | 'writeable')
    },
  })
}
