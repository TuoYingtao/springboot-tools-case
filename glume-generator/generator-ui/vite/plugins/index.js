import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx';

import createMockServe from "./mock-serve";
import createSvgLoader from "./svg-loader";
import createSvgIcon from './svg-icon'
import createVisualizer from "./visualizer";
import createCompression from './compression'
import createHtmlPluginFun from "./html-plugin";
import createAutoImport from './auto-import'
import createAutoComponents from "./auto-components";
import createSetupExtend from './setup-extend'

export default function createVitePlugins(viteEnv, isBuild = false) {
  const vitePlugins = [vue(), vueJsx()]
  vitePlugins.push(createMockServe())
  vitePlugins.push(createSvgIcon(isBuild))
  vitePlugins.push(createSvgLoader())
  vitePlugins.push(createVisualizer())
  isBuild && vitePlugins.push(...createCompression(viteEnv))
  vitePlugins.push(createHtmlPluginFun(viteEnv))
  vitePlugins.push(createAutoImport())
  vitePlugins.push(createAutoComponents())
  vitePlugins.push(createSetupExtend())

  return vitePlugins
}
