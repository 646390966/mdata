<template>
  <div class="outer">

    <div class="header">
      <div class="header_left"></div>
      <div class="header_right">
        <span style="font-weight:bold">用户名：</span><el-input v-model="search.username" style="width: 140px"
          placeholder="请输入用户名称" />
        <span style="font-weight:bold;margin-left:15px">创建时间：</span><el-date-picker v-model="search.createTime"
          type="datetimerange" style="width:400px" :shortcuts="shortcuts" valueFormat="YYYY-MM-DD HH:mm:ss"
          format="YYYY-MM-DD HH:mm:ss" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" />
        <el-button style="margin-left:20px" @click="reset">重置</el-button>
        <el-button @click="getDataList">搜索</el-button>
      </div>

    </div>
    <div class="content">
      <el-table :data="tableData"  @sort-change="tableSortChange"
      :header-cell-style="{ background: '#f5f7fa', color: '#606266', 'text-align': 'center' }"
      :cell-style="{ 'text-align': 'center' }" border stripe show-overflow-tooltip>
      <el-table-column prop="username" label="用户名" width="180" />
      <el-table-column label="角色" width="360">
        <template #default="scope">
          <el-tag style="margin-right:5px" size="large" round v-for="(item, index) in scope.row.role" :key="index">{{
            item
          }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" sortable='custom' />
      <el-table-column prop="updateTime" label="修改时间" width="180" sortable='custom' />
      <el-table-column label="头像">
        <template #default="scope">
          <Picture :imageUrl="scope.row.avator"></Picture>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" @click="handleEdit(scope.row)">
            编辑
          </el-button>

          <el-popconfirm title="确定删除?"  @confirm="handleDelete(scope.row)">
            <template #reference>
              <el-button type="danger" v-preventReClick>
                删除
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    </div>

    <div class="pagination">
      <el-pagination background v-model:current-page="pagination.page" v-model:page-size="pagination.size"
        :total="pagination.total" :page-sizes="[10, 20, 40, 80, 100, 200, 500]"
        layout="prev, pager,next,  sizes,jumper, ->, total" @size-change="onSizeChange" @current-change="onPageChange" />
    </div>

    <el-dialog v-model="dialogVisible" title="更新" width="500" :before-close="closeDialog">

      <el-form :model="chooseData" :rules="rules" ref="formRef" style="width: 400px" label-width="auto"
        hide-required-asterisk>
        <el-form-item label="用户名" prop="username" >
          <el-input v-model.trim="chooseData.username" size="large">
          </el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="chooseData.password" type="password" size="large" show-password>
          </el-input>
        </el-form-item>
        <el-form-item label="再次输入密码" prop="passwordAgain">
          <el-input v-model="chooseData.passwordAgain" type="password" size="large" show-password>
          </el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">

          <el-tag style="margin-right:5px" size="large" round v-for="(item, index) in chooseData.role" :key="item"
            closable @close="removeRole(item)">{{
              item
            }}
          </el-tag>
          <el-icon v-if="!tagAddVisable" style="margin-left:8px;cursor:pointer" @click="tagAddVisable = true">
            <Plus />
          </el-icon>
          <el-input v-else v-model="addRole" style="width: 240px" placeholder="请输入新角色" @keydown.enter="confirmRoleClick">

            <template #append>
              <img @click="confirmRoleClick" src="@/assets/images/enter.png" style="background-color: #fff;" width="26"
                height="26" />

            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload class="avatar-uploader" :action="registerUrl" :show-file-list="false"
            :before-upload="beforeAvatarUpload">
            <div v-if="chooseData.avator">
              <Picture :isClick=false :imageUrl="chooseData.avator"></Picture>
            </div>
            <el-icon v-else class="avatar-uploader-icon">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
      </el-form>


      <template #footer>
        <div class="dialog-footer">
          <el-button @click="closeDialog">取消</el-button>
          <el-button type="primary" @click="updateDate">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts" name="User">
import Picture from '@/components/picture/index.vue'
import { ref, Ref, onMounted, reactive } from 'vue'
import setting from '@/setting'
import { getUserListApi, deleteUserApi, updateUserApi } from '@/api/user';
import type { User, UserList } from '@/api/user/type'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElNotification } from 'element-plus'
import { camelToUnderscore } from '@/utils/function'
import type { UploadProps } from 'element-plus'
const pagination = ref({
  page: 1,
  size: 10,
  total: 0
});
const search: Ref<{ createTime: [], username: string }> = ref({
  createTime: [],
  username: ''

})
const formRef = ref()
const rowFile = ref()



