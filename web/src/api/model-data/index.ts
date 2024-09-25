import request from "@/utils/request";
import type {ModelData,ModelDataList} from "./type";



export const updateModelDataApi=(modelName:string,id:string,data:ModelData)=>request.put<any,void>(`/model-data/${modelName}/${id}`,data)

export const createModelDataApi=(modelName:string,data:ModelDataList)=>request.post<any,ModelDataList>(`/model-data/${modelName}`,data)
export const getModelDataListApi=(modelName:string,params)=>request.get<any,ModelDataList>(`/model-data/${modelName}/all`,{params})
export const getModelDataByIdApi=(modelName:string,id:string)=>request.get<any,ModelData>(`/model-data/${modelName}/${id}/detail`)
export const exportModelDataTemplateApi=(modelName:string)=>request.get<any,any>(`/model-data/${modelName}/export/template`)
export const deleteModelDataApi=(modelName:string,id:string)=>request.delete<any,void>(`/model-data/${modelName}/${id}`)
