import { defineComponent, TransitionGroup } from 'vue';
import { ElBreadcrumb, ElBreadcrumbItem } from 'element-plus';
import { RouteLocationMatched, RouteLocationRaw } from 'vue-router';
import style from './index.module.scss';

export default defineComponent({
  name: 'Breadcrumb',
  setup(props) {
    const route = useRoute();
    const router = useRouter();
    const levelList = ref<RouteLocationMatched[]>([]);

    function getBreadcrumb() {
      // only show routes with meta.title
      let matched = route.matched.filter((item) => item.meta && item.meta.title);
      const first = matched[0];
      // 判断是否为首页
      if (!isDashboard(first as RouteLocationMatched)) {
        // @ts-ignore
        matched = [{ path: '/index', meta: { title: '首页' } }].concat(matched);
      }

      levelList.value = matched.filter((item) => item.meta && item.meta.title && item.meta.breadcrumb !== false);
    }
    function isDashboard(route: RouteLocationMatched) {
      const name = route && route.name;
      if (!name) {
        return false;
      }
      // @ts-ignore
      return name?.trim() === 'Index';
    }
    function handleLink(item: RouteLocationMatched) {
      const { redirect, path } = item;
      if (redirect) {
        router.push(redirect as RouteLocationRaw);
        return;
      }
      router.push(path);
    }

    watchEffect(() => {
      // if you go to the redirect page, do not update the breadcrumbs
      if (route.path.startsWith('/redirect/')) {
        return;
      }
      getBreadcrumb();
    });
    getBreadcrumb();

    const renderContext = (item: RouteLocationMatched, index: number) => (
      <>
        {item.redirect === 'noRedirect' || index == levelList.value.length - 1 ? (
          <span class={style.noRedirect}>{item.meta.title}</span>
        ) : (
          <a
            onClick={(e) => {
              e.preventDefault();
              handleLink(item);
            }}>
            {item.meta.title}
          </a>
        )}
      </>
    );

    const renderBreadcrumbItem = () =>
      levelList.value.map((item, index) => (
        <ElBreadcrumbItem
          key={item.path}
          v-slots={{
            default: renderContext(item, index),
          }}
        />
      ));

    const renderTransitionGroup = () => (
      <TransitionGroup
        name="breadcrumb"
        v-slots={{
          default: renderBreadcrumbItem,
        }}
      />
    );

    const renderBreadcrumb = () => (
      <ElBreadcrumb
        class={style.appBreadcrumb}
        separator="/"
        v-slots={{
          default: renderTransitionGroup,
        }}
      />
    );

    return {
      renderBreadcrumb,
    };
  },
  render() {
    return this.renderBreadcrumb();
  },
});
