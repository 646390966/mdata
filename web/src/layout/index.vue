<template>
    <div class="layout_container">

        <!-- 顶部导航 -->
        <div class="layout_tabbar">
            <div class="layout_tabbar_left">
                <Logo>
                </Logo>
            </div>
            <div class="layout_tabbar_center">
                <el-menu :default-active="rootRoute" mode="horizontal" background-color="#001529" text-color="#ffffff"
                    active-text-color="#ffffff"  @select="handleSelect">

                    <Tarbbar :menuList="userCenter.userRoute"></Tarbbar>

                </el-menu>
            </div>
            <div class="layout_tabbar_crumbs">
                <BreadCrumb style="position:absolute;top:50%;left:50%;transform:translate(-50%,-50%)"></BreadCrumb>
            </div>
            <div class="layout_tabbar_right">
                <div style="display:flex;align-items:center;justify-content:end;height:100%">
                    <Setting></Setting>
                </div>


            </div>
        </div>
        <!-- 左侧菜单 -->
        <div class="layout_slider" :class="{ fold: disPlayMenu ? false : true }">

            <el-scrollbar v-show="disPlayMenu">
                <el-menu :default-active="$route.path" background-color="#001529" text-color="#ffffff"
                active-text-color="#ffffff">
                    <Menu :menuList="currentChildRoute"></Menu>
                </el-menu>
            </el-scrollbar>
        </div>
        <!-- 内容区域 -->
        <div class="layout_main" :class="{ fold: disPlayMenu ? false : true }">
            <Main></Main>
          
        </div>

    </div>
</template>
<script setup lang="ts">
import Logo from './logo/index.vue'
import { useRoute, useRouter } from 'vue-router'
import Menu from './menu/index.vue'
import Tarbbar from './tarbbar/index.vue'
import Main from './main/index.vue'
import BreadCrumb from './tarbbar/breadcrumb/index.vue'
import Setting from './tarbbar/setting/index.vue'
import { ref, computed, onMounted } from 'vue'
import router from '@/router'
import { userStore } from "@/store/user";
//获取路由对象
const $route = useRoute()

const currentChildRoute = ref()
const disPlayMenu = ref(false)
const userCenter =  userStore()
const rootRoute = ref("/")

const handleSelect = (key: string) => {
    
    let useRoute = userCenter.userRoute
    let index = key.indexOf("/", 1)
    if (index != -1) {
        key = key.substring(0, index)
    }
    rootRoute.value=key
    for (let i = 0; i < useRoute.length; i++) {
        let currentRoute = useRoute[i];
        if (currentRoute.path == key) {
            currentChildRoute.value = currentRoute.children
            disPlayMenu.value = !currentRoute.meta.hideMenu
            break
        }
    }

}
onMounted(() => {
    handleSelect($route.path)
})
</script>

<style scoped lang="scss">
//左侧菜单的宽度
$base-menu-width: 260px;

$base-menu-min-width: 0px;
//上级目录宽度
$layout_tabbar_center_width: 520px;
:deep(.el-menu-item.is-active) {
    background-color: #409EFF!important;
  }
.layout_container {

    height: 100vh;
    width: 100%;
    position: relative;
    font-size:16px;
    color: black;
    overflow:hidden;
    .layout_tabbar {
        position: relative;
        top: 0;
        width: 100%;
        height: $base-menu-logo-height;
        background: $base-menu-background;

        .layout_tabbar_left {
            position: absolute;
            width: $base-menu-width;
            height: $base-menu-logo-height;
        }

        .layout_tabbar_center {
            position: absolute;
            width: $layout_tabbar_center_width;
            left: $base-menu-width;
            height: $base-menu-logo-height;
        }

        .layout_tabbar_crumbs {
            position: absolute;
            width: $layout_tabbar_center_width;
            left: calc($base-menu-width + $layout_tabbar_center_width);
            height: $base-menu-logo-height;
        }

        .layout_tabbar_right {
            position: absolute;
            width: calc(100% - $base-menu-width - $layout_tabbar_center_width - $layout_tabbar_center_width);
            left: calc($base-menu-width + $layout_tabbar_center_width + $layout_tabbar_center_width);
            height: $base-menu-logo-height;
        }
    }

    .layout_slider {
        position: absolute;
        top: $base-menu-logo-height;
        color: white;
        width: $base-menu-width;
        background: $base-menu-background;
        height: calc(100vh - $base-menu-logo-height);
        transition:all 0.3s;
        &.fold {
            width: $base-menu-min-width;
        }
    }



    .layout_main {
        position: absolute;
        top: $base-menu-logo-height;
        left: $base-menu-width;
        width: calc(100% - $base-menu-width);
        background-color:#efecfa;
        height: calc(100% - $base-menu-logo-height);

        &.fold {
            left: $base-menu-min-width;
            width: calc(100% - $base-menu-min-width);
        }
    }
}
</style>