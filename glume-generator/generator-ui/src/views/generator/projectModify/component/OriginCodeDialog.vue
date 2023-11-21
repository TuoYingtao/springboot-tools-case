<template>
  <el-dialog :title="title" v-model="open" width="700px" draggable append-to-body @close="onClose">
    <el-form ref="FormRef" :model="formData" :rules="rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目名" prop="projectName">
            <el-tooltip class="box-item" effect="dark" content="项目名如：glume-boot" :show-after="700" placement="top">
              <el-input v-model="formData.projectName" disabled placeholder="请输入项目名" />
            </el-tooltip>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目标识" prop="projectCode">
            <el-tooltip class="box-item" effect="dark" content="项目标识如：glume" :show-after="700" placement="top">
              <el-input v-model="formData.projectCode" disabled placeholder="请输入项目标识" />
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目包名" prop="projectPackage">
            <el-tooltip class="box-item" effect="dark" content="项目包名如：com.glume" :show-after="700" placement="top">
              <el-input v-model="formData.projectPackage" disabled placeholder="请输入项目包名" />
            </el-tooltip>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目路径" prop="projectPath">
            <el-tooltip class="box-item" effect="dark" content="项目源码下载路径" :show-after="700" placement="top">
              <el-input v-model="formData.projectPath" disabled placeholder="请输入项目路径" />
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider>变更后的信息</el-divider>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item label="项目名" prop="modifyProjectName">
            <el-tooltip class="box-item" effect="dark" content="项目名如：glume-boot" :show-after="700" placement="top">
              <el-input v-model="formData.modifyProjectName" placeholder="请输入项目名" />
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="项目标识" prop="modifyProjectCode">
            <el-tooltip class="box-item" effect="dark" content="项目标识如：glume" :show-after="700" placement="top">
              <el-input v-model="formData.modifyProjectCode" placeholder="请输入项目标识" />
            </el-tooltip>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="项目包名" prop="modifyProjectPackage">
            <el-tooltip class="box-item" effect="dark" content="项目包名如：com.glume" :show-after="700" placement="top">
              <el-input v-model="formData.modifyProjectPackage" placeholder="请输入项目包名" />
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="排除文件" prop="exclusions">
            <el-tooltip class="box-item" effect="dark" content="排除文件如：多个文件之间用英文逗号分割" :show-after="700" placement="top">
              <el-input v-model="formData.exclusions" placeholder="请输入排除文件" />
            </el-tooltip>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="变更文件" prop="modifySuffix">
            <el-tooltip class="box-item" effect="dark" content="变更文件如：多个文件之间用英文逗号分割" :show-after="700" placement="top">
              <el-input v-model="formData.modifySuffix" placeholder="请输入变更文件" />
            </el-tooltip>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitForm">下 载</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="FormDialog">
import { DATA, Props } from "@/views/generator/projectModify/constants";

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
      emit('onDownload', props.formData);
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
