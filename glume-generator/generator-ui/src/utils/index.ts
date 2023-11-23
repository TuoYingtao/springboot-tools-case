/**
 * 日期格式化
 * @param time
 * @param pattern
 * @returns {string|null}
 */
export function parseTime(time: string | number, pattern?: string) {
  if (arguments.length === 0 || !time) {
    return null;
  }
  const format = pattern || '{y}-{m}-{d} {h}:{i}:{s}';
  let date;
  if (typeof time === 'object') {
    date = time;
  } else {
    if (typeof time === 'string' && /^[0-9]+$/.test(time)) {
      time = parseInt(time);
    } else if (typeof time === 'string') {
      time = time
        .replace(new RegExp(/-/gm), '/')
        .replace('T', ' ')
        .replace(new RegExp(/\.[\d]{3}/gm), '');
    }
    if (typeof time === 'number' && time.toString().length === 10) {
      time = time * 1000;
    }
    date = new Date(time);
  }
  const formatObj: Record<string, any> = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay(),
  };
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key: string) => {
    let value = formatObj[key];
    // Note: getDay() returns 0 on Sunday
    if (key === 'a') {
      return ['日', '一', '二', '三', '四', '五', '六'][value];
    }
    if (result.length > 0 && value < 10) {
      value = '0' + value;
    }
    return value || 0;
  });
  return time_str;
}

/**
 * 表格时间格式化
 */
export function formatDate(cellValue: string | number) {
  if (cellValue == null || cellValue == '') return '';
  var date = new Date(cellValue);
  var year = date.getFullYear();
  var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1;
  var day = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
  var hours = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
  var minutes = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
  var seconds = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
  return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
}

/**
 * 格式化时间
 * @param {number} time
 * @param {string} option
 * @returns {string}
 */
export function formatTime(time: number, option: string) {
  if (('' + time).length === 10) {
    time = parseInt(time + '') * 1000;
  } else {
    time = +time;
  }
  const d = new Date(time);
  const now = Date.now();
  const diff = (now - d.getTime()) / 1000;
  if (diff < 30) {
    return '刚刚';
  } else if (diff < 3600) {
    // less 1 hour
    return Math.ceil(diff / 60) + '分钟前';
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前';
  } else if (diff < 3600 * 24 * 2) {
    return '1天前';
  }
  if (option) {
    return parseTime(time, option);
  } else {
    return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分';
  }
}

/**
 * 首字母大小
 */
export function titleCase(str: string) {
  return str.replace(/( |^)[a-z]/g, (L) => L.toUpperCase());
}

/**
 * 下划转驼峰
 */
export function camelCase(str: string) {
  return str.replace(/_[a-z]/g, (str1) => str1.substr(-1).toUpperCase());
}

/**
 * 判断是否是字符串数字：'1'
 */
export function isNumberStr(str: string) {
  return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str);
}

/**
 * 获取随机数
 *
 * @param {number} [num=100]
 * @returns number
 */
export function getRandomArray(num = 100): number {
  let resultNum = Number((Math.random() * num).toFixed(0));
  if (resultNum <= 1) {
    resultNum = 1;
  }
  return resultNum;
}

/**
 * 表单重置
 */
export function resetForm(refName: string) {
  // @ts-ignore
  if (this.$refs[refName]) {
    // @ts-ignore
    this.$refs[refName].resetFields();
  }
}

/**
 * 添加日期范围
 */
export function addDateRange(params: Record<string, any>, dateRange: string[], propName: string) {
  let search = params;
  search = typeof search === 'object' && search !== null && !Array.isArray(search) ? search : {};
  dateRange = Array.isArray(dateRange) ? dateRange : [];
  if (typeof propName === 'undefined') {
    search['beginTime'] = dateRange[0];
    search['endTime'] = dateRange[1];
  } else {
    search['begin' + propName] = dateRange[0];
    search['end' + propName] = dateRange[1];
  }
  return search;
}

/**
 * 回显数据字典
 */
export function selectDictLabel(datas: Record<string, any>, value: any) {
  if (value === undefined) {
    return '';
  }
  var actions = [];
  Object.keys(datas).some((key) => {
    if (datas[key].value == '' + value) {
      actions.push(datas[key].label);
      return true;
    }
  });
  if (actions.length === 0) {
    actions.push(value);
  }
  return actions.join('');
}

/**
 * 回显数据字典（字符串数组）
 */
