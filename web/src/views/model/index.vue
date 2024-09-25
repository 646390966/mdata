<template>
    <div class="outer">
        <div class="header">
            <div class="header_left">
                <span style="font-weight:bold">名称：</span><el-input v-model="search.name" style="width: 140px"
                    placeholder="请输入名称" />
                <span style="font-weight:bold;margin-left:15px">创建时间：</span><el-date-picker v-model="search.createTime"
                    type="datetimerange" style="width:400px" :shortcuts="shortcuts" valueFormat="YYYY-MM-DD HH:mm:ss"
                    format="YYYY-MM-DD HH:mm:ss" range-separator="至" start-placeholder="开始时间" end-placeholder="结束时间" />
                <el-button style="margin-left:20px" @click="reset">重置</el-button>
                <el-button @click="getDataList">搜索</el-button>
            </div>
            <div class="header_right">
                <el-button type="primary" @click="addDataDialog">新建模型</el-button>

            </div>
        </div>
        <div class="content">
            <el-table :data="tableData" @sort-change="tableSortChange" style="height:100%"
                :header-cell-style="{ background: '#f5f7fa', color: '#606266', 'text-align': 'center' }"
                :cell-style="{ 'text-align': 'center' }" border stripe show-overflow-tooltip>
                <el-table-column prop="name" label="名称" width="180" />
                <el-table-column prop="comment" label="注释" width="180" />
                <el-table-column prop="db" label="库名" width="180" />
                <el-table-column prop="source" label="数据库类型" width="180" />

                <el-table-column prop="createTime" label="创建时间" width="180" sortable='custom' />
                <el-table-column prop="updateTime" label="修改时间" width="180" sortable='custom' />

                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button type="primary" @click="handleEdit(scope.row)">
                            编辑
                        </el-button>

                        <el-popconfirm title="确定删除?" @confirm="handleDelete(scope.row)">
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
                layout="prev, pager,next,  sizes,jumper, ->, total" @size-change="onSizeChange"
                @current-change="onPageChange" />
        </div>

        <el-dialog v-model="dataDialogVisible" :title="dialogTitle" width="1100" :before-close="closeDialog">
            <el-form :model="chooseData" :rules="rules" ref="formRef" label-width="auto" hide-required-asterisk>
                <div style="display:flex;justify-content: space-around;">
                    <el-form-item prop="name">
                        <template #label>
                            <span style="font-size:16px">模型名
                            </span>
                        </template>
                        <el-input v-model.trim="chooseData.name" size="large">
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="comment">
                        <template #label>
                            <span style="font-size:16px">描述
                            </span>
                        </template>
                        <el-input v-model.trim="chooseData.comment" size="large">
                        </el-input>
                    </el-form-item>
                </div>
                <el-table :data="chooseData.fields" highlight-current-row style="width: 100%"
                    :header-cell-style="{ background: '#f5f7fa', color: '#606266', 'text-align': 'center' }"
                    :cell-style="{ 'text-align': 'center' }" border stripe show-overflow-tooltip>
                    <el-table-column prop="name" label="名称" width="180">
                        <template #default="scope">
                            <el-form-item :prop="'fields.' + scope.$index + '.name'"
                                :rules="[{ required: true, message: '字段名称不能为空' }]">
                                <el-input style="margin-top:16px" v-model="scope.row.name" placeholder="请输入内容"></el-input>
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column prop="type" label="类型" width="150">
                        <template #default="scope">
                            <el-form-item :prop="'fields.' + scope.$index + '.type'"
                                :rules="[{ required: true, message: '类型不能为空' }]">
                                <el-select style="margin-top:16px" v-model="scope.row.type" placeholder="请选择">
                                    <el-option v-for="item in types" :key="item.value" :label="item.label"
                                        :value="item.value" />
                                </el-select>
                            </el-form-item>
                        </template>
                    </el-table-column>
                    <el-table-column prop="length" label="长度" width="150">
                        <template #default="scope">
                            <el-input v-model="scope.row.length" placeholder="请输入内容"></el-input>
                        </template>
                    </el-table-column>
                    <el-table-column prop="comment" label="描述" width="180">
                        <template #default="scope">
                            <el-input v-model="scope.row.comment" placeholder="请输入内容"></el-input>
                        </template>
                    </el-table-column>

                    <el-table-column prop="primaryKey" label="是否可搜索" width="180">
                        <template #default="scope">
                            <el-radio-group v-model="scope.row.searchAble" class="ml-4" :disabled="scope.row.type=='BOOLEAN'">
                                <el-radio size="small" :label="true">是</el-radio>
                                <el-radio size="small" :label="false">否</el-radio>

                            </el-radio-group>
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" width="120">
                        <template #default="scope">
                            <el-button size="small" @click="handleDeleteRow(scope.$index)">删除</el-button>

                        </template>
                    </el-table-column>
                </el-table>
            </el-form>

            <div style="display:flex;justify-content:center;align-items: center;margin-top:5px" @click="handleAddRow">
                <svg-icon name="add" color="black" width="20px" height="20px"></svg-icon>
            </div>

            <div class="clearfix">

                <el-button type="primary" style="float:right" @click="operateData">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script setup lang="ts" name="Model">

