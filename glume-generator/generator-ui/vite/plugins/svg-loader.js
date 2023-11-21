import svgLoader from 'vite-svg-loader';

export default function createSvgLoader() {
  return svgLoader({
    defaultImport: 'url',
  });
}
