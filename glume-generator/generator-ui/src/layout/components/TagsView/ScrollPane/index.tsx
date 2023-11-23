import { defineComponent } from 'vue';
import { RouteLocationNormalizedLoaded } from 'vue-router';
import { ElScrollbar } from 'element-plus';

import './index.scss';
import useTagsViewStore from '@/stores/modules/tagsView';

export default defineComponent({
  name: 'ScrollPane',
  props: {},
  mounted() {
    this.scrollWrapper.addEventListener('scroll', this.emitScroll, true);
  },
  beforeUnmount() {
    this.scrollWrapper.removeEventListener('scroll', this.emitScroll);
  },
  setup(props, { expose, emit }) {
    // @ts-ignore
    const { proxy, slots } = getCurrentInstance();
    const tagsViewStore = useTagsViewStore();
    const emitScroll = () => emit('scroll');
    const tagAndTagSpacing = ref(4);

    const scrollWrapper = computed(() => proxy.$refs.scrollContainer.$refs.wrapRef);
    const visitedViews = computed(() => tagsViewStore.visitedViews);

    function handleScroll(e: WheelEvent) {
      // @ts-ignore
      const eventDelta = e.wheelDelta || -e.deltaY * 40;
      const $scrollWrapper = scrollWrapper.value;
      $scrollWrapper.scrollLeft = $scrollWrapper.scrollLeft + eventDelta / 4;
    }

    function moveToTarget(currentTag: RouteLocationNormalizedLoaded) {
      const $container = proxy.$refs.scrollContainer.$el;
      const $containerWidth = $container.offsetWidth;
      const $scrollWrapper = scrollWrapper.value;

      let firstTag = null;
      let lastTag = null;

      // find first tag and last tag
      if (visitedViews.value.length > 0) {
        firstTag = visitedViews.value[0];
        lastTag = visitedViews.value[visitedViews.value.length - 1];
      }

      if (firstTag === currentTag) {
        $scrollWrapper.scrollLeft = 0;
      } else if (lastTag === currentTag) {
        $scrollWrapper.scrollLeft = $scrollWrapper.scrollWidth - $containerWidth;
      } else {
        const tagListDom = document.getElementsByClassName('tags-view-item') as HTMLCollectionOf<HTMLElement>;
        const currentIndex = visitedViews.value.findIndex((item) => item === currentTag);
        let prevTag = null;
        let nextTag = null;
        for (const k in tagListDom) {
          if (k !== 'length' && Object.hasOwnProperty.call(tagListDom, k)) {
            if (tagListDom[k].dataset.path === visitedViews.value[currentIndex - 1].path) {
              prevTag = tagListDom[k];
            }
            if (tagListDom[k].dataset.path === visitedViews.value[currentIndex + 1].path) {
              nextTag = tagListDom[k];
            }
          }
        }

        // the tag's offsetLeft after of nextTag
        const afterNextTagOffsetLeft = nextTag!.offsetLeft + nextTag!.offsetWidth + tagAndTagSpacing.value;

        // the tag's offsetLeft before of prevTag
        const beforePrevTagOffsetLeft = prevTag!.offsetLeft - tagAndTagSpacing.value;
        if (afterNextTagOffsetLeft > $scrollWrapper.scrollLeft + $containerWidth) {
          $scrollWrapper.scrollLeft = afterNextTagOffsetLeft - $containerWidth;
        } else if (beforePrevTagOffsetLeft < $scrollWrapper.scrollLeft) {
          $scrollWrapper.scrollLeft = beforePrevTagOffsetLeft;
        }
      }
    }

    expose({
      moveToTarget,
    });

    // @ts-ignore
    const renderScrollbar = () => (
      <ElScrollbar
        class="scroll-container"
        ref="scrollContainer"
        vertical={false}
        on={{ wheel: (event: WheelEvent) => event.preventDefault() }}
        onWheel={handleScroll}>
        {slots.default()}
      </ElScrollbar>
    );

    return {
      scrollWrapper,
      emitScroll,
      renderScrollbar,
    };
  },
  render() {
    return this.renderScrollbar();
  },
});
