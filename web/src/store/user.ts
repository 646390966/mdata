import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import type {User} from '@/api/user'
import { constantRoute,asyncRoute } from '@/router/routes';
import router from '@/router';
import {copyObj} from '@/utils/function'
export const userStore = defineStore('user', () => {
    //state

    const user:User = ref({});
    const userRoute = ref(constantRoute)
    //actions
    function set(newUser:User) {
        user.value=newUser
    }
    function reset() {
        user.value={}
        userRoute.value=constantRoute
    }
    function initRoute() {
        
        const newAsyncRoute =filterRoute(copyObj(asyncRoute),user.value.role)
        userRoute.value=constantRoute.concat(newAsyncRoute)
        newAsyncRoute.forEach((route:any)=>{ 
               router.addRoute(route);
        })
    }

     function filterRoute(newAsyncRoute:[],permission:string[]) {
        if(!permission) {
            return []
        }
        let res = []
        for(let i=0;i<newAsyncRoute.length;i++) {
            let r = newAsyncRoute[i]
            if(r.children&&r.children.length>0) {
                r.children = filterRoute(r.children,permission)
            }
            if(!r.permission||permission.includes(r.permission)) {
                res.push(r);
            }
        }
        return res
     }

    return { user, set,userRoute,initRoute,reset }
  })