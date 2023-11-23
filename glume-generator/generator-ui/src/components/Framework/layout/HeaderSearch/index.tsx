import { defineComponent } from 'vue';
import { ElOption, ElSelect } from 'element-plus';
import Fuse from 'fuse.js';
import { getNormalPath } from '@/utils';
import { isHttp } from '@/utils/validate';
import usePermissionStore from '@/stores/modules/permission';

import classStyle from './index.module.scss';

interface HeaderSearch {
  title: string[];
  path: string;
}

export default defineComponent({
  name: 'HeaderSearch',
  setup(props) {
    const headerSearchSelectRef = ref(null);
    const search = ref('');
    const options = ref<Fuse.FuseResult<HeaderSearch>[]>([]);
    const searchPool = ref<HeaderSearch[]>([]);
    const show = ref(false);
    const fuse = ref<Fuse<HeaderSearch> | undefined>(undefined);
    const router = useRouter();
    const routes = computed(() => usePermissionStore().routes);

    onMounted(() => {
      searchPool.value = generateRoutes(routes.value);
    });

    watchEffect(() => {
      searchPool.value = generateRoutes(routes.value);
    });

    watch(show, (value) => {
      if (value) {
        document.body.addEventListener('click', close);
      } else {
        document.body.removeEventListener('click', close);
      }
    });

    watch(searchPool, (list) => {
      initFuse(list);
    });

    function click() {
      show.value = !show.value;
      if (show.value) {
        // @ts-ignore
        headerSearchSelectRef.value && headerSearchSelectRef.value.focus();
      }
    }
    function close() {
      // @ts-ignore
      headerSearchSelectRef.value && headerSearchSelectRef.value.blur();
      options.value = [];
      show.value = false;
    }
    function change(val?: Record<string, any>) {
      const path = val != undefined ? val.path : '';
      if (isHttp(path)) {
        // http(s):// 路径新窗口打开
        const pindex = path.indexOf('http');
        window.open(path.substr(pindex, path.length), '_blank');
      } else {
        router.push(path);
      }
      search.value = '';
      options.value = [];
      nextTick(() => {
        show.value = false;
      });
    }
    function initFuse(list: HeaderSearch[]) {
      fuse.value = new Fuse(list, {
        shouldSort: true,
        threshold: 0.4,
        location: 0,
        distance: 100,
        minMatchCharLength: 1,
        keys: [
          {
            name: 'title',
            weight: 0.7,
          },
          {
            name: 'path',
            weight: 0.3,
          },
        ],
      });
    }
    // 过滤掉侧边栏可以显示的路由
    // 并生成国际化标题
    function generateRoutes(routes: LayoutRoutes[], basePath = '', prefixTitle: string[] = []) {
      let res: HeaderSearch[] = [];
      for (const r of routes) {
        // 跳过隐藏路由器
        if (r.hidden) {
          continue;
        }
        const p = r.path.length > 0 && r.path[0] === '/' ? r.path : '/' + r.path;
        const data: HeaderSearch = {
          path: !isHttp(r.path) ? getNormalPath(basePath + p) : r.path,
          title: [...prefixTitle],
        };
        if (r.meta && r.meta.title) {
          data.title = [...data.title, r.meta.title];

          if (r.redirect !== 'noRedirect') {
            // 只推送有标题的路由
            // 特殊情况：需要排除父路由器而不重定向
            res.push(data);
          }
        }
        // 递归子路由
        if (r.children) {
          const tempRoutes = generateRoutes(r.children, data.path, data.title);
          if (tempRoutes.length >= 1) {
            res = [...res, ...tempRoutes];
          }
        }
      }
      return res;
    }
    function querySearch(query?: string) {
      if (query !== '' && query != undefined && fuse.value != undefined) {
        options.value = fuse.value.search(query);
      } else {
        options.value = [];
      }
    }

    const renderOption = () =>
      options.value.map((option) => (
        <ElOption key={option.item.path} value={option.item} label={option.item.title.join(' > ')} />
      ));

    const renderSelect = () => (
      <ElSelect
        class={['renderElSelect', classStyle.headerSearchSelect]}
        ref="headerSearchSelectRef"
        modelValue={search.value}
        remoteMethod={() => querySearch()}
        filterable={true}
        defaultFirstOption={true}
        remote={true}
        placeholder={'Search'}
        onChange={() => change()}
        v-slots={{
          default: renderOption(),
        }}
      />
    );

    const renderHeaderSearch = () => (
      <div class={[classStyle.headerSearch, show.value && classStyle.show]}>
        <svg-icon
          class-name={classStyle.searchIcon}
          icon-class="search"
          onClick={(e: MouseEvent) => {
            e.stopPropagation();
            click();
          }}
        />
        {renderSelect()}
      </div>
    );

    return {
      renderHeaderSearch,
    };
  },
  render() {
    return this.renderHeaderSearch();
  },
});
