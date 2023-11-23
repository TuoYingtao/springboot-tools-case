import defaultSettings from '@/settings';
import { useDynamicTitle } from '@/utils/theme/dynamicTitle';

// 默认设置信息
const { sideTheme, theme, showSettings, topNav, tagsView, fixedHeader, sidebarLogo, dynamicTitle } = defaultSettings;
// 本地设置信息
const layoutSetting = localStorage.getItem('layout-setting') || '';
// 解析本地设置信息
const storageSetting = layoutSetting != '' ? JSON.parse(layoutSetting) : '';

const useSettingsStore = defineStore('settings', {
  state: () =>
    ({
      title: '',
      theme: storageSetting.theme || theme,
      sideTheme: storageSetting.sideTheme || sideTheme,
      showSettings: showSettings,
      topNav: storageSetting.topNav === undefined ? topNav : storageSetting.topNav,
      tagsView: storageSetting.tagsView === undefined ? tagsView : storageSetting.tagsView,
      fixedHeader: storageSetting.fixedHeader === undefined ? fixedHeader : storageSetting.fixedHeader,
      sidebarLogo: storageSetting.sidebarLogo === undefined ? sidebarLogo : storageSetting.sidebarLogo,
      dynamicTitle: storageSetting.dynamicTitle === undefined ? dynamicTitle : storageSetting.dynamicTitle,
    }) as Settings,
  actions: {
    // 修改布局设置
    changeSetting(data: Record<string, any>) {
      const { key, value } = data;
      if (Object.hasOwn(this, key)) {
        this[key] = value;
      }
    },
    // 设置网页标题
    setTitle(title: string) {
      this.title = title;
      useDynamicTitle();
    },
  },
});

export default useSettingsStore;
