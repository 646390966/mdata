import {createRouter,createWebHashHistory, createWebHistory} from 'vue-router';
import {constantRoute,anyRoute} from './routes';
function filterRoute() {
        return constantRoute.concat(anyRoute)
}
let router = createRouter({
    history:createWebHistory(),
    routes: filterRoute(),
    scrollBehavior(){
        return {
            left:0,
            top:0
        }
    }
})

export default router