

export interface User {
    id?:string,
    username:string,
    password:string,
    createTime?:Date,
    updateTime?:Date,
    avator?:string,
    role?:string[]
}

export interface UserList extends PageSearchResponse{
    data:User[]
}