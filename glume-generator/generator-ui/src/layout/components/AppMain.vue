<template>
  <section class="app-main">
    <el-config-provider :locale="locale" :size="size" >
      <router-view v-slot="{ Component, route }" >
        <transition name="fade-transform" :mode="mode">
          <keep-alive :include="tagsViewStore.cachedViews">
            <component v-if="!route.meta.link" :is="Component" :key="route.path"/>
          </keep-alive>
        </transition>
      </router-view>
    </el-config-provider>
    <iframe-toggle />
  </section>
</template>

<script setup>
import iframeToggle from "./IframeToggle/index"
import useTagsViewStore from '@/stores/modules/tagsView'
import locale from "element-plus/es/locale/lang/zh-cn"; // 中文语言
import useAppStore from '@/stores/modules/app'

const tagsViewStore = useTagsViewStore()

const size = computed(() => useAppStore().size || 'small');

// 路由中添加key解决切换路由时页面不展示内容，刷新后才展示内容问题
const mode = computed(() => {
  return import.meta.env.VITE_APP_ENV === 'production' ? 'out-in' : '';
});
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;
}

.fixed-header + .app-main {
  padding-top: 50px;
}

.hasTagsView {
  .app-main {
    /* 84 = navbar + tags-view = 50 + 34 */
    min-height: calc(100vh - 84px - 35px);
  }

  .fixed-header + .app-main {
    padding-top: 84px;
  }
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 6px;
  }
}

::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>

