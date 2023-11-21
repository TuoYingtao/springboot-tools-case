/**
 * 定时任务工具
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-26 15:00:05
 * @Version: v1.0.0
*/

class TaskChannel {
  private static instance: TaskChannel;

  private interval: Map<string, any> = new Map<string, any>();

  public static getTaskChannelInstance(): TaskChannel {
    if (!TaskChannel.instance) {
      TaskChannel.instance = new TaskChannel();
    }
    return TaskChannel.instance;
  }

  public getInterval(): Map<string, any> {
    return this.interval;
  }

  public setTaskChannel(key: string, fun: any): void {
    this.interval.set(key, fun);
  }

  public getIntervalValue(key: string): string | null {
    if (key && this.interval.has(key)) {
      return this.interval.get(key);
    }
    return null;
  }

  public batchGetIntervalValue(keys: string[]): string[] {
    const values: string[] = [];
    keys.forEach((item) => {
      if (this.interval.has(item)) {
        values.push(this.interval.get(item));
      }
    });
    return values;
  }

  public deleteItem(key: string): boolean {
    if (key && this.interval.has(key)) {
      this.interval.delete(key);
      return true;
    }
    return false;
  }

  public batchDeleteItem(keys: string[]) {
    keys.forEach((item) => {
      if (this.interval.has(item)) {
        this.interval.delete(item);
      }
    });
  }

  public clearAll(): boolean {
    const num = this.interval.size;
    if (num === 0) return true;
    this.interval.clear();
    return true;
  }
}

export class TaskFun {
  private readonly cache: any;

  constructor(cache: any) {
    this.cache = cache;
  }

  private static instance: TaskFun;

  public static getTaskFunInstance(cache: any): TaskFun {
    if (!TaskFun.instance) {
      TaskFun.instance = new TaskFun(cache);
    }
    return TaskFun.instance;
  }

  public async homeData(funcApi: Function, param?: any) {
    try {
      const { result } = await funcApi();
      this.cache.botNum = result;
    } catch (e: any) {
      throw new Error(e.message);
    }
  }

  public homeData2() {
    console.log('测试2');
  }
}

export class TaskCallTemplate {
  private static instance: TaskCallTemplate;

  private taskChannel: TaskChannel;

  private readonly taskFun: TaskFun;

  private mapKey: string;

  constructor(cache: any) {
    this.taskChannel = TaskChannel.getTaskChannelInstance();
    this.taskFun = TaskFun.getTaskFunInstance(cache);
    this.mapKey = '';
    window.addEventListener('beforeunload', () => {
      const interval = this.taskChannel.getInterval();
      const num = interval.size;
      if (num > 0) {
        const arrKey: string[] = [];
        for (const entry of interval.entries()) {
          arrKey.push(entry[0]);
        }
        this.clearTask(...arrKey);
      }
    });
  }

  public static getTaskCallTemplateInstance(cache: any) {
    if (!this.instance) {
      this.instance = new TaskCallTemplate(cache);
    }
    return this.instance;
  }

  public clearTask(...args: string[]) {
    try {
      if (args.length === 0) throw new Error('参数不能为空');
      if (args.length === 1) {
        const INTERVAL = this.taskChannel.getIntervalValue(args[0]);
        if (INTERVAL !== null) {
          clearInterval(INTERVAL);
          this.taskChannel.deleteItem(args[0]);
        }
      } else if (args.length > 1) {
        const arr: string[] = args.filter((item) => this.taskChannel.getIntervalValue(item) !== null);
        const values: string[] = this.taskChannel.batchGetIntervalValue(arr);
        values.forEach((value) => clearInterval(value));
        this.taskChannel.batchDeleteItem(arr);
      }
    } catch (e: any) {
      console.error(e.message)
    }
  }

  private intervalDotNum<T extends keyof TaskFun>(fun: any, method: T, time: number, ...args: Parameters<TaskFun[T]>) {
    const INTERVAL = setInterval(async () => {
      try {
        await (fun[method] as any).apply(fun, args);
      } catch (e: any) {
        throw new Error(e.message);
      }
    }, time);
    this.taskChannel.setTaskChannel(this.mapKey, INTERVAL);
  }

  public callFun(key: string, fun: any, time = 1000, ...args: any[]) {
    try {
      if (!key) throw new Error('键值不能为空');
      if (!fun) throw new Error('方法名称不能为空');
      if (!(typeof time === 'number')) throw new Error('参数类型错误');
      this.mapKey = key;
      const interval = this.taskChannel.getInterval();
      if (!interval.has(key)) {
        this.intervalDotNum(this.taskFun, fun, time, ...args);
      }
    } catch (e: any) {
      console.error(e.message)
    }
  }
}
