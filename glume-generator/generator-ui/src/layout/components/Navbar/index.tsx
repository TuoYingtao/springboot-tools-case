import { defineComponent } from 'vue';
import { useDark, useToggle } from '@vueuse/core';
import { ElDropdown, ElDropdownMenu, ElIcon, ElMessageBox, ElTooltip } from 'element-plus';

import useAppStore from '@/stores/modules/app';
import useUserStore from '@/stores/modules/user';
import useSettingsStore from '@/stores/modules/settings';

import './index.scss';
import Breadcrumb from '@/components/Framework/layout/Breadcrumb/index';
import TopNav from '@/components/Framework/layout/TopNav/index.vue';
import Hamburger from '@/components/Framework/layout/Hamburger/index.vue';
import Screenfull from '@/components/Framework/layout/Screenfull/index.vue';
import SizeSelect from '@/components/Framework/layout/SizeSelect/index.vue';
import HeaderSearch from '@/components/Framework/layout/HeaderSearch/index';
import SvgIcon from '@/components/Framework/SvgIcon/index.vue';

export default defineComponent({
  name: 'Navbar',
  setup(props, { emit }) {
    const appStore = useAppStore();
    const userStore = useUserStore();
    const settingsStore = useSettingsStore();
    const isDark = useDark();
    const toggleDark = useToggle(isDark);

    function toggleSideBar() {
      appStore.toggleSideBar(false);
    }

    function handleCommand(command: string) {
      switch (command) {
        case 'setLayout':
          setLayout();
          break;
        case 'logout':
          logout();
          break;
        default:
          break;
      }
    }

    function logout() {
      ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
        .then(() => {
          userStore.logOut().then(() => {
            location.href = '/index';
          });
        })
        .catch(() => {});
    }

    function setLayout() {
      return emit('setLayout');
    }

    emit('setLayout');

    const renderDark = () => (
      <div onClick={() => toggleDark}>
        <el-switch
          size="small"
          style="--el-switch-off-color: #CCCCCC; --el-switch-on-color: var(--theme-color);"
          active-action-icon="Moon"
          inactive-action-icon="Sunny"
          v-model={isDark.value}
        />
      </div>
    );

    const renderGit = () => {
      const url = 'https://github.com/TuoYingtao';
      return (
        <div class="right-menu-item hover-effect">
          <SvgIcon iconClass="github" onClick={() => window.open(url)} />
        </div>
      );
    };

    const renderIcon = () => (
      <ElIcon>
        <caret-bottom />
      </ElIcon>
    );

    const renderTooltip = (content: string, slots: JSX.Element) => (
      <ElTooltip
        content={content}
        effect="dark"
        placement="bottom"
        v-slots={{
          default: slots,
        }}
      />
    );

    const renderDropdownMenu = (slots: JSX.Element) => (
      <ElDropdownMenu
        v-slots={{
          default: slots,
        }}
      />
    );

    const renderDropdown = () => (
      <ElDropdown
        class="right-menu-item hover-effect"
        trigger="click"
        onCommand={handleCommand}
        v-slots={{
          default: (
            <>
              <div class="avatar-wrapper">
                <img class="user-avatar" src={userStore.avatar} />
                {renderIcon()}
              </div>
            </>
          ),
          dropdown: renderDropdownMenu(
            <>
              <router-link to="/user/profile">
                <el-dropdown-item>个人中心</el-dropdown-item>
              </router-link>
              {settingsStore.showSettings && (
                <>
                  <el-dropdown-item command="setLayout">
                    <span>布局设置</span>
                  </el-dropdown-item>
                </>
              )}
              <el-dropdown-item divided command="logout">
                <span>退出登录</span>
              </el-dropdown-item>
            </>,
          ),
        }}
      />
    );

    return {
      toggleDark,
      isDark,
      settingsStore,
      appStore,
      renderGit,
      renderDark,
      toggleSideBar,
      renderTooltip,
      renderDropdown,
    };
  },
  render() {
    return (
      <div class="navbar">
        <Hamburger
          id="hamburger-container"
          class="hamburger-container"
          isActive={this.appStore.sidebar.opened}
          onToggleClick={this.toggleSideBar}
        />
        {
          // @ts-ignore
          !this.settingsStore.topNav && <Breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
        }
        {this.settingsStore.topNav && <TopNav id="topmenu-container" class="topmenu-container" />}
        <div class="right-menu">
          {this.appStore.device !== 'mobile' && (
            <>
              {
                // @ts-ignore
                <HeaderSearch id="header-search" class="right-menu-item" />
              }
              {false && this.renderTooltip('黑暗主题', this.renderDark())}
              {this.renderTooltip('源码地址', this.renderGit())}
              <Screenfull id="screenfull" class="right-menu-item hover-effect" />
              {this.renderTooltip('布局大小', <SizeSelect id="size-select" class="right-menu-item hover-effect" />)}
            </>
          )}
          <div class="avatar-container">{this.renderDropdown()}</div>
        </div>
      </div>
    );
  },
});
