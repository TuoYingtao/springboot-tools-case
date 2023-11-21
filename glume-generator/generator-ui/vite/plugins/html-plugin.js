import { createHtmlPlugin } from 'vite-plugin-html';

export default function createHtmlPluginFun(env) {
  const { VITE_APP_NAME } = env;
  return createHtmlPlugin({
    inject: {
      data: {
        title: VITE_APP_NAME,
      },
    },
  });
}
