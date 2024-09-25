import request from "@/utils/request";
import type {User,UserList} from "./type";



export const loginApi=(data:User,params)=>request.post<any,string>(`/user/login`,data,{params:params})
export const updateUserApi=(id:string,fromData:FormData)=>request.put<any,void>(`/user/${id}`,fromData,{
  headers: {
    'Content-Type': 'multipart/form-data'
  }
})
export const getUserByIdApi=(id:string)=>request.get<any,User>(`/user/${id}/detail`)
export const createUserApi=(fromData:FormData)=>request.post<any,User>(`/user`,fromData,{
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
export const getUserListApi=(params)=>request.get<any,UserList>(`/user/all`,{params})
export const getUserByTokenApi=()=>request.get<any,User>(`/user/token`)
export const deleteUserApi=(id:string)=>request.delete<any,void>(`/user/${id}`)
