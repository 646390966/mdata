<template>
    <div class="container">
        <div class="wapper">
            <div class="a"></div>
            <div class="b">
    
                <span>404 页面无法找到</span>
                <el-button type="primary" @click="goback" size="large" round>返回</el-button>
    
    
            </div>
        </div>


    </div>
</template>

<script setup lang="ts">
import {useRouter} from 'vue-router';

let $router = useRouter();
const goback=()=>{
    $router.back()
}
</script>
export default{
    name:"404"
}
<style scoped>
.container {
    height: 100vh;
    display: flex;
    justify-content: center;
    align-items: center;
    .wapper{
        width: 700px;
        height: 300px;
        display: flex;
        .a {
            background-image: url('@/assets/images/404.svg');
            background-repeat: no-repeat;
            background-size: contain;
            width: 500px;
            height: 400px;
            flex:1;
        }
        
        .b {
        flex:0 1 auto;
        display:flex;
        flex-direction: column;
        justify-content: space-around;
        }
    }
}



.container span {
    color:black;
    font-size: 22px;
    font-weight: bolder;
}
</style>