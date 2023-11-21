import Cookies from 'js-cookie'

// 侧边栏状态
const sidebarStatus = Cookies.get('sidebarStatus');

const useAppStore = defineStore('app', {
  persist: { // 本地持久化
    enabled: true,
    strategies: [
      {
        key: "app-storage",
        storage: sessionStorage,
      },
    ],
  },
  state: () => ({
    sidebar: {
      opened: sidebarStatus ? !!+sidebarStatus : true,
      withoutAnimation: false,
      hide: false
    },
    device: 'desktop',
    size: Cookies.get('size') || 'small'
  } as App),
  actions: {
    /**
     * 切换侧边栏
     * @param withoutAnimation 是否开启动画效果
     */
    toggleSideBar(withoutAnimation: boolean) {
      if (this.sidebar.hide) {
        return false;
      }
      this.sidebar.opened = !this.sidebar.opened
      this.sidebar.withoutAnimation = withoutAnimation
      if (this.sidebar.opened) {
        Cookies.set('sidebarStatus', String(1))
      } else {
        Cookies.set('sidebarStatus', String(0))
      }
    },
    /**
     * 关闭侧边栏
     * @param withoutAnimation 是否开启动画效果
     */
    closeSideBar({withoutAnimation}: {withoutAnimation:boolean}) {
      Cookies.set('sidebarStatus', String(0))
      this.sidebar.opened = false
      this.sidebar.withoutAnimation = withoutAnimation
    },
    /**
     * 切换设备
     * @param device 设备名称
     */
    toggleDevice(device: string) {
      this.device = device
    },
    /**
     * 全局样式大小
     * @param size
     */
    setSize(size: string) {
      this.size = size;
      Cookies.set('size', size)
    },
    /**
     * 切换侧边栏显示隐藏
     * @param status
     */
    toggleSideBarHide(status: boolean) {
      this.sidebar.hide = status
    }
  }
})

export default useAppStore
