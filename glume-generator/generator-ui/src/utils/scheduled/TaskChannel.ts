export class TaskChannel {
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
