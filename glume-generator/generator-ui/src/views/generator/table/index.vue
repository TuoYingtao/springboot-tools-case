<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="QueryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="表名" prop="tableName">
        <el-input
            v-model="queryParams.tableName"
            placeholder="请输入表名"
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
        <el-button type="primary" plain icon="Plus" @click="handleImport" v-hasPermi="['*:*:*']">
          导入
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Folder" :disabled="multiple" @click="handleGeneratorCode()" v-hasPermi="['*:*:*']">
          生成代码
        </el-button>
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
      <el-table-column label="表名" align="center" prop="tableName"/>
      <el-table-column label="表说明" align="center" prop="tableComment" :show-overflow-tooltip="true"/>
      <el-table-column label="类" align="center" prop="className"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="350" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="View" @click="handlePreviewCode(scope.row)" v-hasPermi="['*:*:*']">预览</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['*:*:*']">修改</el-button>
          <el-button link type="primary" icon="Folder" @click="handleGenerator(scope.row)" v-hasPermi="['*:*:*']">生成代码</el-button>
          <el-button link type="primary" icon="Refresh" @click="handleSync(scope.row)" v-hasPermi="['*:*:*']">同步</el-button>
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
  <form-dialog
      ref="FormDialogRef"
      :form-data="form"
      :type-list="typeList"
      @onSubmitField="onSubmitField"/>
  <!-- 导入弹窗 -->
  <import-dialog ref="ImportDialogRef" title="导入数据表" :datasource-list="datasourceList" @onImportTable="onImportTable" />
  <!-- 生成弹窗 -->
  <generator-dialog ref="GeneratorDialogRef" title="生成代码" :form-data="form" :base-class-list="baseClassList" @onAmendSubmitForm="onAmendSubmitForm" @onGeneratorCode="onGeneratorCode" />
  <!-- 预览代码 -->
  <preview-dialog ref="PreviewDialogRef" title="预览代码" :preview-code-map="previewCodeMap" />
</template>

<script setup lang="ts" name="GeneratorCode">
import { getCurrentInstance } from "vue";
import * as CurrentConstants from "@/views/generator/table/constants";
import { parseTime } from "@/utils";
import { GeneratorTypeEnum } from "@/enum";
import { TableEntity, TableField } from "@/api/generator/models/TableEntity";
import { BaseClassEntity } from "@/api/generator/models/BaseClassEntity";
import { DatasourceEntity } from "@/api/generator/models/DatasourceEntity";
import { TableApiService } from "@/api/generator/TableApiService";
import { DatasourceApiService } from "@/api/generator/DatasourceApiService";
import { FieldTypeApiService } from "@/api/generator/FieldTypeApiService";
import { BaseClassApiService } from "@/api/generator/BaseClassApiService";
import { GeneratorApiService } from "@/api/generator/GeneratorApiService";
import ImportDialog from "@/views/generator/table/component/ImportDialog.vue";
import GeneratorDialog from "@/views/generator/table/component/GeneratorDialog.vue";
import FormDialog from "@/views/generator/table/component/FormDialog.vue";
import PreviewDialog from "@/views/generator/table/component/PreviewDialog.vue";

// 弹窗 Ref
const FormDialogRef = ref();
const ImportDialogRef = ref();
const GeneratorDialogRef = ref();
const PreviewDialogRef = ref();
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
const dataList = ref<TableEntity[]>([]);
// 总条数
const total = ref(0);
// 全局修改按钮状态
const single = ref(true);
// 全局批量按钮选中
const multiple = ref(true);
// 批量选中ID
const ids = ref<number[]>([]);

const serviceApi = new TableApiService();
const datasourceApi = new DatasourceApiService();
const fieldTypeApi = new FieldTypeApiService();
const baseClassApi = new BaseClassApiService();
const generatorApi = new GeneratorApiService();

