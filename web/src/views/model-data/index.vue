<template>
    <div class="outer">
        <div class="left">
            <el-card style="height:100%">
                <template #header>
                    <div>
                        <span style="font-weight:bolder">模型名称</span>

                    </div>
                </template>
                <el-tree ref="treeRef" :current-node-key="currentModel.id" :data="modelList" :props="defaultProps"
                    node-key="id" @node-click="handleNodeClick" highlight-current />
            </el-card>
        </div>
        <div class="right">
            <div class="header">
                <div class="header_left">
                    <template v-for='(value, key, index) in search'>
                        <span style="font-weight:bold;margin-left:15px">{{ key }}:</span> <el-input v-model="search[`${key}`]"
                            style="width: 140px" placeholder="请输入名称" />
                    </template>



                </div>
                <div class="header_right">
                    <el-button type="primary" style="margin-left:20px" @click="reset">重置</el-button>
                    <el-button type="primary" @click="getModelDataList">搜索</el-button>


                </div>
            </div>
            <div class="header">
                <div class="header_left">

                </div>
                <div class="header_right">
                    <el-button type="primary" @click="excelExport">EXCEL导出</el-button>
                    <el-button type="primary" @click="excelImport">EXCEL导入</el-button>
                    <el-button type="primary" @click="excelTemplateExport">EXCEL模板导出</el-button>
                    <el-button type="primary" @click="addDataDialog">新增数据</el-button>

                </div>
            </div>
            <div class="content">
                <el-table :data="modelDataList" @sort-change="tableSortChange" style="height:100%"
                    :header-cell-style="{ background: '#f5f7fa', color: '#606266', 'text-align': 'center' }"
                    :cell-style="{ 'text-align': 'center' }" border stripe show-overflow-tooltip>
                    <template v-for='(col) in modelFields'>
                        <el-table-column align='center' sortable :show-overflow-tooltip="true" :prop="col.name"
                            :label="col.name">
                        </el-table-column>
                    </template>
                    <el-table-column label="操作" width="200">
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
        </div>
        <el-dialog v-model="dataDialogVisible" :title="dialogTitle" width="400" :before-close="closeDialog">
            <el-form :model="chooseData" ref="formRef" label-width="auto" hide-required-asterisk>
                <div v-for="(item, index) in modelFields">
                    <el-form-item :label="item.name" :prop="item.name">
                        <el-input v-model="chooseData[item.name]" style="width:300px" clearable></el-input>
                    </el-form-item>
                </div>
            </el-form>
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
import { getModelDataListApi, exportModelDataTemplateApi, createModelDataApi, updateModelDataApi, deleteModelDataApi, getModelDataByIdApi } from '@/api/model-data';
import type { ModelList, Model } from '@/api/model/type'
import type { ModelData, ModelDataList } from '@/api/model-data/type'
import { camelToUnderscore } from '@/utils/function'
import { nextTick } from 'process';
const dataDialogVisible = ref(false);
const defaultProps = {
    label: 'name',
}
const dialogTitle = ref('')
const treeRef = ref()
const modelList = ref<ModelList>()
const modelDataList = ref<ModelDataList>()
const modelFields = ref()
const currentModel = ref({ 'id': '' })
const search: Ref<{ createTime: [], name: string }> = ref({


})
const pagination = ref({
    page: 1,
    size: 10,
    total: 100
});
const closeDialog = () => {

    dataDialogVisible.value = !dataDialogVisible.value
}

const reset = () => {
    for (let key in search.value) {
        search.value[key] = ''
    }
}
const chooseData = ref<ModelData>({

})
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
const operateData = async () => {

    if (dialogTitle.value == "新增数据") {

        await createModelDataApi(currentModel.value.name, [chooseData.value])
        ElNotification({
            type: 'success',
            message: '新增成功'
        })
    } else if (dialogTitle.value == "编辑数据") {
        await updateModelDataApi(currentModel.value.name, chooseData.value['id'], chooseData.value)
        ElNotification({
            type: 'success',
            message: '修改成功'
        })
    }
    getModelDataList()
    dataDialogVisible.value = !dataDialogVisible.value
}
const excelTemplateExport = async () => {

    await exportModelDataTemplateApi(currentModel.value.name).then((res) => {
        const type = 'application/octet-stream'
        const url = window.URL.createObjectURL(new Blob([res], { type }))
        const link = document.createElement('a')

        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', 'aaa.xlsx')
        document.body.appendChild(link)
        link.click()

        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)
        document.body.removeChild(link)
        ElNotification({
            type: 'success',
            message: '获取成功'
        })
    })
}
const excelImport = () => {

}
const excelExport = () => {

}
const getModelList = async () => {
    let res: ModelList = await getModelListApi({
        page: 1
        , size: -1

    })

    modelList.value = res.data

    if (modelList.value.length > 0) {

        nextTick(() => {
            currentModel.value = modelList.value[0]
            getModelFields()
            getModelDataList()
        })
    }
}
const addDataDialog = () => {
    dialogTitle.value = "新增数据"
    chooseData.value = {

    }
    dataDialogVisible.value = true;

}
const handleEdit = (row) => {
    dialogTitle.value = "编辑数据"
    chooseData.value = row
    dataDialogVisible.value = true;
}
const handleDelete = (row) => {
    deleteModelDataApi(currentModel.value.name, row.id).then((res) => {
        ElMessage({
            message: '删除成功',
            type: 'success',
        })
        getModelDataList()
    })
}
const tableSortChange = (column) => {
    search.value.orderColumn = column.prop
    search.value.order = column.order
    getModelDataList()

}
const getModelDataList = async () => {

    let res: ModelList = await getModelDataListApi(currentModel.value['name'], {
        page: pagination.value.page,
        size: pagination.value.size,
        ...search.value
    })
    modelDataList.value = res.data
    pagination.value.total = res.total
}
const getModelFields = async () => {

    let res: Model = await getModelByIdApi(currentModel.value['id'])

    modelFields.value = res['fields'].filter(s =>

        s.name != 'id'
    )


    modelFields.value.forEach((item) => {

        if (item.searchAble == true) {
            search.value[item.name] = ''
        }
    })

}
const handleNodeClick = (data: Model) => {
    currentModel.value = data

    getModelFields()

    getModelDataList()
}
const onPageChange = (value: number) => {
    pagination.value.page = value;
    getModelDataList()
}
const onSizeChange = (value: number) => {
    pagination.value.size = value;
    getModelDataList()
}
onMounted(() => {
    getModelList()


})
</script>

<style scoped  lang="scss">
$base-pagination-height: 40px;
$base-header-height: 45px;

:deep(.el-tree--highlight-current .el-tree-node.is-current>.el-tree-node__content) {
    background-color: #409EFF !important;
    color: white !important;
}

:deep(.el-tree-node__label) {
    font-size: 16px;
}

.clearfix::after {
    content: "";
    display: table;
    clear: both;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.outer {
    height: 100%;
    width: 100%;
    padding: 20px;
    padding-bottom: 0px;
    display: flex;
    justify-content: space-between;

    .left {
        width: 15%;
    }

    .right {

        width: 84%;

        .header {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            height: $base-header-height;
            padding-bottom: 15px;

            .header_left {
                flex-basis: 55%;
                display: flex;
                justify-content: flex-start;
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
            height: calc(100% - $base-pagination-height - $base-header-height - $base-header-height);
            padding-bottom: 5px;
        }

        .pagination {
            height: $base-pagination-height;
            float: right;

        }
    }
}
</style>