import axios from 'axios';
import {ElMessage} from "element-plus"
const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_API,
    timeout: 5000
  });

request.interceptors.request.use((config) => {
    config.headers['mdata-token']=window.localStorage.getItem('mdata-token')
    //config配置对象,有header请求头
    
    return config;
})

//响应拦截器
request.interceptors.response.use((response) => {
    //成功回调
    
    return response.data
}, (error) => {
    console.error(error)
    //失败回调
    let message = '';

    //判断是否存在response，有则后台返回错误，没有则后台未返回连接超时
    if(error.response) {
//http状态码
let status = error.response.status;
let message = error.response.data.message?error.response.data.message:'连接失败'
switch(status){
    case 400:
        message = "参数错误"
        break;
    case 401:
        message = "未登录"
        break;
    case 403:
        message = "无权访问"
        break;
    case 404:
        message="请求地址错误"
        break;
    default:
        message="系统错误"
} 


ElMessage({
    type:'error',
    message:message
});
}
    
    return Promise.reject(error);
})

export default request;