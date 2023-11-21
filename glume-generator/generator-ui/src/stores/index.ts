import piniaPersist from 'pinia-plugin-persist'
const store = createPinia();
store.use(piniaPersist);

export * from './modules/app';
export * from './modules/dict';
export * from './modules/permission';
export * from './modules/settings';
export * from './modules/tagsView';
export * from './modules/user';

export { store };
export default store
