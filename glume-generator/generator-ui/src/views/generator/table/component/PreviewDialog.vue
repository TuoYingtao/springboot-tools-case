<template>
  <el-dialog :title="title" v-model="open" width="80%" draggable append-to-body @close="onClose">
    <el-tabs v-model="activeName">
      <el-tab-pane
        v-for="(value, key) in previewCodeMap"
        :label="substringHandler(key)"
        :name="substringHandler(key)"
        :key="key">
        <el-link
          :underline="false"
          icon="el-icon-document-copy"
          v-copyText="value"
          v-copyText:callback="clipboardSuccess"
          style="float: right">
          复制
        </el-link>
        <pre><code class="hljs" v-html="highlightedCode(value, key)"></code></pre>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup lang="ts" name="PreviewDialog">
import { PreviewProps } from '@/views/generator/table/constants';
import hljs from 'highlight.js/lib/highlight';
import 'highlight.js/styles/github-gist.css';
import java from 'highlight.js/lib/languages/java';
import xml from 'highlight.js/lib/languages/xml';
import javascript from 'highlight.js/lib/languages/javascript';
import typescript from 'highlight.js/lib/languages/typescript';
import sql from 'highlight.js/lib/languages/sql';

hljs.registerLanguage('java', java);
hljs.registerLanguage('xml', xml);
hljs.registerLanguage('html', xml);
hljs.registerLanguage('vue', xml);
hljs.registerLanguage('javascript', javascript);
hljs.registerLanguage('typescript', typescript);
hljs.registerLanguage('sql', sql);

// @ts-ignore
const { proxy } = getCurrentInstance();
const emit = defineEmits([]);
// 打开弹窗状态
const open = ref(false);
const props: PreviewProps = withDefaults(defineProps<PreviewProps>(), {
  title: '',
  previewCodeMap: () => ({}),
});

const activeName = ref<string>('');

const substringHandler = computed(() => {
  return (key: string) => key.substring(key.lastIndexOf('/') + 1, key.indexOf('.ftl'));
});

watch(
  () => props.previewCodeMap,
  (newVal) => {
    if (newVal) {
      const [firstKey] = Object.keys(newVal);
      activeName.value = substringHandler.value(firstKey);
    }
  },
);

/** 高亮显示 */
function highlightedCode(code: string, key: string) {
  const vmName = key.substring(key.lastIndexOf('/') + 1, key.indexOf('.ftl'));
  var language = vmName.split('.').pop();
  const result = hljs.highlight(language, code || '', true);
  return result.value || '&nbsp;';
}

/** 复制代码成功 */
function clipboardSuccess() {
  proxy.$modal.msgSuccess('复制成功');
}

/**
 * 提交
 */
const submitForm = () => {
  emit('onImportTable' as never);
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
const resetForm = () => {};
defineExpose({ onOpen, onClose, resetForm });
</script>

<style scoped lang="scss"></style>