export function selectDictLabels(datas: Record<string, any>, value: any, separator: string) {
  if (value === undefined || value.length === 0) {
    return '';
  }
  if (Array.isArray(value)) {
    value = value.join(',');
  }
  var actions: any[] = [];
  var currentSeparator = undefined === separator ? ',' : separator;
  var temp = value.split(currentSeparator);
  Object.keys(value.split(currentSeparator)).some((val) => {
    var match = false;
    Object.keys(datas).some((key) => {
      if (datas[key].value == '' + temp[val]) {
        actions.push(datas[key].label + currentSeparator);
        match = true;
      }
    });
    if (!match) {
      actions.push(temp[val] + currentSeparator);
    }
  });
  return actions.join('').substring(0, actions.join('').length - 1);
}

/**
 * 字符串格式化(%s )
 */
export function sprintf(str: string) {
  var args = arguments,
    flag = true,
    i = 1;
  str = str.replace(/%s/g, function () {
    var arg = args[i++];
    if (typeof arg === 'undefined') {
      flag = false;
      return '';
    }
    return arg;
  });
  return flag ? str : '';
}

/**
 * 转换字符串，undefined,null等转化为""
 */
export function parseStrEmpty(str: string) {
  if (!str || str == 'undefined' || str == 'null') {
    return '';
  }
  return str;
}

/**
 * 构造树型结构数据
 * @param {*} data 数据源
 * @param {*} id id字段 默认 'id'
 * @param {*} parentId 父节点字段 默认 'parentId'
 * @param {*} children 孩子节点字段 默认 'children'
 */
export function handleTree(data: [], id: string, parentId: string, children: string) {
  let config = {
    id: id || 'id',
    parentId: parentId || 'parentId',
    childrenList: children || 'children',
  };

  var childrenListMap: Record<string, Record<string, any>[]> = {};
  var nodeIds = {};
  var tree: Record<string, any>[] = [];

  for (let d of data) {
    let parentId = d[config.parentId];
    if (childrenListMap[parentId] == null) {
      childrenListMap[parentId] = [];
    }
    nodeIds[d[config.id]] = d;
    childrenListMap[parentId].push(d);
  }

  for (let d of data) {
    let parentId = d[config.parentId];
    if (nodeIds[parentId] == null) {
      tree.push(d);
    }
  }

  for (let t of tree) {
    adaptToChildrenList(t);
  }

  function adaptToChildrenList(o: Record<string, any>) {
    if (childrenListMap[o[config.id]] !== null) {
      o[config.childrenList] = childrenListMap[o[config.id]];
    }
    if (o[config.childrenList]) {
      for (let c of o[config.childrenList]) {
        adaptToChildrenList(c);
      }
    }
  }
  return tree;
}

/**
 * 参数处理
 * @param {*} params  参数
 */
export function tansParams(params: Record<string, any>) {
  let result = '';
  for (const propName of Object.keys(params)) {
    const value = params[propName];
    var part = encodeURIComponent(propName) + '=';
    if (value !== null && value !== '' && typeof value !== 'undefined') {
      if (typeof value === 'object') {
        for (const key of Object.keys(value)) {
          if (value[key] !== null && value[key] !== '' && typeof value[key] !== 'undefined') {
            let params = propName + '[' + key + ']';
            var subPart = encodeURIComponent(params) + '=';
            result += subPart + encodeURIComponent(value[key]) + '&';
          }
        }
      } else {
        result += part + encodeURIComponent(value) + '&';
      }
    }
  }
  return result;
}

/**
 * 返回项目路径
 */
export function getNormalPath(p: string) {
  if (p.length === 0 || !p || p == 'undefined') {
    return p;
  }
  let res = p.replace('//', '/');
  if (res[res.length - 1] === '/') {
    return res.slice(0, res.length - 1);
  }
  return res;
}

/**
 * 验证是否为blob格式
 */
export function blobValidate(data: Blob) {
  return data.type !== 'application/json';
}

/**
 * 网页离开返回页面；网页标签提示
 */
export function HTMLTitle() {
  const defaultSettings = import.meta.env.VITE_APP_NAME;
  document.addEventListener('visibilitychange', function () {
    const titleName = defaultSettings; // 页面标签标题
    let isHidden = document.hidden;
    let title = '(*´∇｀*) 欢迎回来！';
    let title2 = '(oﾟvﾟ)ノ Hi';
    if (isHidden) {
      setTimeout(() => {
        document.title = title2;
      }, 1000);
    } else {
      document.title = title;
      setTimeout(() => {
        document.title = titleName;
      }, 1000);
    }
  });
}
