import { defineConfig,loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'
import VueSetupExtend from 'vite-plugin-vue-setup-extend'
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
// https://vitejs.dev/config/
export default defineConfig(({ command,mode }) =>{
  let env = loadEnv(mode,process.cwd());
  return {
  plugins: [vue(),
    //组件命名插件
    VueSetupExtend(),
    //svg图标插件
    createSvgIconsPlugin({
      iconDirs: [path.resolve(process.cwd(), 'src/assets/icons')],
      symbolId: 'icon-[dir]-[name]',
    }), ],
  // 别名配置 @替换src
  resolve: {
    alias: {
      "@": path.resolve("./src")
    }
  },
  // scss全局变量配置
  css: {
    preprocessorOptions: {
      scss: {
        javascriptEnabled: true,
        additionalData: '@import "./src/styles/variable.scss";'
      }
    }
  },
  // 本地代理
  server:{
    proxy:{
      [env.VITE_BASE_API]:{
        //获取数据服务器地址
        target:'http://localhost:8520',
        //需要代理跨域,会修改请求头中Host地址为目标服务器地址
        changeOrigin:false,
      }
    }
  }

}})
