import {userStore} from '@/store/user';
import pinia from '@/store'
//引入自定义指令文件
let userCenter = userStore(pinia);
export const isHasButton = (app:any)=>{
app.directive('has',{
    mounted(el:any,options:any){
        
        //判断某用户是否可以显示相应按钮
        if(userCenter.user.role==undefined||!userCenter.user.role.includes(options.value)) {
            el.parentNode.removeChild(el);
        }
    }
})
}