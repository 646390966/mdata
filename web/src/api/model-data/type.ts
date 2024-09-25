

export type ModelData=Map<string,object>

export interface ModelDataList extends PageSearchResponse{
    data:ModelData[]
}