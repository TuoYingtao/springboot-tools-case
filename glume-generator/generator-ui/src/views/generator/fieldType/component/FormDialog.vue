<template>
  <el-dialog :title="title" v-model="open" width="500px" draggable append-to-body @close="onClose">
    <el-form ref="FormRef" :model="formData" :rules="rules" label-width="100px">
      <el-form-item prop="columnType">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="字段类型" :show-after="700" placement="top">
            <div class="flex justify-center items-center">
              字段类型<el-icon><QuestionFilled /></el-icon>
            </div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.columnType" placeholder="请输入字段类型" />
      </el-form-item>
      <el-form-item prop="attrType">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="属性类型如：Integer" :show-after="700" placement="top">
            <div class="flex justify-center items-center">
              属性类型<el-icon><QuestionFilled /></el-icon>
            </div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.attrType" placeholder="请输入属性类型" />
      </el-form-item>
      <el-form-item prop="packageName">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="基类所在的包名路径" :show-after="700" placement="top">
            <div class="flex justify-center items-center">
              属性包名<el-icon><QuestionFilled /></el-icon>
            </div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.packageName" placeholder="请输入属性包名" />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="FormDialog">
import { DATA, Props } from '@/views/generator/fieldType/constants';

const emit = defineEmits([]);
// 表单Ref
const FormRef = ref();
// 常量
const { form, rules } = DATA;
// 打开弹窗状态
const open = ref(false);
const props: Props = withDefaults(defineProps<Props>(), {
  title: '',
  formData: () => DATA.form,
});

/**
 * 提交
 */
const submitForm = () => {
  FormRef.value.validate((valid: boolean) => {
    if (valid) {
      const key = props.formData.id !== -1 ? 'onAmendSubmitForm' : 'onSaveSubmitForm';
      if (props.formData.hasOwnProperty('id') && props.formData.id === -1) {
        props.formData.id = undefined;
        props.formData = JSON.parse(JSON.stringify(props.formData));
      }
      emit(key as never, props.formData);
    }
  });
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
  FormRef.value.resetFields();
  props.formData.id = -1;
};
defineExpose({ onOpen, onClose, resetForm });
</script>

<style scoped lang="scss"></style>
