<template>
  <div class="app-container">
<#if queryList!?size gt 0>
    <el-form :model="queryParams" ref="QueryRef" :inline="true" v-show="showSearch" label-width="68px">
<#list queryList as field>
      <el-form-item label="${field.fieldComment!}" style="width: 308px;">
  <#if field.queryFormType == "text" || field.queryFormType == "textarea" || field.queryFormType == "editor">
        <el-input
            v-model="queryParams.${field.attrName}"
            placeholder="请输入${field.fieldComment!}"
            clearable
            style="width: 240px"
            @keyup.enter="handleQuery"/>
  <#elseif field.queryFormType == "select">
        <el-select v-model="queryParams.${field.attrName}" placeholder="请选择${field.fieldComment!}">
          <el-option label="选择" value="0"></el-option>
        </el-select>
  <#elseif field.queryFormType == "radio">
        <el-radio-group v-model="queryParams.${field.attrName}">
          <el-radio :label="0">单选</el-radio>
        </el-radio-group>
  <#elseif field.queryFormType == "date">
        <el-date-picker
            v-model="dateRange"
            value-format="YYYY-MM-DD"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"/>
  <#elseif field.queryFormType == "datetime">
        <el-date-picker
            v-model="dateRange"
            value-format="YYYY-MM-DD HH:mm:ss"
            type="daterange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"/>
  </#if>
      </el-form-item>
</#list>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
</#if>
    <!--  表格操作  -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="Plus"
            @click="handleAdd"
            v-hasPermi="['*:*:*']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="success"
            plain
            icon="Edit"
            :disabled="single"
            @click="handleUpdate()"
            v-hasPermi="['*:*:*']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="Delete"
            :disabled="multiple"
            @click="handleDelete()"
            v-hasPermi="['*:*:*']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getDataList"/>
    </el-row>
    <!--  表格数据  -->
    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
<#list gridList as field>
      <el-table-column label="${field.fieldComment!}" align="center" prop="${field.attrName}" :show-overflow-tooltip="true"/>
</#list>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['*:*:*']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['*:*:*']">删除</el-button>
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
  <form-dialog ref="FormDialogRef" :title="title" :form-data="form" @onAmendSubmitForm="onAmendSubmitForm" @onSaveSubmitForm="onSaveSubmitForm" />
</template>

<script setup lang="ts" name="${ClassName}Index">
import { getCurrentInstance } from "vue";
import * as CurrentConstants from "@/views/${moduleName}/${className}/constants";
import FormDialog from "@/views/${moduleName}/${className}/component/FormDialog.vue";
import { ${ClassName}ApiService } from "@/api/${className}/${ClassName}ApiService";
import { ${ClassName}Entity } from "@/api/${className}/models/${ClassName}Entity";

// 弹窗 Ref
const FormDialogRef = ref();
// @ts-ignore
const { proxy } = getCurrentInstance();
// data 数据
const currentData = reactive(CurrentConstants.DATA);
const { queryParams, form, rules } = toRefs(currentData);
// 日期参数
const dateRange = ref([]);
// 搜索框显示
const showSearch = ref(true);
// 表格加载动画
const loading = ref(true);
// 表格数据
const dataList = ref<${ClassName}Entity[]>([]);
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

const serviceApi = new ${ClassName}ApiService();

onMounted(() => {
  getDataList();
})

/** 查询参数列表 */
async function getDataList() {
  loading.value = true;
  try {
    const { data } = await serviceApi.page(proxy.addDateRange(queryParams.value, dateRange.value));
    dataList.value = data.list;
    total.value = data.totalCount;
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  } finally {
    loading.value = false;
  }
}

/** 删除按钮操作 */
function handleDelete(row?: ${ClassName}Entity) {
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
  title.value = "添加基类";
}

/** 修改按钮操作 */
async function handleUpdate(row?: ${ClassName}Entity) {
  const params = row!.id || ids.value;
  try {
    const { data } = await serviceApi.detail(params);
    if (!data) return proxy.$modal.msgWarning(`未找到[<#noparse>${params}</#noparse>]的数据`);
    form.value = data;
    FormDialogRef.value.onOpen()
    title.value = "修改基类";
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
}

/**
 * 修改
 */
const onAmendSubmitForm = async (formData: ${ClassName}Entity) => {
  try {
    await serviceApi.update(formData);
    proxy.$modal.msgSuccess('修改成功');
    await getDataList();
    FormDialogRef.value.onClose();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/**
 * 新增
 */
const onSaveSubmitForm = async (formData: ${ClassName}Entity) => {
  try {
    await serviceApi.save(formData);
    proxy.$modal.msgSuccess('新增成功');
    await getDataList();
    FormDialogRef.value.onClose();
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
function handleSelectionChange(selection: ${ClassName}Entity[]) {
  ids.value = selection.map((item: ${ClassName}Entity) => item.id!);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
</script>

<style scoped lang="scss">

</style>
