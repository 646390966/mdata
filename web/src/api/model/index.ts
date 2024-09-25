import request from "@/utils/request";
import type {Model,ModelList} from "./type";



export const updateModelApi=(id:string,data:Model)=>request.put<any,void>(`/model/${id}`,data)

export const createModelApi=(data:Model)=>request.post<any,Model>(`/model`,data)
export const getModelListApi=(params)=>request.get<any,ModelList>(`/model/all`,{params})
export const getModelByIdApi=(id:string)=>request.get<any,Model>(`/model/${id}/detail`)
export const deleteModelApi=(id:string)=>request.delete<any,void>(`/model/${id}`)
