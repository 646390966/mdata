<template>
    <el-breadcrumb :separator-icon="ArrowRight">
        <el-breadcrumb-item v-has="`breadcrumb`" v-for="(item,index) in $route.matched" :key="index" :to="item.path">
            
            <el-icon  size="20" color="white" style="vertical-align:middle">
                <component :is="item.meta.icon"></component>
            </el-icon>
            
            <span style="color:white;font-size:16px;margin-left:5px;vertical-align:middle">{{item.meta.title}}</span>
        </el-breadcrumb-item>
    </el-breadcrumb>
</template>
<script setup lang="ts">
import { ArrowRight } from '@element-plus/icons-vue'
//引入设置标题与logo配置文件
import { useRoute } from 'vue-router';
let $route = useRoute();




</script>

<style scoped lang="scss"></style>