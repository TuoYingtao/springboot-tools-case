import merge from "lodash/merge";
import router from '@/router'
import { RouteLocationNormalizedLoaded, RouteLocationRaw } from "vue-router";
import useTagsViewStore from '@/stores/modules/tagsView'

export default {
  // 刷新当前tab页签
  refreshPage(obj: RouteLocationNormalizedLoaded) {
    console.log(obj)
    const { path, query, matched } = router.currentRoute.value;
    if (obj === undefined) {
      matched.forEach((m) => {
        if (m.components && m.components.default && m.components.default.name) {
          if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
            obj = merge(obj, { name: m.components.default.name, path: path, query: query });
          }
        }
      });
    }
    return useTagsViewStore().delCachedView(obj).then(() => {
      const { path, query } = obj
      router.replace({
        path: '/redirect' + path,
        query: query
      })
    })
  },
  // 关闭当前tab页签，打开新页签
  closeOpenPage(obj: RouteLocationRaw) {
    useTagsViewStore().delView(router.currentRoute.value);
    if (obj !== undefined) {
      return router.push(obj);
    }
  },
  // 关闭指定tab页签
  closePage(obj: RouteLocationNormalizedLoaded) {
    if (obj === undefined) {
      return useTagsViewStore().delView(router.currentRoute.value).then(({visitedViews}) => {
        const latestView = visitedViews.slice(-1)[0]
        if (latestView) {
            return router.push(latestView.fullPath)
        }
        return router.push('/');
      });
    }
    return useTagsViewStore().delView(obj);
  },
  // 关闭所有tab页签
  closeAllPage() {
    return useTagsViewStore().delAllViews({} as RouteLocationNormalizedLoaded);
  },
  // 关闭左侧tab页签
  closeLeftPage(obj: RouteLocationNormalizedLoaded) {
    return useTagsViewStore().delLeftTags(obj || router.currentRoute.value);
  },
  // 关闭右侧tab页签
  closeRightPage(obj: RouteLocationNormalizedLoaded) {
    return useTagsViewStore().delRightTags(obj || router.currentRoute.value);
  },
  // 关闭其他tab页签
  closeOtherPage(obj: RouteLocationNormalizedLoaded) {
    return useTagsViewStore().delOthersViews(obj || router.currentRoute.value);
  },
  // 打开tab页签
  openPage(url: RouteLocationRaw) {
    return router.push(url);
  },
  // 修改tab页签
  updatePage(obj: RouteLocationNormalizedLoaded) {
    return useTagsViewStore().updateVisitedView(obj);
  }
}
