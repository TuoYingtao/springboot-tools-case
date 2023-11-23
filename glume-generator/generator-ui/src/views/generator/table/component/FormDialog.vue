<template>
  <el-drawer :title="title" v-model="open" size="70%" :with-header="false">
    <el-tabs v-model="activeName" @tab-click="handleTabsClick">
      <el-tab-pane class="tabPane" label="属性设置" name="field">
        <el-table :data="attrTableData" style="width: 100%">
          <el-table-column align="center" label="#" prop="sort" width="80" />
          <el-table-column align="center" width="80">
            <template #header>
              <el-tooltip effect="dark" content="允许上下拖拽" :show-after="700" placement="top">
                <el-icon><QuestionFilled /></el-icon>
              </el-tooltip>
            </template>
            <template #default="scope">
              <div class="drag-btn">
                <el-icon><Sort /></el-icon>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="字段名" prop="fieldName" />
          <el-table-column align="center" prop="fieldComment" :show-overflow-tooltip="true">
            <template #header>
              <el-tooltip effect="dark" content="可编辑内容" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  <el-icon><Edit /></el-icon>说明
                </div>
              </el-tooltip>
            </template>
            <template #default="{ row, column, $index }">
              <div style="min-height: 10px" @click="selectAmendItem(column, $index)">
                <span v-show="selectItemKey !== `${column.property}-${$index}`">{{ row.fieldComment }}</span>
                <el-input
                  v-show="selectItemKey === `${column.property}-${$index}`"
                  v-model="row.fieldComment"
                  placeholder="请输入说明"
                  autofocus
                  @blur="selectItemKey = ''" />
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="字段类型" prop="fieldType" />
          <el-table-column align="center" label="属性名" prop="attrName">
            <template #header>
              <el-tooltip effect="dark" content="可编辑内容" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  <el-icon><Edit /></el-icon>属性名
                </div>
              </el-tooltip>
            </template>
            <template #default="{ row, column, $index }">
              <div style="min-height: 10px" @click="selectAmendItem(column, $index)">
                <span v-show="selectItemKey !== `${column.property}-${$index}`">{{ row.attrName }}</span>
                <el-input
                  v-show="selectItemKey === `${column.property}-${$index}`"
                  v-model="row.attrName"
                  placeholder="请输入属性名"
                  autofocus
                  @blur="selectItemKey = ''" />
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="属性类型" prop="attrType">
            <template #default="{ row }">
              <el-select v-model="row.attrType" placeholder="选择属性类型">
                <el-option v-for="item in typeList" :key="item" :label="item" :value="item" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="主键" prop="primaryPk">
            <template #default="{ row }">
              <el-checkbox v-model="row.primaryPk" />
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="属性功能" name="fun">
        <el-table :data="attrTableData" style="width: 100%">
          <el-table-column align="center" label="属性名" prop="attrName" />
          <el-table-column align="center" label="自动填充" prop="autoFill">
            <template #default="{ row }">
              <el-select v-model="row.autoFill" placeholder="选择自动填充">
                <el-option v-for="item in fill" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="日期填充" prop="dateFormat">
            <template #default="{ row }">
              <el-select v-model="row.dateFill" placeholder="选择日期格式">
                <el-option v-for="item in dateFills" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="日期格式" prop="dateFormat">
            <template #default="{ row }">
              <el-select v-model="row.dateFormat" placeholder="选择日期格式">
                <el-option v-for="item in dateFormats" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="时区" prop="dateFormat">
            <template #default="{ row }">
              <el-select v-model="row.timeZone" placeholder="选择时区">
                <el-option v-for="item in timeZones" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="表单配置" name="form">
        <el-table :data="attrTableData" style="width: 100%">
          <el-table-column align="center" label="属性名" prop="attrName" />
          <el-table-column align="center" label="说明" prop="fieldComment" :show-overflow-tooltip="true" />
          <el-table-column align="center" label="表单显示" prop="formItem">
            <template #default="{ row }">
              <el-checkbox v-model="row.formItem" />
            </template>
          </el-table-column>
          <el-table-column align="center" label="表单必填" prop="formRequired">
            <template #default="{ row }">
              <el-checkbox v-model="row.formRequired" />
            </template>
          </el-table-column>
          <el-table-column align="center" prop="fieldComment">
            <template #header>
              <el-tooltip effect="dark" content="可编辑内容" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  <el-icon><Edit /></el-icon>表单效验
                </div>
              </el-tooltip>
            </template>
            <template #default="{ row, column, $index }">
              <div style="min-height: 10px" @click="selectAmendItem(column, $index)">
                <span v-show="selectItemKey !== `${column.property}-${$index}`">{{ row.formValidator }}</span>
                <el-input
                  v-show="selectItemKey === `${column.property}-${$index}`"
                  v-model="row.formValidator"
                  placeholder="请输入表单效验"
                  autofocus
                  @blur="selectItemKey = ''" />
              </div>
            </template>
          </el-table-column>
          <el-table-column align="center" label="表单类型" prop="formType">
            <template #default="{ row }">
              <el-select v-model="row.formType" placeholder="选择表单类型">
                <el-option v-for="item in formType" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" prop="attrName">
            <template #header>
              <el-tooltip effect="dark" content="可编辑内容" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  <el-icon><Edit /></el-icon>表单字典类型
                </div>
              </el-tooltip>
            </template>
            <template #default="{ row, column, $index }">
              <div style="min-height: 10px" @click="selectAmendItem(column, $index)">
                <span v-show="selectItemKey !== `${column.property}-${$index}`">{{ row.formDict }}</span>
                <el-input
                  v-show="selectItemKey === `${column.property}-${$index}`"
                  v-model="row.formDict"
                  placeholder="请输入表单字典类型"
                  autofocus
                  @blur="selectItemKey = ''" />
              </div>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="列表配置" name="grid">
        <el-table :data="attrTableData" style="width: 100%">
          <el-table-column align="center" label="属性名" prop="attrName" />
          <el-table-column align="center" label="说明" prop="fieldComment" :show-overflow-tooltip="true" />
          <el-table-column align="center" label="列表显示" prop="gridItem">
            <template #default="{ row }">
              <el-checkbox v-model="row.gridItem" />
            </template>
          </el-table-column>
          <el-table-column align="center" label="列表排序" prop="gridSort">
            <template #default="{ row }">
              <el-checkbox v-model="row.gridSort" />
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="查询配置" name="query">
        <el-table :data="attrTableData" style="width: 100%">
          <el-table-column align="center" label="属性名" prop="attrName" />
          <el-table-column align="center" label="说明" prop="fieldComment" :show-overflow-tooltip="true" />
          <el-table-column align="center" label="查询显示" prop="queryItem">
            <template #default="{ row }">
              <el-checkbox v-model="row.queryItem" />
            </template>
          </el-table-column>
          <el-table-column align="center" label="查询方式" prop="queryType">
            <template #default="{ row }">
              <el-select v-model="row.queryType" placeholder="选择查询方式">
                <el-option v-for="item in query" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column align="center" label="查询表单类型" prop="queryFormType">
            <template #default="{ row }">
              <el-select v-model="row.queryFormType" placeholder="选择查询表单类型">
                <el-option v-for="item in formType" :key="item.value" :label="item.label" :value="item.value" />
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts" name="FormDialog">
import { TabsPaneContext } from 'element-plus';
import Sortable from 'sortablejs';
import { DATA, Props } from '@/views/generator/table/constants';
import { TableEntity, TableField } from '@/api/generator/models/TableEntity';

