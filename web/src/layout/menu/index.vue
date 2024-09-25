<template>
    <template v-for="(item,index) in menuList" :key="item.path">

        <div v-if="item.children&&item.children.length>=1">
                <el-sub-menu  :index="item.path">
                    <template #title>
                        <el-icon>
                            <component :is="item.meta.icon"></component>
                        </el-icon>
                        <span>{{item.meta.title}}</span>
                    </template>
                    <Menu :menuList="item.children"></Menu>
                </el-sub-menu>
        </div>
        <div v-else>
            <el-menu-item :index="item.path" @click="goRoute">
                <el-icon>
                    <component :is="item.meta.icon"></component>
                </el-icon>
                <template #title>
                    <span>{{item.meta.title}}</span>
                </template>
            </el-menu-item>
        </div>
    </template>
</template>

<script setup lang="ts">

defineProps(['menuList'])
import {useRouter} from 'vue-router';
let $router = useRouter();
const goRoute = (vc:any)=>{
    
    $router.push(vc.index)
}



</script>
<script  lang="ts">

export default{
    name:'Menu'
}



</script>

<style scoped lang="scss">


</style>