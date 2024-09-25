<template>
    <el-button icon="Refresh" circle @click="updateRefsh"></el-button>
    <el-button icon="FullScreen" circle @click="fullScreen"></el-button>
    <el-popover placement="bottom" :width="150"  trigger="hover">  
        <template #reference>
          <el-button icon="Setting" circle></el-button>
        </template>
        <el-form>
            <el-form-item label="主题颜色">
                <el-color-picker v-model="color" @change="setColor" show-alpha :predefine="predefineColors" :teleported="false"/>
            </el-form-item>
            <el-form-item label="暗黑模式">
              <el-switch v-model="dark" @change="changeDark" />
            </el-form-item>
          </el-form>
      </el-popover>
    
      <el-dropdown>
     
        <span style="font-size:18px;color:white;margin-left:20px">
          <img v-if="userCenter.user.avator" :src="'data:image/png;base64,'+userCenter.user.avator" style="border-radius:12px;width:25px;height:25px;vertical-align:middle" alt=""/>
          <span style="vertical-align:middle;margin-left:2px;">{{userCenter.user.username}}</span>
            <el-icon size="18" color="white" style="vertical-align:middle">
                <arrow-down />
            </el-icon>
        </span>
        <template #dropdown>
            <el-dropdown-menu >
                <el-dropdown-item @click="logout" ><el-icon size="16">
                  <SwitchButton/>
                </el-icon><span style="font-size:16px">退出登录</span></el-dropdown-item>
                <el-dropdown-item @click="deleteUser" ><el-icon size="16">
                  <Remove/>
                </el-icon><span style="font-size:16px">注销</span></el-dropdown-item>
            </el-dropdown-menu>
        </template>
    </el-dropdown>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { settingStore } from "@/store/setting";
import {useRouter} from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import settings from '@/setting'
import { userStore } from "@/store/user";
import type {User} from '@/api/user'
import { deleteUserApi } from '@/api/user';
const userCenter = userStore();
let $router = useRouter();
const settingCenter = settingStore();

const color = ref('rgba(255, 69, 0, 0.68)')
const changeDark = () => {
  let html = document.documentElement
  dark.value ? html.className = 'dark' : html.className = ''
}
let dark = ref<boolean>(false)
  const setColor = () => {
  //通过js修改根节点的样式
  const html = document.documentElement;
  html.style.setProperty('--el-color-primary', color.value)
}
const fullScreen = ()=>{
    let full = document.fullscreenElement;
    if (!full) {
    //利用dom方法实现全屏
    document.documentElement.requestFullscreen();
  } else {
    document.exitFullscreen();
  }
}
const updateRefsh = ()=>{
  settingCenter.reverse()
}

const predefineColors = ref([
  '#ff4500',
  '#ff8c00',
  '#ffd700',
  '#90ee90',
  '#00ced1',
  '#1e90ff',
  '#c71585',
  'rgba(255, 69, 0, 0.68)',
  'rgb(255, 120, 0)',
  'hsv(51, 100, 98)',
  'hsva(120, 40, 94, 0.5)',
  'hsl(181, 100%, 37%)',
  'hsla(209, 100%, 56%, 0.73)',
  '#c7158577',
])

const deleteUser = async () => {
  ElMessageBox.confirm(
    '是否确定要注销用户?',
    '注销',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(async ()=>{
    
    await deleteUserApi(userCenter.user.id);
    
    window.localStorage.removeItem('mdata-token')
    userCenter.reset()
    $router.push('/login')
  })
  
}
const logout = async () => {
  ElMessageBox.confirm(
    '是否确定要登出?',
    '登出',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(()=>{
    window.localStorage.removeItem('mdata-token')
    userCenter.reset()
    $router.push('/login')
  })
  
}




</script>


<style scoped>

</style>