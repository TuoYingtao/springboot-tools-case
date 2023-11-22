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
