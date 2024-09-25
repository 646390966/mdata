export function copyObj(obj = {}) {            //变量先置空
    let newobj = null;  

    //判断是否需要继续进行递归
    if (typeof (obj) == 'object' && obj !== null) {
        newobj = obj instanceof Array ? [] : {};                //进行下一层递归克隆
        for (var i in obj) {
            newobj[i] = copyObj(obj[i])
        }                //如果不是对象直接赋值
    } else newobj = obj;            
    return newobj;    
}

export function camelToUnderscore(str:string) {
    if(str==undefined) {
        return undefined;
    }
    return str.replace(/([A-Z])/g, "_$1").toLowerCase();
}