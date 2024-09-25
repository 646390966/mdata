<template>
    <template v-for="(item,index) in menuList" :key="item.path">

        
        <el-menu-item :index="item.path" @click="goRoute">
            <el-icon>
                <component :is="item.meta.icon"></component>
            </el-icon>
            <template #title>
                <span>{{item.meta.title}}</span>
            </template>
        </el-menu-item>
       
    </template>
    
</template>

<script setup lang="ts">

const props = defineProps(['menuList'])

import {useRouter} from 'vue-router';
let $router = useRouter();
const goRoute = (vc:any)=>{
    
    $router.push(vc.index)
}



</script>

<style scoped lang="scss">


</style>