import { defineComponent } from 'vue';
import { ElScrollbar, ElMenu } from 'element-plus';

import Logo from './Logo.vue';
import Version from './Version.vue';
import SidebarItem from './SidebarItem';
import variables from '@/assets/styles/variables.module.scss';

import useAppStore from '@/stores/modules/app';
import useSettingsStore from '@/stores/modules/settings';
import usePermissionStore from '@/stores/modules/permission';

export default defineComponent({
  name: 'Sidebar',
  components: {
    SidebarItem,
  },
  setup(props) {
    const route = useRoute();
    const appStore = useAppStore();
    const settingsStore = useSettingsStore();
    const permissionStore = usePermissionStore();

    const sidebarRouters = computed(() => permissionStore.sidebarRouters);
    const showLogo = computed(() => settingsStore.sidebarLogo);
    const sideTheme = computed(() => settingsStore.sideTheme);
    const theme = computed(() => settingsStore.theme);
    const isCollapse = computed(() => !appStore.sidebar.opened);

    const activeMenu = computed(() => {
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu as string;
      }
      return path;
    });

    const renderSidebarItem = () =>
      sidebarRouters.value.map((route, index: number) => {
        return <SidebarItem key={route.path + index} item={route} basePath={route.path} />;
      });

    const renderMenu = () => (
      <ElMenu
        defaultActive={activeMenu.value}
        collapse={isCollapse.value}
        backgroundColor={sideTheme.value === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground}
        textColor={sideTheme.value === 'theme-dark' ? variables.menuColor : variables.menuLightColor}
        uniqueOpened={true}
        activeTextColor={theme.value}
        collapseTransition={false}
        mode="vertical"
        v-slots={{
          default: renderSidebarItem,
        }}
      />
    );

    const renderScrollbar = () => (
      <ElScrollbar
        class={sideTheme.value}
        wrapClass="scrollbar-wrapper"
        v-slots={{
          default: renderMenu,
        }}
      />
    );

    return {
      showLogo,
      sideTheme,
      isCollapse,
      renderScrollbar,
    };
  },
  render() {
    const title = import.meta.env.VITE_APP_NAME;
    console.log(title);
    return (
      <div
        class={[this.showLogo && 'has-logo']}
        style={{
          backgroundColor: this.sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground,
        }}>
        {this.showLogo && <Logo v-model:collapse={this.isCollapse} />}
        {this.renderScrollbar()}
        <Version v-model:collapse={this.isCollapse} />
      </div>
    );
  },
});
