<template>
    <div class="login_container">
        <div class="login_wapper">
            <div class="login_left">

                

                <p class="title">{{ setting.title }}</p>
                <svg-icon :name="setting.logo" width="300px" height="300px"></svg-icon>
            </div>
            <div class="login_right">
                <h4 class="login_title">
                    登录
                </h4>
                <el-form :model="form" :rules="rules" ref="formRef" status-icon>
                    <el-form-item prop="username">
                        <el-input v-model.trim="form.username" size="large" style="width:270px">
                            <template #prefix>
                                <el-icon>
                                    <Avatar />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="form.password" type="password" size="large" style="width:270px" show-password>
                            <template #prefix>
                                <el-icon>
                                    <Lock />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-checkbox v-model="form.remember" label="14天内记住登录信息" size="large">
                            14天内记住登录信息
                            <el-tooltip content="为了确保你的信息安全，不建议在公共设备上勾选此项">
                                <el-icon style="color: #e6a23c">
                                    <Warning />
                                </el-icon>
                            </el-tooltip>
                        </el-checkbox>
                    </el-form-item>
                    <el-form-item style="margin-bottom:0px">
                        <el-button style="width:300px"  type="primary" size="large" @click="login">登录</el-button>
                        
                    </el-form-item>
                    <el-form-item style="float:right">
                       <router-link to="register" style="color:blue;font-size:16px;text-decoration:none">新用户注册</router-link>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import setting from '@/setting'
import { loginApi, getUserByIdApi,getUserListApi,deleteUserApi } from '@/api/user';
import type {User,UserList} from '@/api/user/type'
import { useRouter,useRoute } from 'vue-router'
import { ElNotification} from 'element-plus'
let $router = useRouter();
let $route = useRoute();

const formRef = ref()
const form = ref({})

const checkUserName = (rule, value, callback) => {

    const reg = /^[0-9A-Za-z]+$/;

    if (reg.test(value)) {
        callback();
    }

    callback(new Error('只能输入数字或字母'));
}

const  getTimeMessage = ()=>{
    let message = ''
    let hours = new Date().getHours();
    if(hours<=9){
        message = '早上'
    } else if(hours<=14){
        message = '上午'
    } else if(hours<=18) {
        message = '下午'
    } else {
        message = '晚上'
    }
    return message
   }

const login = () => {
    formRef.value.validate(async (valid:boolean) => {

        if (!valid) {
            return
        }
        
        let result:string = await loginApi({ 'username': form.value.username, 'password': btoa(form.value.password) },{'remember':form.value.remember})
        
        window.localStorage.setItem('mdata-token', result)
        
        let redirect = $route.query.redirect
        $router.push({path:redirect||'/'});

        //登录成功提示信息
        ElNotification({
            type:'success',
            message:'登录成功',
            title:`hi,${getTimeMessage()}好`
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
    ]
})
</script>

<style scoped>
.login_container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;

    .login_wapper {
        display: flex;
        width: 50vw;
        height: 50vh;

        box-shadow: -4px 5px 10px rgba(0, 0, 0, 0.4);
        border-radius: 5px;

        .login_left {
            display: flex;
            flex: 1;
            background-color: #409eff;
            justify-content: center;
            align-items: center;
            flex-direction: column;



            .title {
                font-weight: 300;
                color: white;
                font-size: 25px;
                letter-spacing: 2px;
                margin-top: 30px;
              
            }
        }

        .login_right {
            display: flex;
            flex: 1;
            justify-content: center;
            align-items: center;
            flex-direction: column;

            .login_title {
                color: black;
                margin-bottom: 40px;
                font-weight: 500;
                font-size: 22px;
                letter-spacing: 4px;
            }
        }
    }



}</style>