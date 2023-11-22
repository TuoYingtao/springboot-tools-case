import { TaskFun } from "@/utils/scheduled/TaskFun";
import { TaskChannel } from "@/utils/scheduled/TaskChannel";

/**
 * 定时任务工具
 *
 * @Author: TuoYingtao
 * @Date: 2023-10-26 15:00:05
 * @Version: v1.0.0
*/
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
          clearInterval(Number(INTERVAL));
          this.taskChannel.deleteItem(args[0]);
        }
      } else if (args.length > 1) {
        const arr: string[] = args.filter((item) => this.taskChannel.getIntervalValue(item) !== null);
        const values: number[] = this.taskChannel.batchGetIntervalValue(arr).map(value => Number(value));
        values.forEach((value) => clearInterval(value));
        this.taskChannel.batchDeleteItem(arr);
      }
    } catch (e: any) {
      console.error(e.message)
    }
  }

  private intervalExecoute<T extends keyof TaskFun>(fun: any, method: T, time: number, ...args: Parameters<TaskFun[T]>) {
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
        this.intervalExecoute(this.taskFun, fun, time, ...args);
      }
    } catch (e: any) {
      console.error(e.message)
    }
  }
}