// 数据源列表
const datasourceList = ref<DatasourceEntity[]>([]);
// 类型列表
const typeList = ref<string[]>([]);
// 基类列表
const baseClassList = ref<BaseClassEntity[]>([]);
// 预览代码
const previewCodeMap = ref<Record<string, string>>();

onMounted(() => {
  getDataList();
  getDatasourceList();
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

/** 数据源列表 */
async function getDatasourceList() {
  try {
    const { data } = await datasourceApi.list();
    datasourceList.value = data;
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
}

/** 删除按钮操作 */
function handleDelete(row?: TableEntity) {
  const params = row ? [row!.id] : ids.value;
  proxy.$modal.confirm('是否确认删除编号为"' + params + '"的数据项？').then(function () {
    return serviceApi.delete(params);
  }).then(() => {
    getDataList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/** 导入按钮操作 */
async function handleImport() {
  await getDatasourceList();
  ImportDialogRef.value.onOpen();
}

/** 修改按钮操作 */
async function handleUpdate(row?: TableEntity) {
  const params = row!.id || ids.value;
  try {
    const { data } = await serviceApi.detail(params);
    typeList.value = Array.from(new Set((await fieldTypeApi.list()).data.map((item) => item.attrType)));
    if (!data) return proxy.$modal.msgWarning(`未找到[${params}]的数据`);
    form.value = data;
    FormDialogRef.value.onOpen()
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
}

/**
 * 预览代码按钮操作
 */
const handlePreviewCode = async (row: TableEntity) => {
  try {
    const { data } = await generatorApi.previewCode(row.id as number);
    previewCodeMap.value = data;
    PreviewDialogRef.value.onOpen();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/** 生成代码按钮操作 */
const handleGenerator = async (row?: TableEntity) => {
  const params = row!.id || ids.value;
  try {
    const { data } = await serviceApi.detail(params);
    baseClassList.value = (await baseClassApi.list()).data;
    form.value = data;
    GeneratorDialogRef.value.onOpen();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/**
 * 修改
 */
const onAmendSubmitForm = async (formData: TableEntity) => {
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
 * 修改
 */
const onSubmitField = async (formData: TableField[]) => {
  try {
    await serviceApi.updateTableField(formData);
    proxy.$modal.msgSuccess('修改成功');
    await getDataList();
    FormDialogRef.value.onClose();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/** 导入表数据 */
const onImportTable = async (e: Record<string, any>) => {
  try {
    const { data } = await serviceApi.importTable(e.dataSourceId, e.tableNames);
    proxy.$modal.msgSuccess(data);
    await getDataList();
    ImportDialogRef.value.onClose();
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

/** 同步表数据 */
const handleSync = async (formData: TableEntity) => {
  proxy.$modal.confirm('是否确认同步表名为"' + formData.tableName + '"的数据项？').then(async function () {
    const { data } = await serviceApi.sync(formData.id!);
    return data;
  }).then((data: string) => {
    getDataList();
    proxy.$modal.msgSuccess(data);
  }).catch(() => {
  });
};

/** 生成代码 */
const onGeneratorCode = async (formData: TableEntity) => {
  try {
    await onAmendSubmitForm(formData);
    proxy.$modal.loading('代码生成中...');
    if (formData.generatorType === GeneratorTypeEnum.ZIP) {
      await generatorApi.download('' + formData.id!);
      return;
    }
    const { data } = await generatorApi.code([formData.id!]);
    proxy.$modal.msgSuccess(data);
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  } finally {
    proxy.$modal.closeLoading();
  }
};

const handleGeneratorCode = async () => {
  try {
    proxy.$modal.loading('代码生成中...');
    const { data } = await generatorApi.code(ids.value);
    proxy.$modal.msgSuccess(data);
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  } finally {
    proxy.$modal.closeLoading();
  }
}

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
function handleSelectionChange(selection: TableEntity[]) {
  ids.value = selection.map((item: TableEntity) => item.id!);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}
</script>

<style scoped lang="scss">

</style>
