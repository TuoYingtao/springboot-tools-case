import { IAxiosRequestConfig } from "@/type/axios";
import { config } from "@/utils/request/index";


export function Log(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
  const className = target.constructor.name;
  const originalMethod = descriptor.value;
  descriptor.value = function(...args: any[]) {
    const options = className === 'VAxios'
        ? Reflect.get(this, 'options') as IAxiosRequestConfig : config;
    if (options.requestOptions.isDebugger) {
      const filePath = window.location.pathname;
      console.debug(`[${filePath}] Entering [${className}.${propertyKey}] with [arguments]`, ...args);
      const result = originalMethod.apply(this, args);
      console.debug(`[${filePath}] Exiting  [${className}.${propertyKey}] with [result]`, result);
      return result;
    } else {
      return originalMethod.apply(this, args);
    }
  }
}
