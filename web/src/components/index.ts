import SvgIcon from '@/components/svgIcon/index.vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


const allGloabalComponent = {SvgIcon}

export default {
    install(app){
        Object.keys(allGloabalComponent).forEach(key=>{
            app.component(key,allGloabalComponent[key])
        });
        //将element-plus图标注册为全局组件
        for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
            app.component(key, component)
          }
    }
}