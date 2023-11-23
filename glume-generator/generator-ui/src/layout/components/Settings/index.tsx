import { defineComponent, Ref } from 'vue';

import './index.scss';
import darkSvg from '@/assets/images/dark.svg';
import lightSvg from '@/assets/images/light.svg';

import { useDynamicTitle } from '@/utils/theme/dynamicTitle';
import useAppStore from '@/stores/modules/app';
import useSettingsStore from '@/stores/modules/settings';
import usePermissionStore from '@/stores/modules/permission';
import { handleThemeStyle } from '@/utils/theme/theme';
import { ElButton, ElColorPicker, ElDivider, ElDrawer, ElSwitch } from 'element-plus';

export default defineComponent({
  name: 'Settings',
  setup(props, { expose }) {
    // @ts-ignore
    const { proxy } = getCurrentInstance();
    const appStore = useAppStore();
    const settingsStore = useSettingsStore();
    const permissionStore = usePermissionStore();
    const showSettings = ref(false);
    const theme = ref(settingsStore.theme);
    const sideTheme = ref(settingsStore.sideTheme);
    const storeSettings = computed(() => settingsStore);
    const predefineColors = ref([
      '#409EFF',
      '#ff4500',
      '#ff8c00',
      '#ffd700',
      '#90ee90',
      '#00ced1',
      '#1e90ff',
      '#c71585',
    ]);

    /** 是否需要topnav */
    const topNav = computed({
      get: () => storeSettings.value.topNav,
      set: (val) => {
        settingsStore.changeSetting({ key: 'topNav', value: val });
        if (!val) {
          appStore.toggleSideBarHide(false);
          permissionStore.setSidebarRouters(permissionStore.defaultRoutes);
        }
      },
    });
    /** 是否需要tagview */
    const tagsView = computed({
      get: () => storeSettings.value.tagsView,
      set: (val) => {
        settingsStore.changeSetting({ key: 'tagsView', value: val });
      },
    });
    /**是否需要固定头部 */
    const fixedHeader = computed({
      get: () => storeSettings.value.fixedHeader,
      set: (val) => {
        settingsStore.changeSetting({ key: 'fixedHeader', value: val });
      },
    });
    /**是否需要侧边栏的logo */
    const sidebarLogo = computed({
      get: () => storeSettings.value.sidebarLogo,
      set: (val) => {
        settingsStore.changeSetting({ key: 'sidebarLogo', value: val });
      },
    });
    /**是否需要侧边栏的动态网页的title */
    const dynamicTitle = computed({
      get: () => storeSettings.value.dynamicTitle,
      set: (val) => {
        settingsStore.changeSetting({ key: 'dynamicTitle', value: val });
        // 动态设置网页标题
        useDynamicTitle();
      },
    });

    function themeChange(val: string | null) {
      if (val != null) {
        settingsStore.changeSetting({ key: 'theme', value: val });
        theme.value = val;
        handleThemeStyle(val);
      }
    }

    function handleTheme(val: string) {
      settingsStore.changeSetting({ key: 'sideTheme', value: val });
      sideTheme.value = val;
    }

    function saveSetting() {
      proxy.$modal.loading('正在保存到本地，请稍候...');
      let layoutSetting = {
        topNav: storeSettings.value.topNav,
        tagsView: storeSettings.value.tagsView,
        fixedHeader: storeSettings.value.fixedHeader,
        sidebarLogo: storeSettings.value.sidebarLogo,
        dynamicTitle: storeSettings.value.dynamicTitle,
        sideTheme: storeSettings.value.sideTheme,
        theme: storeSettings.value.theme,
      };
      localStorage.setItem('layout-setting', JSON.stringify(layoutSetting));
      setTimeout(proxy.$modal.closeLoading(), 1000);
    }

    function resetSetting() {
      proxy.$modal.loading('正在清除设置缓存并刷新，请稍候...');
      localStorage.removeItem('layout-setting');
      setTimeout('window.location.reload()', 1000);
    }

    function openSetting() {
      showSettings.value = true;
    }

    expose({
      openSetting,
    });

    /** 选色器 */
    const renderColorPicker = () => (
      <ElColorPicker
        v-model={theme.value}
        predefine={predefineColors.value}
        onChange={(e: string | null) => themeChange(e)}
      />
    );

    /** 分割线 */
    const renderDivider = () => <ElDivider />;

    /** 开关 */
    const renderSwitch = (val: Ref<boolean | string>) => <ElSwitch v-model={[val.value, 'model-value']} />;

    /** 按钮 */
    const renderButton = (
      btnName: string,
      onCallback: Function,
      plain: boolean,
      icon?: string,
      type: any = 'primary',
    ) => (
      <ElButton type={type} plain={plain} icon={icon} onClick={() => onCallback()}>
        {btnName}
      </ElButton>
    );

    /** 根节点 */
    const renderRootNode = () => (
      <ElDrawer
        v-model={showSettings.value}
        withHeader={false}
        direction="rtl"
        size="300px"
        v-slots={{
          default: renderChildNode,
        }}
      />
    );

    /** 子节点 */
    const renderChildNode = () => (
      <>
        <div class="setting-drawer-title">
          <h3 class="drawer-title">主题风格设置</h3>
        </div>
        <div class="setting-drawer-block-checbox">
          <div class="setting-drawer-block-checbox-item" onClick={() => handleTheme('theme-dark')}>
            <img src={darkSvg} alt="dark" />
            {sideTheme.value === 'theme-dark' && (
              <div class="setting-drawer-block-checbox-selectIcon" style="display: block;">
                <i aria-label="图标: check" class="anticon anticon-check">
                  <svg
                    viewBox="64 64 896 896"
                    data-icon="check"
                    width="1em"
                    height="1em"
                    v-model:fill={theme.value}
                    aria-hidden="true"
                    focusable="false"
                    class>
                    <path d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z" />
                  </svg>
                </i>
              </div>
            )}
          </div>
          <div class="setting-drawer-block-checbox-item" onClick={() => handleTheme('theme-light')}>
            <img src={lightSvg} alt="light" />
            {sideTheme.value === 'theme-light' && (
              <div class="setting-drawer-block-checbox-selectIcon" style="display: block;">
                <i aria-label="图标: check" class="anticon anticon-check">
                  <svg
                    viewBox="64 64 896 896"
                    data-icon="check"
                    width="1em"
                    height="1em"
                    v-model:fill={theme.value}
                    aria-hidden="true"
                    focusable="false"
                    class>
                    <path d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474a32 32 0 0 0-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9l273.9 347c12.8 16.2 37.4 16.2 50.3 0l488.4-618.9c4.1-5.1.4-12.8-6.3-12.8z" />
                  </svg>
                </i>
              </div>
            )}
          </div>
        </div>
        <div class="drawer-item">
          <span>主题颜色</span>
          <span class="comp-style">{renderColorPicker()}</span>
        </div>
        {renderDivider()}
        <h3 class="drawer-title">系统布局配置</h3>
        <div class="drawer-item">
          <span>开启 TopNav</span>
          <span class="comp-style">{renderSwitch(topNav)}</span>
        </div>
        <div class="drawer-item">
          <span>开启 Tags-Views</span>
          <span class="comp-style">{renderSwitch(tagsView)}</span>
        </div>

        <div class="drawer-item">
          <span>固定 Header</span>
          <span class="comp-style">{renderSwitch(fixedHeader)}</span>
        </div>

        <div class="drawer-item">
          <span>显示 Logo</span>
          <span class="comp-style">{renderSwitch(sidebarLogo)}</span>
        </div>

        <div class="drawer-item">
          <span>动态标题</span>
          <span class="comp-style">{renderSwitch(dynamicTitle)}</span>
        </div>
        {renderDivider()}
        {renderButton('保存配置', saveSetting, true, 'DocumentAdd')}
        {renderButton('重置配置', resetSetting, true, 'Refresh', '')}
      </>
    );

    return { renderRootNode };
  },
  render() {
    return this.renderRootNode();
  },
});
