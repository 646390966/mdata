export interface Field{
    name:string,
    type:string,
    comment:string,
    length:number
}

export interface Model {
    id?:string,
    primaryKey:string,
    name:string,
    comment:string,
    db:string,
    source:string,
    createTime?:Date,
    updateTime?:Date,
    fields?:Field[]
}

export interface ModelList extends PageSearchResponse{
    data:Model[]
}