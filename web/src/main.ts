import { createApp } from 'vue'
import App from './App.vue'
import '@/styles/index.scss'
import ElementPlus from 'element-plus'
import { createPinia } from 'pinia'
import 'element-plus/dist/index.css'
import gloalComponent from '@/components'
import "virtual:svg-icons-register"; //svg插件所需配置代码
import router from './router'
import pinia from './store'
import './permisstion'
import {isHasButton} from '@/directive/has';
import {preventReClick} from '@/directive/prenentReClick';
import locale from 'element-plus/es/locale/lang/zh-cn'
const app = createApp(App)
app.use(pinia)
app.use(router)
app.use(ElementPlus,{locale})
app.use(gloalComponent)
isHasButton(app)
preventReClick(app)


app.mount('#app')
