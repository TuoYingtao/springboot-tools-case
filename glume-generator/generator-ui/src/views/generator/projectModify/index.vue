<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="QueryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="项目名" prop="projectName">
        <el-input
            v-model="queryParams.projectName"
            placeholder="请输入项目名"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间" style="width: 308px;">
        <el-date-picker
            v-model="dateRange"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <!--  表格操作  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['*:*:*']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['*:*:*']">
          修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['*:*:*']">
          删除
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getDataList"/>
    </el-row>
    <!--  表格数据  -->
    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="项目名" align="center" prop="projectName"/>
      <el-table-column label="项目标识" align="center" prop="projectCode" :show-overflow-tooltip="true"/>
      <el-table-column label="项目包名" align="center" prop="projectPackage" :show-overflow-tooltip="true"/>
      <el-table-column label="项目路径" align="center" prop="projectPath" :show-overflow-tooltip="true"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="250" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['*:*:*']">
            修改
          </el-button>
          <el-button link type="primary" icon="Download" @click="handleDownload(scope.row)" v-hasPermi="['*:*:*']">
            源码下载
          </el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['*:*:*']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--  分页  -->
    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getDataList"
    />
  </div>
  <!-- 添加或修改参数配置对话框 -->
  <form-dialog
      ref="FormDialogRef"
      :title="title"
      :form-data="form"
      @onAmendSubmitForm="onAmendSubmitForm"
      @onSaveSubmitForm="onSaveSubmitForm"
  />
  <origin-code-dialog ref="OriginCodeDialogRef" title="源码下载" :form-data="form" @onDownload="onDownload" />
</template>

<script setup lang="ts" name="ProjectModify">
import { getCurrentInstance } from "vue";
import * as CurrentConstants from "@/views/generator/projectModify/constants";
import { parseTime } from "@/utils";
import { ProjectModifyEntity } from "@/api/generator/models/ProjectModifyEntity";
import FormDialog from "@/views/generator/projectModify/component/FormDialog.vue";
import { ProjectModifyApiService } from "@/api/generator/ProjectModifyApiService";
import OriginCodeDialog from "@/views/generator/projectModify/component/OriginCodeDialog.vue";

// 弹窗 Ref
const FormDialogRef = ref();
const OriginCodeDialogRef = ref();
// @ts-ignore
const {proxy} = getCurrentInstance();
// data 数据
const currentData = reactive(CurrentConstants.DATA);
const {queryParams, form, rules} = toRefs(currentData);
// 日期参数
const dateRange = ref([]);
// 搜索框显示
const showSearch = ref(true);
// 表格加载动画
const loading = ref(true);
// 表格数据
const dataList = ref<ProjectModifyEntity[]>([]);
// 总条数
const total = ref(0);
// 全局修改按钮状态
const single = ref(true);
// 全局批量按钮选中
const multiple = ref(true);
// 批量选中ID
const ids = ref<number[]>([]);
// 弹窗标题
const title = ref("");

const serviceApi = new ProjectModifyApiService();

onMounted(() => {
  getDataList();
})

/** 查询参数列表 */
async function getDataList() {
  loading.value = true;
  try {
    const {data} = await serviceApi.page(proxy.addDateRange(queryParams.value, dateRange.value));
    dataList.value = data.list;
    total.value = data.totalCount;
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  } finally {
    loading.value = false;
  }
}

/** 删除按钮操作 */
function handleDelete(row?: ProjectModifyEntity) {
  const params = row ? [row!.id] : ids.value;
  proxy.$modal.confirm('是否确认删除编号为"' + params + '"的数据项？').then(function () {
    return serviceApi.delete(params);
  }).then(() => {
    getDataList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 新增按钮操作 */
function handleAdd() {
  FormDialogRef.value.onOpen()
  title.value = "添加项目";
}

/** 修改按钮操作 */
async function handleUpdate(row?: ProjectModifyEntity) {
  const params = row!.id || ids.value;
  try {
    const {data} = await serviceApi.detail(params);
    if (!data) return proxy.$modal.msgWarning(`未找到[${params}]的数据`);
    form.value = data;
    FormDialogRef.value.onOpen();
    title.value = "修改项目";
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
}

/** 源码下载 */
const handleDownload = async (row: ProjectModifyEntity) => {
  const params = row.id || ids.value;
  try {
    const {data} = await serviceApi.detail(params);
    if (!data) return proxy.$modal.msgWarning(`未找到[${params}]的数据`);
    form.value = data;
    OriginCodeDialogRef.value.onOpen();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
}

/** 修改 */
const onAmendSubmitForm = async (formData: ProjectModifyEntity) => {
  try {
    await serviceApi.update(formData);
    proxy.$modal.msgSuccess('修改成功');
    await getDataList();
    FormDialogRef.value.onClose();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/** 新增 */
const onSaveSubmitForm = async (formData: ProjectModifyEntity) => {
  try {
    await serviceApi.save(formData);
    proxy.$modal.msgSuccess('新增成功');
    await getDataList();
    FormDialogRef.value.onClose();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/** 下载源码 */
const onDownload = async (formData: ProjectModifyEntity) => {
  try {
    await serviceApi.download(formData.id as number);
    OriginCodeDialogRef.value.onClose();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getDataList();
}

/** 重置按钮操作 */
function resetQuery() {
  dateRange.value = [];
  proxy.resetForm("QueryRef");
  handleQuery();
}

/** 多选框选中数据 */
function handleSelectionChange(selection: ProjectModifyEntity[]) {
  ids.value = selection.map((item: ProjectModifyEntity) => item.id!);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
</script>

<style scoped lang="scss">

</style>
