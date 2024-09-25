<template>
    
        <router-view v-slot="{Component}">
            <transition name="fade">
             
                <!-- 渲染一级路由组件的子路由 -->
                <component :is="Component" v-if="flag"></component>
            </transition>

        </router-view>
    
</template>

<script setup lang="ts">
import { defineComponent } from 'vue';
import { settingStore } from "@/store/setting";
import { watch,nextTick,ref } from 'vue';
const setting = settingStore();
const flag = ref(true)
watch(()=>setting.flag,()=>{
    flag.value=!flag.value
    nextTick(()=>{
        flag.value=!flag.value
    })
})
</script>

<style scoped>
.fade-enter-from{
    opacity:0;
}
.fade-enter-active {
    transition:all 1s;
}
.fade-enter-to{
    opacity:1;
}
</style>