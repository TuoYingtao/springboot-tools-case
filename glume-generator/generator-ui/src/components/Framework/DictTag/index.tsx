import { defineComponent } from 'vue';
import { ElTag } from 'element-plus';
import './index.scss';

export default defineComponent({
  name: 'DictTag',
  props: {
    options: {
      // 数据
      type: Array,
      default: null,
    },
    value: [Number, String, Array], // 当前的值
    showValue: {
      // 当未找到匹配的数据时，显示value
      type: Boolean,
      default: true,
    },
  },
  setup(props) {
    // 记录未匹配的项
    const unMatchArray = ref<Array<number | string | DictInfo>>([]);

    const values = computed(() => {
      if (props.value !== null && typeof props.value !== 'undefined') {
        return Array.isArray(props.value) ? props.value : [String(props.value)];
      } else {
        return [];
      }
    });

    const unMatch = computed(() => {
      unMatchArray.value = [];
      if (props.value !== null && typeof props.value !== 'undefined') {
        // 传入值为非数组
        if (!Array.isArray(props.value)) {
          if ((props.options as DictInfo[]).some((v) => v.value == props.value)) return false;
          unMatchArray.value.push(props.value);
          return true;
        }
        // 传入值为Array
        props.value.forEach((item) => {
          if (!(props.options as DictInfo[]).some((v) => v.value == item)) unMatchArray.value.push(item);
        });
        return true;
      }
      // 没有value不显示
      return false;
    });

    function handleArray(array?: string[]) {
      if (!array || array.length === 0) return '';
      return array.reduce((pre: string, cur: string) => {
        return pre + ' ' + cur;
      });
    }

    const renderTag = (item: DictInfo, index: number) => (
      <ElTag disableTransitions={true} key={item.value} class={item.elTagClass}>
        {item.label + ' '}
      </ElTag>
    );

    const renderDom = () => (
      <>
        {(props.options as DictInfo[]).map((item: DictInfo, index) => {
          if (values.value.includes(item.value)) {
            if (
              (item.elTagType == 'default' || item.elTagType == '') &&
              (item.elTagClass == '' || item.elTagClass == null)
            ) {
              return (
                <span key={item.value} class={item.elTagClass}>
                  {item.label + ' '}
                </span>
              );
            } else {
              return renderTag(item, index);
            }
          }
        })}
        {
          // @ts-ignore
          unMatch.value && props.showValue && <>{unMatchArray.value | handleArray()}</>
        }
      </>
    );

    return {
      renderDom,
    };
  },
  render() {
    return this.renderDom();
  },
});
