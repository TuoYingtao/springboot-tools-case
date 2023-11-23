const context = import.meta.glob('@/assets/icons/svg/*.svg');

export interface IconSelect {
  icon: string;
  stem: string;
}

const importAll = (importContext: Record<string, () => Promise<unknown>>) => Object.keys(importContext);
const re = /\.\/|\/(.*)\.svg/;
const icons = importAll(context).map((i) => {
  const str = i.split('/');
  // @ts-ignore
  const str2 = i.match(re)['1'].split('/');
  const name = str[str.length - 1];
  const value = str2[str2.length - 1];
  return { icon: name, stem: value } as IconSelect;
});
export default icons;