import { ref, Ref, onMounted, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElNotification } from 'element-plus'
import { getModelListApi, createModelApi, updateModelApi, deleteModelApi, getModelByIdApi } from '@/api/model';
import type { ModelList, Model } from '@/api/model/type'
import { camelToUnderscore } from '@/utils/function'
const search: Ref<{ createTime: [], name: string }> = ref({
    createTime: [],
    name: ''

})
const chooseData = ref<Model>({
    fields: []
})
const dialogTitle = ref('')
const types = ref([
    { label: "字符串", value: "STRING" },
    { label: "数字", value: "NUMBER" },
    { label: "布尔", value: "BOOLEAN" },
    { label: "年月日", value: "DATE" },
    { label: "年月日时分秒", value: "DATETIME" },
    { label: "时间戳", value: "TIMESTAMP" },
])
function handleAddRow(index) {
    chooseData.value.fields.push({
        searchAble:false
    });
}
const reset = () => [
    search.value = {}
]
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
function handleDeleteRow(index) {
    chooseData.value.fields.splice(index, 1);
}
const rules = ref({
    name: [
        { required: true, message: '请输入名称', trigger: 'blur' },
    ]

})
const pagination = ref({
    page: 1,
    size: 10,
    total: 100
});
const tableData = ref<Model[]>()
const formRef = ref()
const addDataDialog = () => {
    dialogTitle.value = "新增模型"
    chooseData.value = {
        fields: []
    }
    dataDialogVisible.value = true;

}
const tableSortChange = (column) => {
    search.value.orderColumn = column.prop
    search.value.order = column.order== 'ascending' ? false : true
    getDataList()

}
const dataDialogVisible = ref(false);
const handleDelete = (row) => {

    deleteModelApi(row.id).then((res) => {
        ElMessage({
            message: '删除成功',
            type: 'success',
        })
        getDataList()
    })
}
const handleEdit = async (row) => {
    dialogTitle.value = "修改模型"
    chooseData.value = await getModelByIdApi(row.id)
    chooseData.value.fields = chooseData.value.fields.filter(item=>item.name!='id')
    dataDialogVisible.value = true;
}
const operateData = () => {
    formRef.value.validate(async (valid: boolean) => {

        if (!valid) {
            return
        }

        if (dialogTitle.value == "新增模型") {
            await createModelApi(chooseData.value)
            ElNotification({
                type: 'success',
                message: '新增成功'
            })
        } else {
            await updateModelApi(chooseData.value.id, chooseData.value)
            ElNotification({
                type: 'success',
                message: '修改成功'
            })
        }
        getDataList()
        dataDialogVisible.value = !dataDialogVisible.value
    })

}

const closeDialog = () => {

    dataDialogVisible.value = !dataDialogVisible.value
}
const getDataList = async () => {

    let createTimeStart = search.value.createTime && search.value.createTime.length > 0 ? search.value.createTime[0] : undefined
    let createTimeEnd = search.value.createTime && search.value.createTime.length > 1 ? search.value.createTime[1] : undefined
    let res: ModelList = await getModelListApi({
        page: pagination.value.page
        , size: pagination.value.size
        , name: search.value.name
        , createTimeStart: createTimeStart
        , createTimeEnd: createTimeEnd
        , desc: search.value.order == 'ascending' ? false : true
        , orderColumn: camelToUnderscore(search.value.orderColumn)
    })

    tableData.value = res.data
    pagination.value.total = res.total
}
const onPageChange = (value: number) => {
    pagination.value.page = value;
    getDataList()
}
const onSizeChange = (value: number) => {
    pagination.value.size = value;
    getDataList()
}
onMounted(() => {
    getDataList()


})
</script>

<style scoped  lang="scss">
$base-pagination-height: 40px;
$base-header-height: 45px;

.clearfix::after {
    content: "";
    display: table;
    clear: both;
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
            flex-basis: 55%;
            display: flex;
            justify-content: flex-end;
            align-items: center;
        }

        .header_right {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            flex-basis: 45%;
        }
    }

    .content {
        height: calc(100% - $base-pagination-height - $base-header-height);
        padding-bottom: 5px;
    }

    .pagination {
        height: $base-pagination-height;
        float: right;
    }
}
</style>