const emit = defineEmits([]);
// 常量
const { form, rules, fill, dateFills, dateFormats, timeZones, formType, query } = DATA;
// 打开弹窗状态
const open = ref(false);
const props: Props = withDefaults(defineProps<Props>(), {
  title: '',
  formData: () => DATA.form,
  typeList: () => [],
});

const sortable = ref();

const activeName = ref<string>('field');
// 属性设置列表数据
const attrTableData = ref<TableField[]>([]);
// 可编辑框选中
const selectItemKey = ref<string>('');

watch(
  () => props.formData,
  (newVal: TableEntity) => {
    initSortable();
    if (newVal.fieldList) {
      attrTableData.value = newVal.fieldList;
    }
  },
);
watch(
  () => activeName.value,
  (newVal) => {
    console.log(newVal);
  },
);

const initSortable = () => {
  nextTick(() => {
    const el: any = window.document.querySelector('.tabPane tbody');
    sortable.value = Sortable.create(el, {
      handle: '.drag-btn',
      onEnd: (e: any) => {
        const { newIndex, oldIndex } = e;
        const currRow = attrTableData.value.splice(oldIndex, 1)[0];
        attrTableData.value.splice(newIndex, 0, currRow);
      },
    });
  });
};

/**
 * tabs 切换事件
 */
const handleTabsClick = (tab: TabsPaneContext) => {
  console.log(tab);
};

/**
 * 选中的
 */
const selectAmendItem = (column: Record<string, any>, index: number) => {
  selectItemKey.value = `${column.property}-${index}`;
};

/**
 * 提交
 */
const submitForm = () => {
  emit('onSubmitField' as never, attrTableData.value);
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
  activeName.value = 'field';
  attrTableData.value = [];
  selectItemKey.value = '';
  props.formData = DATA.form;
  props.typeList = [];
};
defineExpose({ onOpen, onClose, resetForm });
</script>

<style scoped lang="scss">
.tabPane {
  user-select: none;
}
.drag-btn {
  cursor: move;
  font-size: 12px;
}

.el-table__row.sortable-ghost,
.el-table__row.sortable-chosen {
  background-color: #dfecfb;
}
</style>
