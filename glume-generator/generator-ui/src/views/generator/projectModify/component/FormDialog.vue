<template>
  <el-dialog :title="title" v-model="open" width="500px" draggable append-to-body @close="onClose">
    <el-form ref="FormRef" :model="formData" :rules="rules" label-width="80px">
      <el-form-item prop="projectName">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="项目名如：glume-boot" :show-after="700" placement="top">
            <div class="flex justify-center items-center">
              项目名<el-icon><QuestionFilled /></el-icon>
            </div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.projectName" placeholder="请输入项目名" />
      </el-form-item>
      <el-form-item prop="projectCode">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="项目标识如：glume" :show-after="700" placement="top">
            <div class="flex justify-center items-center">
              项目名<el-icon><QuestionFilled /></el-icon>
            </div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.projectCode" placeholder="请输入项目标识" />
      </el-form-item>
      <el-form-item prop="projectPackage">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="项目包名如：com.glume" :show-after="700" placement="top">
            <div class="flex justify-center items-center">
              项目包名<el-icon><QuestionFilled /></el-icon>
            </div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.projectPackage" placeholder="请输入项目包名" />
      </el-form-item>
      <el-form-item prop="projectPath">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="项目路径" :show-after="700" placement="top">
            <div class="flex justify-center items-center">
              项目路径<el-icon><QuestionFilled /></el-icon>
            </div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.projectPath" type="textarea" placeholder="请输入项目路径" />
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
import { DATA, Props } from '@/views/generator/projectModify/constants';

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
  FormRef.value.clearValidate();
  props.formData.id = -1;
};
defineExpose({ onOpen, onClose, resetForm });
</script>

<style scoped lang="scss"></style>
