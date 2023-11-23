import packages from '../../package.json';

// 项目名称
export const VITE_APP_NAME = import.meta.env.VITE_APP_NAME;
// 项目版本
export const VERSION = packages.version;

// 项目页脚版权信息
export const COPYRIGHT_INFO = `Copyright @ 2023-${new Date().getFullYear()} ${VITE_APP_NAME}. All Rights Reserved.`;
// 项目key
export const PREFIX_FIELD = 'generator-ui';
// Token 令牌key
export const TOKEN_NAME = `${PREFIX_FIELD}-token`;

// 系统默认角色
export const ROLE_DEFAULT = 'ROLE_DEFAULT';

// 需要登录吗 true-需要登录 false-不需要登录
export const IS_TOKEN_AUTH = false;