const registerUrl = ref(import.meta.env.VITE_BASE_API + "/user/register")
const tagAddVisable = ref(false)
const tableData = ref<User[]>()
const dialogVisible = ref(false)
const addRole = ref('')
const chooseData = ref<User>({})
const removeRole = (role) => {
  
  chooseData.value.role = chooseData.value.role.filter(e => e !== role);
}
const confirmRoleClick = () => {
  if (addRole.value == undefined || addRole.value == '') {
    return
  }
  if(!chooseData.value.role) {
    chooseData.value.role=[]
  }
  chooseData.value.role.push(addRole.value)
  addRole.value = ''
  tagAddVisable.value = false
}
const updateDate = async () => {
  formRef.value.validate(async (valid: boolean) => {

    if (!valid) {
      return
    }

    const formData = new FormData();
    if (rowFile.value) {
      formData.append('file', rowFile.value);
    }
    if (chooseData.value.role) {
      formData.append('role', chooseData.value.role);
    }
    formData.append('username', chooseData.value.username);
    formData.append('password', btoa(chooseData.value.password));
    await updateUserApi(chooseData.value.id, formData)


    //登录成功提示信息
    ElNotification({
      type: 'success',
      message: '更新成功'
    })
    dialogVisible.value = !dialogVisible.value
    getDataList()
  });

}

const tableSortChange = (column) => {
  search.value.orderColumn = column.prop
  search.value.order = column.order
  getDataList()

}
const getDataList = async () => {

  let createTimeStart = search.value.createTime && search.value.createTime.length > 0 ? search.value.createTime[0] : undefined
  let createTimeEnd = search.value.createTime && search.value.createTime.length > 1 ? search.value.createTime[1] : undefined
  let res: UserList = await getUserListApi({
    page: pagination.value.page
    , size: pagination.value.size, username: search.value.username
    , createTimeStart: createTimeStart, createTimeEnd: createTimeEnd
    , desc: search.value.order == 'ascending' ? false : true
    , orderColumn: camelToUnderscore(search.value.orderColumn)
  })

  res.data.forEach((r) => {
    r.show = false
  })
  tableData.value = res.data
  pagination.value.total = res.total
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.type !== 'image/jpeg' && rawFile.type !== 'image/png') {
    ElMessage.error('图片必须为JPG或者PNG格式')
    return false
  } else if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('图片大小不能超过2MB')
    return false
  }
  rowFile.value = rawFile
  const reader = new FileReader()
  reader.readAsDataURL(rawFile)
  reader.onload = function () {
    chooseData.value.avator = this.result
  }

  return false
}

const onPageChange = (value: number) => {
  pagination.value.page = value;
  getDataList()
}
const onSizeChange = (value: number) => {
  pagination.value.size = value;
  getDataList()
}
const closeDialog = () => {
  chooseData.value = {}
  dialogVisible.value = !dialogVisible.value
}

const checkUserName = (rule, value, callback) => {

  const reg = /^[0-9A-Za-z]+$/;

  if (reg.test(value)) {
    callback();
  }

  callback(new Error('只能输入数字或字母'));
}
const checkPasswordAgain = (rule, value, callback) => {

  if (value == chooseData.value.password) {
    callback();
  }

  callback(new Error('两次密码输入不一致'));
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

const reset = () => [
  search.value = {}
]
const handleEdit = (row: User) => {
  chooseData.value = JSON.parse(JSON.stringify(row));

  chooseData.value.password = ''
  dialogVisible.value = !dialogVisible.value
}

const handleDelete = (row) => {
  deleteUserApi(row.id).then((res) => {
    ElMessage({
      message: '删除成功',
      type: 'success',
    })
    getDataList()
  })
}
const shortcuts = [
  {
    text: "最近一周",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      return [start, end];
    },
  },
  {
    text: "最近一个月",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      return [start, end];
    },
  },
  {
    text: "最近三个月",
    value: () => {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      return [start, end];
    },
  },
];
onMounted(() => {
  getDataList()


})
</script>

<style scoped  lang="scss">
$base-pagination-height: 30px;
$base-header-height: 45px;

#myImg {
  width: 500px;
  height: 500px;
  background-size: contain;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table .el-table__cell) {
  box-sizing: border-box;
  min-width: 1px;
  padding: 8px 0;
  position: relative;
  text-align: left;
  text-overflow: ellipsis;
  vertical-align: middle;
  z-index: auto;
}

.outer {
  height: 100%;
  padding: 20px;
  padding-bottom: 0px;

  .header {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    height: $base-header-height;
    padding-bottom: 15px;

    .header_left {
      flex-basis: 450;
    }

    .header_right {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      flex-basis: 55%;
    }
  }

  .content {
    height: calc(100% - $base-pagination-height - $base-header-height)
  }

  .pagination {
    height: $base-pagination-height;
    float: right;
  }
}
</style>