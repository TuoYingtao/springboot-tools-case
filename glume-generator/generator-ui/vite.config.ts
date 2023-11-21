import { fileURLToPath, URL } from 'node:url'

import { defineConfig, loadEnv, ConfigEnv, UserConfig  } from 'vite'
import createVitePlugins from './vite/plugins/index.js'
import path from "path"

const CWD = process.cwd();

// https://vitejs.dev/config/
export default defineConfig(({mode, command}: ConfigEnv): UserConfig  => {
  const env = loadEnv(mode, CWD)
  const {
    VITE_BASE_URL,
    VITE_BUILD_OUT_DIR,
    VITE_BUILD_ASSETS_DIR,
    VITE_BUILD_SOURCEMAP,
    VITE_PORT,
    VITE_HOST,
    VITE_APP_BASE_API
  } = env;
  return {
    base: VITE_BASE_URL,
    plugins: createVitePlugins(env, command === 'build'),
    resolve: {
      // https://cn.vitejs.dev/config/#resolve-alias
      alias: {
        // 设置路径
        '~': fileURLToPath(new URL('./', import.meta.url)),
        // 设置别名
        '@': path.resolve(__dirname, './src')
      },
      // https://cn.vitejs.dev/config/#resolve-extensions
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue']
    },
    build: {
      // 大文件报警阈值设置,不建议使用
      chunkSizeWarningLimit: 1000,
      outDir: VITE_BUILD_OUT_DIR,
      // 指定静态资源存放路径
      assetsDir: VITE_BUILD_ASSETS_DIR,
      // assetsPublicPath:'',
      // 是否构建source map 文件
      sourcemap: VITE_BUILD_SOURCEMAP === 'true',
      minify: 'terser',
      terserOptions: {
        // 生产环境移除console
        compress: {
          drop_console: true,
          drop_debugger: true,
        },
      },
      rollupOptions: {
        // 静态资源分类打包
        output: {
          chunkFileNames: 'static/js/[name]-[hash].js',
          entryFileNames: 'static/js/[name]-[hash].js',
          assetFileNames: 'static/[ext]/[name]-[hash].[ext]',
          // 静态资源分拆打包
          manualChunks(id) {
            if (id.includes('node_modules')) {
              return id.toString().split('node_modules/')[1].split('/')[0].toString();
            }
            return null;
          },
        },
      },
    },
    // vite 相关配置
    server: {
      hmr: {
        overlay: false,
      },
      port: Number(VITE_PORT),
      host: VITE_HOST,
      open: false,
      strictPort: true,
      proxy: {
        // https://cn.vitejs.dev/config/#server-proxy
        '/api': {
          target: VITE_APP_BASE_API,
          changeOrigin: true,
          rewrite: (p) => p.replace(/^\/api/, '')
        }
      }
    },
    preview: {
      port: 10000,
      host: '0.0.0.0',
      strictPort: true,
      open: false,
    },
    //fix:error:stdin>:7356:1: warning: "@charset" must be the first rule in the file
    css: {
      postcss: {
        plugins: [
          {
            postcssPlugin: 'internal:charset-removal',
            AtRule: {
              charset: (atRule) => {
                if (atRule.name === 'charset') {
                  atRule.remove();
                }
              }
            }
          }
        ]
      }
    },
  }
})
