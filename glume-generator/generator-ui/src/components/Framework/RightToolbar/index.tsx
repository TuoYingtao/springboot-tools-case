import { defineComponent } from 'vue';
import { ElDialog, ElRow, ElTooltip, ElTransfer, TransferDataItem, TransferKey } from 'element-plus';

import './index.scss';

export default defineComponent({
  name: 'RightToolbar',
  props: {
    showSearch: {
      type: Boolean,
      default: true,
    },
    columns: {
      type: Array,
    },
    search: {
      type: Boolean,
      default: true,
    },
    gutter: {
      type: Number,
      default: 10,
    },
  },
  setup(props, { emit }) {
    // 显隐数据
    const value = ref([]);
    // 弹出层标题
    const title = ref('显示/隐藏');
    // 是否显示弹出层
    const open = ref(false);

    const style = computed(() => {
      const ret: Record<string, any> = {};
      if (props.gutter) {
        ret.marginRight = `${props.gutter / 2}px`;
      }
      return ret;
    });

    // 搜索
    function toggleSearch() {
      emit('update:showSearch', !props.showSearch);
    }

    // 刷新
    function refresh() {
      console.log('刷新');
      emit('queryTable');
    }

    // 右侧列表元素变化
    function dataChange(data: TransferKey[]) {
      for (let item in props.columns) {
        // @ts-ignore
        const key = props.columns[item].key;
        // @ts-ignore
        props.columns[item].visible = !data.includes(key);
      }
    }

    // 打开显隐列dialog
    function showColumn() {
      open.value = true;
    }

    // 显隐列初始默认隐藏列
    for (let item in props.columns) {
      // @ts-ignore
      if (props.columns[item].visible === false) {
        // @ts-ignore
        value.value.push(parseInt(item));
      }
    }

    const renderDialog = () => (
      <ElDialog modelValue={open.value} title={title.value} appendToBody={true}>
        <ElTransfer
          modelValue={value.value}
          titles={['显示', '隐藏']}
          data={props.columns as TransferDataItem[]}
          onChange={() => dataChange}
        />
      </ElDialog>
    );

    const renderRow = () => (
      <ElRow>
        {props.search && (
          <ElTooltip class="item" effect="dark" content={props.showSearch ? '隐藏搜索' : '显示搜索'} placement="top">
            <el-button circle={true} icon="Search" onClick={() => toggleSearch()} />
          </ElTooltip>
        )}
        <ElTooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button circle={true} icon="Refresh" onClick={() => refresh()} />
        </ElTooltip>
        {props.columns && (
          <ElTooltip class="item" effect="dark" content="显隐列" placement="top">
            <el-button circle={true} icon="Menu" onClick={() => showColumn()} />
          </ElTooltip>
        )}
      </ElRow>
    );

    const renderRightToolbar = () => (
      <div class="top-right-btn" style={style.value}>
        {renderRow()}
        {renderDialog()}
      </div>
    );

    return {
      renderRightToolbar,
    };
  },
  render() {
    return this.renderRightToolbar();
  },
});
