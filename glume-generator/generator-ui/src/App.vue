<template>
  <RouterView />
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
import useSettingsStore from '@/stores/modules/settings'
import { handleThemeStyle } from '@/utils/theme/theme'

// @ts-ignore
const { proxy } = getCurrentInstance();

onMounted(() => {
  proxy.HTMLTitle()
  nextTick(() => {
    // 初始化主题样式
    handleThemeStyle(useSettingsStore().theme)
  })
})
</script>

<style scoped>

</style>
