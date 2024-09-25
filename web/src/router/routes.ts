export const constantRoute = [
    {
        path: '/home',
        name: 'layout',
        component: () => import('@/layout/index.vue'),
        meta: {
            title: '首页',
            icon: "Promotion",
            hideMenu: true
        },
        children: [
            {
                path: '/home',
                component: () => import('@/views/404/index.vue'),
                meta: {

                }
            }
        ]
    },
    {
        path: '/model',
        name: 'model',
        component: () => import('@/layout/index.vue'),
        redirect: '/model/list',
        meta: {
            title: '模型',
            icon: "Promotion",
        },
        children: [
            {
                path: '/model/list',
                name:'modelList',
                component: () => import('@/views/model/index.vue'),
                meta: {
                    title: '模型列表',
                    icon: "Crop",
                }
            },
            {
                path: '/model/model-data/list',
                name:'modelDataList',
                component: () => import('@/views/model-data/index.vue'),
                meta: {
                    title: '数据列表',
                    icon: "Crop",
                }
            }
        ]
    }
    ,
]
export const anyRoute = [
    {
        path: '/',
        redirect: '/home',
    },
    {
        path: '/login',
        component: () => import('@/views/login/index.vue'),
        name: 'login'
    },
    {
        path: '/register',
        component: () => import('@/views/register/index.vue'),
        name: 'register'
    },
    {
        path: '/:pathMatch(.*)*',
        component: () => import('@/views/404/index.vue'),
        name: '404'
    }
]
export const asyncRoute = [
    {
        path: '/user',
        name: 'user',
        component: () => import('@/layout/index.vue'),
        permission: 'user',
        redirect: '/user/manage',
        meta: {
            title: '用户',
            icon: "User",
        },
        children: [
            {
                path: '/user/manage',
                component: () => import('@/views/user/index.vue'),
                permission: 'user',
                meta: {
                    title: '用户管理',
                    icon: "HomeFilled",
                },
            }
        ]
    }
]