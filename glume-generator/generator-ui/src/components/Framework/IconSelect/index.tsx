import { ElInput } from "element-plus";
import { defineComponent } from "vue";
import icons from './requireIcons'
import { IconSelect } from "./requireIcons";
import SvgIcon from "@/components/Framework/SvgIcon/index.vue";

import './index.scss'

export default defineComponent({
  name: 'IconSelect',
  props: {
    activeIcon: {
      type: String
    }
  },
  setup(props, { expose, emit }) {
    const iconName = ref<string>('');
    const iconList = ref<IconSelect[]>([...icons]);

    function filterIcons() {
      iconList.value = [...icons];
      if (iconName.value) {
        iconList.value = icons.filter((item: IconSelect) => item.stem.indexOf(iconName.value) !== -1);
      }
    }

    function selectedIcon(iconItem: IconSelect) {
      emit('selected', iconItem)
      document.body.click()
    }

    function reset() {
      iconName.value = ''
      iconList.value = icons
    }

    emit('selected');

    expose({ reset });

    const renderSuffixIcon = () => <i class="el-icon-search el-input__icon" />

    const renderInput = () => <ElInput class="icon-search" modelValue={iconName.value} clearable={true} placeholder="请输入图标名称" onClear={filterIcons} onInput={filterIcons} v-slots={{
      suffix: renderSuffixIcon()
    }}  />

    const renderIconSelect = () => <div class="icon-body">
      {renderInput()}
      <div class="icon-list">
        <div class="list-container">
          { iconList.value.map((item: IconSelect, index: number) => <div class="icon-item-wrapper" key={index} onClick={() => selectedIcon(item)}>
            <div class={['icon-item', props.activeIcon === item.stem && 'active']}>
              <SvgIcon iconClass={item.stem} className="icon" style="height: 25px;width: 16px;"/>
              <span>{item.stem}</span>
            </div>
          </div>) }
        </div>
      </div>
    </div>

    return {
      renderIconSelect
    }
  },
  render() {
    return this.renderIconSelect();
  },
})
