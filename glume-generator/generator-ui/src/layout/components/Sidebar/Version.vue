<template>
  <div
    class="sidebar-version-container"
    :class="{ collapse: collapse }"
    :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
    <transition name="sidebarversionFade">
      <div v-if="collapse" class="sidebar-version-link">
        <div class="sidebar-version">{{ version }}</div>
      </div>
      <div v-else class="sidebar-version-link flex justify-center items-center">
        <h1
          class="sidebar-title"
          :style="{
            color: sideTheme === 'theme-dark' ? variables.versionTitleColor : variables.versionLightTitleColor,
          }">
          {{ title }}
        </h1>
        <div class="sidebar-version">{{ version }}</div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import variables from '@/assets/styles/variables.module.scss';
import useSettingsStore from '@/stores/modules/settings';
import { VERSION } from '@/config/global';

defineProps({
  collapse: {
    type: Boolean,
    required: true,
  },
});

const title = import.meta.env.VITE_APP_NAME;
const version = VERSION;
const settingsStore = useSettingsStore();
const sideTheme = computed(() => settingsStore.sideTheme);
</script>

<style lang="scss" scoped>
.sidebarversionFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarversionFade-enter,
.sidebarversionFade-leave-to {
  opacity: 0;
}

.sidebar-version-container {
  position: relative;
  width: 100%;
  height: 35px;
  line-height: 35px;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;
  border-top: 1px solid rgb(45, 56, 79);

  & .sidebar-version-link {
    height: 100%;
    width: 100%;

    & .sidebar-title {
      padding-right: 4px;
    }

    & .sidebar-version,
    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: rgb(31, 39, 59);
      font-weight: 500;
      line-height: 35px;
      font-size: 14px;
      font-family:
        Avenir,
        Helvetica Neue,
        Arial,
        Helvetica,
        sans-serif;
      vertical-align: middle;
    }
  }

  &.collapse {
    .sidebar-version {
      margin-right: 0px;
    }
  }
}
</style>
