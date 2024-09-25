<template>
    <div class="register_container">
        <div class="register_wapper">


            <h4 class="register_title">
                用户注册
            </h4>
            <el-form :model="form" :rules="rules" ref="formRef" style="width: 400px"  label-width="auto"   hide-required-asterisk>
                <el-form-item label="用户名" prop="username" >
                    <el-input v-model.trim="form.username" size="large">
                    </el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="form.password" type="password" size="large"  show-password>
                    </el-input>
                </el-form-item>
                <el-form-item label="再次输入密码" prop="passwordAgain">
                    <el-input v-model="form.passwordAgain" type="password" size="large"  show-password>
                    </el-input>
                </el-form-item>
                <el-form-item label="头像">
                    
                    <el-upload
                    class="avatar-uploader"
                    :action="registerUrl"
                    :show-file-list="false"
                    :before-upload="beforeAvatarUpload"
                    
                  >

                    <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                  </el-upload>
                </el-form-item>
                <el-form-item style="margin:0px;float:right">
                    <el-button style="width:300px" type="primary" size="large" @click="register">注册</el-button>

                </el-form-item>
                <el-form-item style="float:right">
                    <router-link to="login" style="color:blue;font-size:16px;text-decoration:none">已有账号？去登录？</router-link>
                </el-form-item>
            </el-form>
        </div>

    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import setting from '@/setting'
import { login, getUserByIdApi, getUserListApi,createUserApi } from '@/api/user';
import type { User, UserList } from '@/api/user/type'
import { useRouter, useRoute } from 'vue-router'
import { ElNotification,ElMessage } from 'element-plus'
import type { UploadProps } from 'element-plus'
let $router = useRouter();
let $route = useRoute();

const formRef = ref()
const form = ref({})
const rowFile = ref()


const imageUrl = ref('')
const registerUrl = ref(import.meta.env.VITE_BASE_API+"/user/register")


const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg'&&rawFile.type !== 'image/png') {
    ElMessage.error('图片必须为JPG或者PNG格式')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片大小不能超过2MB')
    return false
  }
  rowFile.value=rawFile
  imageUrl.value = URL.createObjectURL(rawFile!)
  return false
}
const checkUserName = (rule, value, callback) => {

    const reg = /^[0-9A-Za-z]+$/;

    if (reg.test(value)) {
        callback();
    }
    
    callback(new Error('只能输入数字或字母'));
}
const checkPasswordAgain = (rule, value, callback) => {

if(value==form.value.password) {
    callback();
}

callback(new Error('两次密码输入不一致'));
}



const register = () => {
    formRef.value.validate(async (valid: boolean) => {

        if (!valid) {
            return
        }
        const formData = new FormData();
        if(rowFile.value) {
        formData.append('file', rowFile.value);
        }
        formData.append('username', form.value.username);
        formData.append('password', btoa(form.value.password));
        let result: string = await createUserApi(formData)

        $router.push('/');

        //登录成功提示信息
        ElNotification({
            type: 'success',
            message: '注册成功'
        })

    });
}
const rules = ref({
    username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
        { validator: checkUserName, trigger: 'blur' },
        { min: 3, max: 10, message: '账号长度需要在3到10之间', trigger: 'blur' },

    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 3, max: 10, message: '密码长度需要在3到10之间', trigger: 'blur' },
    ],
    passwordAgain: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        { min: 3, max: 10, message: '密码长度需要在3到10之间', trigger: 'blur' },
        { validator: checkPasswordAgain, trigger: 'blur' },
    ]
})
</script>

<style scoped>
.register_container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;

    .register_wapper {
        display: flex;
        width: 40vw;
        height: 70vh;

        box-shadow: -4px 5px 10px rgba(0, 0, 0, 0.4);
        border-radius: 5px;
        justify-content: center;
        align-items: center;
        flex-direction: column;



        .register_title {
            color: black;
            margin-bottom: 40px;
            font-weight: 500;
            font-size: 32px;
            letter-spacing: 4px;
        }

    }



}
.avatar-uploader{
    width: 100px;
    height: 100px;
    border: 1px dotted blue;
}
.avatar-uploader .avatar {
    width: 100px;
    height: 100px;
    display: block;
  
  }
.avatar-uploader .el-upload {
    border: 6px solid red;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
  }
  
  .avatar-uploader .el-upload:hover {
    border-color: var(--el-color-primary);
  }
  
  .el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    text-align: center;
  }
</style>