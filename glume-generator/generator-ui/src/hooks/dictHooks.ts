// @ts-ignore
import useDictStore from '@/stores/modules/dict';
import { DictApiService } from '@/api/dict/DictApiService';

const dictApiService = new DictApiService();

/**
 * 获取字典数据
 */
export function useDict(...args: any[]) {
  const res = ref<Record<string, DictInfo[]>>({});
  return (() => {
    args.forEach((dictType, index) => {
      res.value[dictType] = [];
      const dicts = useDictStore().getDict(dictType);
      if (dicts) {
        res.value[dictType] = dicts;
      } else {
        dictApiService.getDicts(dictType).then((resp: Record<string, any>) => {
          res.value[dictType] = resp.data.map(
            (p: Record<string, any>) =>
              ({ label: p.dictLabel, value: p.dictValue, elTagType: p.listClass, elTagClass: p.cssClass }) as DictInfo,
          );
          useDictStore().setDict(dictType, res.value[dictType]);
        });
      }
    });
    return toRefs(res.value);
  })();
}
