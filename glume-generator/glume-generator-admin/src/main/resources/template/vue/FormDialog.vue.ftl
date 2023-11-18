<template>
  <el-dialog :title="title" v-model="open" width="500px" draggable append-to-body @close="onClose">
    <el-form ref="FormRef" :model="formData" :rules="rules" label-width="100px">
<#list formList as field>
      <el-form-item prop="${field.attrName}">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="${field.fieldComment!}" :show-after="700" placement="top">
            <div class="flex justify-center items-center">${field.fieldComment!}<el-icon><QuestionFilled /></el-icon></div>
          </el-tooltip>
        </template>
  <#if field.formType == 'text'>
        <el-input v-model="formData.${field.attrName}" placeholder="请输入${field.fieldComment!}" />
  <#elseif field.formType == 'textarea'>
        <el-input type="textarea" v-model="formData.${field.attrName}" placeholder="请输入${field.fieldComment!}" />
  <#elseif field.formType == 'editor'>
       <el-input type="textarea" v-model="formData.${field.attrName}" placeholder="请输入${field.fieldComment!}" />
  <#elseif field.formType == 'select'>
        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
          <el-select v-model="formData.${field.attrName}" placeholder="请选择">
            <el-option label="请选择" value="0"></el-option>
          </el-select>
        </el-form-item>
  <#elseif field.formType == 'radio'>
        <el-form-item label="${field.fieldComment!}" prop="${field.attrName}">
          <el-radio-group v-model="formData.${field.attrName}">
            <el-radio :label="0">启用</el-radio>
            <el-radio :label="1">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
  <#elseif field.formType == 'checkbox'>
        <el-checkbox-group v-model="formData.${field.attrName}">
          <el-checkbox label="启用" name="type"></el-checkbox>
          <el-checkbox label="禁用" name="type"></el-checkbox>
        </el-checkbox-group>
  <#else>
        <el-input v-model="formData.${field.attrName}" placeholder="${field.fieldComment!}"></el-input>
  </#if>
       </el-form-item>
</#list>
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
import { DATA, Props } from "@/views/${moduleName}/${className}/constants";

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
  })
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
const onOpen = () => open.value = true;

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
defineExpose({ onOpen, onClose, resetForm })
</script>

<style scoped lang="scss">

</style>
