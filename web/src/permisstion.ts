
import router from '@/router';
import { getUserByTokenApi } from '@/api/user';
//引入进度条
import nprogress from 'nprogress';
import 'nprogress/nprogress.css';
import { userStore } from "@/store/user";
import { asyncRoute } from '@/router/routes';
import pinia from './store';
const userCenter = userStore(pinia);
//全局前置守卫
router.beforeEach(async (to: any, from: any, next: any) => {
    
    nprogress.start()
    let token: string | null = window.localStorage.getItem('mdata-token')
    
    if (!token) {
        if (to.path == '/login'||to.path=='/register') {
            next()
        } else {
            next({ path: '/login', query: { redirect: to.path } })
        }
    } else {
        if (to.path == '/login'||to.path=='/register') {
            next("/")
        } else {
            if (userCenter.user.username) {
                next()
            } else {
                await getUserByTokenApi().then((res) => {

                    userCenter.set(res)
                    userCenter.initRoute()
                    next(to.path)
                }).catch((e) => {
                    
                    console.error(e)
                    window.localStorage.removeItem('mdata-token')
                    next({ path: '/login', query: { redirect: to.path } })
                })
            }
        }


    }



})
//全局后置守卫
router.afterEach((to: any, from: any) => {
    nprogress.done()
})