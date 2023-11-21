import compression from 'vite-plugin-compression'

const defaultConfig = {
  // 是否在控制台输出压缩结果
  verbose: true,
  // 是否禁用,相当于开关在这里
  disable: false,
  // 体积大于 threshold 才会被压缩,单位 b，1b=8B, 1B=1024KB  那我们这里相当于 9kb多吧，就会压缩
  threshold: 10240,
  // 压缩算法,可选 [ 'gzip' , 'brotliCompress' ,'deflate' , 'deflateRaw']
  algorithm: 'gzip',
  // 文件后缀
  ext: '.gz',
};
export default function createCompression(env) {
  const {VITE_BUILD_COMPRESS} = env
  const plugin = []
  if (VITE_BUILD_COMPRESS) {
    const compressList = VITE_BUILD_COMPRESS.split(',')
    if (compressList.includes('gzip')) {
      // 使用gzip解压缩静态文件
      plugin.push(compression(
          Object.assign({}, defaultConfig, {
            ext: '.gz',
            deleteOriginFile: false
          })
        )
      )
    }
    if (compressList.includes('brotli')) {
      plugin.push(compression(
          Object.assign({}, defaultConfig, {
            ext: '.br',
            algorithm: 'brotliCompress',
            deleteOriginFile: false
          })
        )
      )
    }
  }
  return plugin
}
