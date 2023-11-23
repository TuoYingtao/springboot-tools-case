<template>
  <el-dialog :title="title" v-model="open" width="700px" draggable append-to-body @close="onClose">
    <el-row :gutter="20">
      <el-col :span="3">数据源</el-col>
      <el-col :span="21">
        <el-select v-model="selectDatasourceId" style="width: 100%" placeholder="请选择数据源">
          <el-option v-for="item in datasourceList" :key="item.id" :label="item.connName" :value="item.id" />
        </el-select>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24">
        <el-table
          ref="multipleTableRef"
          :data="tableList"
          style="width: 100%"
          @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column label="表名" align="center" prop="tableName" />
          <el-table-column label="表说明" align="center" prop="tableComment" show-overflow-tooltip />
        </el-table>
      </el-col>
    </el-row>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="ImportDialog">
import { ImportProps } from '@/views/generator/table/constants';
import { DatasourceApiService } from '@/api/generator/DatasourceApiService';
import { DBTableEntity } from '@/api/generator/models/DatasourceEntity';

// @ts-ignore
const { proxy } = getCurrentInstance();
const emit = defineEmits([]);
// 打开弹窗状态
const open = ref(false);
const props: ImportProps = withDefaults(defineProps<ImportProps>(), {
  title: '',
  datasourceList: () => [],
});

let datasourceApi = new DatasourceApiService();

const selectDatasourceId = ref<number | undefined>();
const tableList = ref<DBTableEntity[]>([]);
const tableNames = ref<string[]>([]);

watch(
  () => selectDatasourceId.value,
  (newVal) => {
    if (selectDatasourceId.value !== undefined) {
      getDBTableList();
    }
  },
);

/**
 * 查询数据库所以表信息
 */
const getDBTableList = async () => {
  try {
    const { data } = await datasourceApi.tableList(selectDatasourceId.value as number);
    tableList.value = data;
  } catch (e: any) {
    proxy.$modal.msgError(e.message);
  }
};

const handleSelectionChange = (selection: DBTableEntity[]) => {
  tableNames.value = selection.map((item: DBTableEntity) => item.tableName);
};

/**
 * 提交
 */
const submitForm = () => {
  emit('onImportTable' as never, { dataSourceId: selectDatasourceId.value, tableNames: tableNames.value });
};

/**
 * 关闭
 */
const cancel = () => {
  onClose();
};

/**
 * 打开弹窗
 */
const onOpen = () => (open.value = true);

/**
 * 关闭弹窗
 */
const onClose = () => {
  open.value = false;
  resetForm();
};

/**
 * 重置表单
 */
const resetForm = () => {
  selectDatasourceId.value = undefined;
  tableList.value = [];
  tableNames.value = [];
};
defineExpose({ onOpen, onClose, resetForm });
</script>

<style scoped lang="scss">
.el-row {
  margin-bottom: 20px;
}
</style>
