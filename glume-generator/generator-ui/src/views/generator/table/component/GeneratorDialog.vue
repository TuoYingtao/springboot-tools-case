<template>
  <el-dialog :title="title" v-model="open" width="800px" draggable append-to-body @close="onClose">
    <el-form ref="FormRef" :model="formData" :rules="rules" label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="表名" prop="tableName">
            <el-input v-model="formData.tableName" disabled placeholder="请输入表名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="说明" prop="tableComment">
            <el-input v-model="formData.tableComment" placeholder="请输入说明" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="className">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="类名如：UserEntity" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  类名<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.className" placeholder="请输入类名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="baseclassId">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="实体类继承的基类" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  继承<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-select style="width: 100%" v-model="formData.baseclassId" clearable placeholder="选择继承">
              <el-option label="不继承" :value="-99" />
              <el-option v-for="item in baseClassList" :key="item.id" :label="item.code" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="moduleName">
            <template #label>
              <el-tooltip
                class="box-item"
                effect="dark"
                content="当前的表输入那个业务模块如：user"
                :show-after="700"
                placement="top">
                <div class="flex justify-center items-center">
                  模块名<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.moduleName" placeholder="请输入模块名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="functionName">
            <template #label>
              <el-tooltip
                class="box-item"
                effect="dark"
                content="功能名将使用在：路由、权限、api地址"
                :show-after="700"
                placement="top">
                <div class="flex justify-center items-center">
                  功能名<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.functionName" placeholder="请输入功能名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="packageName">
            <template #label>
              <el-tooltip
                class="box-item"
                effect="dark"
                content="项目包名：com.project.net"
                :show-after="700"
                placement="top">
                <div class="flex justify-center items-center">
                  项目包名<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.packageName" placeholder="请输入项目包名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="version">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="版本号" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  版本号<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.version" placeholder="请输入版本号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="author">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="默认作者" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  默认作者<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.author" placeholder="请输入默认作者" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="email">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="作者邮箱" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  作者邮箱<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.email" placeholder="请输入作者邮箱" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="commonPackagePath">
            <template #label>
              <el-tooltip
                class="box-item"
                effect="dark"
                content="工具包路径一般是项目包名+模块名如：com.project.net.user"
                :show-after="700"
                placement="top">
                <div class="flex justify-center items-center">
                  工具包路径<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.commonPackagePath" placeholder="请输入工具包路径" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="enableBaseService">
            <template #label>
              <el-tooltip
                class="box-item"
                effect="dark"
                content="三层架构是否继承基类"
                :show-after="700"
                placement="top">
                <div class="flex justify-center items-center">
                  业务架构继承<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-radio-group v-model="formData.enableBaseService">
              <el-radio :label="0">继承</el-radio>
              <el-radio :label="1">不继承</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="generatorType">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="生成方式" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  生成方式<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-radio-group v-model="formData.generatorType">
              <el-radio :label="GeneratorTypeEnum.ZIP">zip压缩包</el-radio>
              <el-radio :label="GeneratorTypeEnum.CUSTOM_PATH">自定义路径</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="formLayout">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="生成方式" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  表单布局<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-radio-group v-model="formData.formLayout">
              <el-radio :label="FormLayoutEnum.ONE_ROW">一列</el-radio>
              <el-radio :label="FormLayoutEnum.TWO_ROW">两列</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="24" v-show="formData.generatorType === GeneratorTypeEnum.CUSTOM_PATH">
          <el-form-item prop="backendPath">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="后端生成路径" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  后端生成路径<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.backendPath" placeholder="请输入后端生成路径" />
          </el-form-item>
        </el-col>
        <el-col :span="24" v-show="formData.generatorType === GeneratorTypeEnum.CUSTOM_PATH">
          <el-form-item prop="frontendPath">
            <template #label>
              <el-tooltip class="box-item" effect="dark" content="前端生成路径" :show-after="700" placement="top">
                <div class="flex justify-center items-center">
                  前端生成路径<el-icon><QuestionFilled /></el-icon>
                </div>
              </el-tooltip>
            </template>
            <el-input v-model="formData.frontendPath" placeholder="请输入前端生成路径" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="success" @click="onGeneratorCode">生成代码</el-button>
        <el-button type="primary" @click="submitForm">保 存</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts" name="FormDialog">
import { DATA, Props } from '@/views/generator/table/constants';
import { FormLayoutEnum, GeneratorTypeEnum } from '@/enum';

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
  baseClassList: () => [],
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
      console.log(props.formData);
      emit(key as never, props.formData);
    }
  });
};

/**
 * 生成代码
 */
const onGeneratorCode = () => {
  FormRef.value.validate((valid: boolean) => {
    if (valid) {
      emit('onGeneratorCode' as never, props.formData);
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
