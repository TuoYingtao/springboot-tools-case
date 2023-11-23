import { defineComponent, Ref } from 'vue';
import { ElMenuItem, ElSubMenu } from 'element-plus';

import AppLink from './Link.vue';
import SvgIcon from '@/components/Framework/SvgIcon/index.vue';
import SidebarItem from './SidebarItem';

import { isExternal } from '@/utils/validate';
import { getNormalPath } from '@/utils';

export default defineComponent({
  name: 'SidebarItem',
  components: {
    AppLink,
    SvgIcon,
  },
  props: {
    // route object
    item: {
      type: Object,
      required: true,
    },
    isNest: {
      type: Boolean,
      default: false,
    },
    basePath: {
      type: String,
      default: '',
    },
  },
  setup(props) {
    const onlyOneChild: Ref<LayoutRoutes> = ref({} as LayoutRoutes);

    function hasOneShowingChild(children: LayoutRoutes[] = [], parent: LayoutRoutes) {
      if (!children) {
        children = [];
      }
      const showingChildren = children.filter((item) => {
        if (props.item.hidden) {
          return false;
        } else {
          // 临时集（如果只有一个显示子项，则将使用）
          onlyOneChild.value = item;
          return true;
        }
      });
      // 当只有一个子路由器时，默认显示子路由器
      if (showingChildren.length === 1) {
        return true;
      }
      // 如果没有可显示的子路由器，则显示父路由器
      if (showingChildren.length === 0) {
        onlyOneChild.value = { ...parent, path: '', noShowingChildren: true };
        return true;
      }
      return false;
    }

    function resolvePath(routePath: string, routeQuery?: string) {
      if (isExternal(routePath)) {
        return routePath;
      }
      if (isExternal(props.basePath)) {
        return props.basePath;
      }
      if (routeQuery) {
        let query = JSON.parse(routeQuery);
        return { path: getNormalPath(props.basePath + '/' + routePath), query: query };
      }
      return getNormalPath(props.basePath + '/' + routePath);
    }

    function hasTitle(title: string) {
      if (title.length > 5) {
        return title;
      } else {
        return '';
      }
    }

    const renderSvgIcon = (iconClass: string) => <SvgIcon iconClass={iconClass} />;

    const renderMenuTitle = (title: string, txt: string) => (
      <span class="menu-title" title={title}>
        {txt}
      </span>
    );

    const renderMenuItem = () => {
      const title = onlyOneChild.value.meta?.title || '';
      return (
        <ElMenuItem
          class={[!props.isNest && 'submenu-title-noDropdown']}
          index={resolvePath(onlyOneChild.value.path)}
          v-slots={{
            default: renderSvgIcon(onlyOneChild.value.meta?.icon || (props.item.meta && props.item.meta.icon)),
            title: renderMenuTitle(hasTitle(title), title),
          }}
        />
      );
    };

    const renderSubMenu = () => (
      <ElSubMenu
        ref="subMenu"
        index={resolvePath(props.item.path)}
        popperAppendToBody
        v-slots={{
          default: () =>
            props.item.children.map((child: LayoutRoutes, index: number) => (
              <SidebarItem
                class="nest-menu"
                key={child.path + index}
                item={child}
                basePath={resolvePath(child.path)}
                isNest={true}
              />
            )),
          title: () =>
            props.item.meta && (
              <>
                {renderSvgIcon(props.item.meta && props.item.meta.icon)}
                {renderMenuTitle(hasTitle(props.item.meta.title), props.item.meta.title)}
              </>
            ),
        }}
      />
    );

    const renderChild = () => {
      if (
        hasOneShowingChild(props.item.children, props.item as LayoutRoutes) &&
        (!onlyOneChild.value.children || onlyOneChild.value.noShowingChildren) &&
        !props.item.alwaysShow
      ) {
        if (onlyOneChild.value.meta) {
          return (
            <AppLink
              to={resolvePath(onlyOneChild.value.path, onlyOneChild.value.query || '')}
              v-slots={{
                default: renderMenuItem,
              }}
            />
          );
        }
      }
      return renderSubMenu();
    };

    return {
      item: props.item,
      renderChild,
    };
  },
  render() {
    return !this.item.hidden && <div>{this.renderChild()}</div>;
  },
});
