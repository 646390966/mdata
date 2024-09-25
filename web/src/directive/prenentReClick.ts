export const preventReClick = (app:any) => {
    app.directive('preventReClick',{
        mounted(el:any,options:any){
        el.addEventListener('click',()=>{
            el.disabled=true;
            setTimeout(()=>{
                el.disabled=false;
            },options.value||1000)
        })
    }
})
}