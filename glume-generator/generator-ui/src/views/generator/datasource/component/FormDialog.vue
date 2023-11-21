<template>
  <el-dialog :title="title" v-model="open" width="500px" draggable append-to-body @close="onClose">
    <el-form ref="FormRef" :model="formData" :rules="rules" label-width="90px">
      <el-form-item prop="connName">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="连接名如：generator" :show-after="700" placement="top">
            <div class="flex justify-center items-center">连接名<el-icon><QuestionFilled /></el-icon></div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.connName" placeholder="请输入连接名" />
      </el-form-item>
      <el-form-item prop="dbType">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="数据库类型如：MySQL" :show-after="700" placement="top">
            <div class="flex justify-center items-center">数据库类型<el-icon><QuestionFilled /></el-icon></div>
          </el-tooltip>
        </template>
        <el-select v-model="formData.dbType" style="width: 100%" placeholder="请输入数据库类型">
          <el-option v-for="item in dbTypeOptions" :key="item.id" :label="item.label" :value="item.label" />
        </el-select>
      </el-form-item>
      <el-form-item prop="connUrl">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="数据库URL" :show-after="700" placement="top">
            <div class="flex justify-center items-center">数据库URL<el-icon><QuestionFilled /></el-icon></div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.connUrl" placeholder="请输入数据库URL" />
      </el-form-item>
      <el-form-item prop="username">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="用户名" :show-after="700" placement="top">
            <div class="flex justify-center items-center">用户名<el-icon><QuestionFilled /></el-icon></div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.username" placeholder="请输入用户名" />
      </el-form-item>
      <el-form-item prop="password">
        <template #label>
          <el-tooltip class="box-item" effect="dark" content="密码" :show-after="700" placement="top">
            <div class="flex justify-center items-center">密码<el-icon><QuestionFilled /></el-icon></div>
          </el-tooltip>
        </template>
        <el-input v-model="formData.password" placeholder="请输入密码" />
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
import { DATA, dbTypeOptions, Props } from "@/views/generator/datasource/constants";
import { CryptoJSUtils } from "@/utils/security/cryptoJS";

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
      props.formData.password = CryptoJSUtils.encrypt(props.formData.password);
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
  FormRef.value.clearValidate();
  props.formData.id = -1;
};
defineExpose({ onOpen, onClose, resetForm })
</script>

<style scoped lang="scss">

</style>
