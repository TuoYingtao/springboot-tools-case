import {
  ElMessage,
  ElMessageBox,
  ElNotification,
  ElLoading,
  NotificationParamsTyped,
  MessageParamsWithType,
} from 'element-plus';
import { ElMessageBoxOptions } from 'element-plus/es/components/message-box/src/message-box.type';

let loadingInstance: Record<any, any>;

export default {
  // 消息提示
  msg(content: MessageParamsWithType) {
    ElMessage.info(content);
  },
  // 错误消息
  msgError(content: MessageParamsWithType) {
    ElMessage.error(content);
  },
  // 成功消息
  msgSuccess(content: MessageParamsWithType) {
    ElMessage.success(content);
  },
  // 警告消息
  msgWarning(content: MessageParamsWithType) {
    ElMessage.warning(content);
  },
  // 弹出提示
  alert(content: ElMessageBoxOptions['message']) {
    ElMessageBox.alert(content, '系统提示');
  },
  // 错误提示
  alertError(content: ElMessageBoxOptions['message']) {
    ElMessageBox.alert(content, '系统提示', { type: 'error' });
  },
  // 成功提示
  alertSuccess(content: ElMessageBoxOptions['message']) {
    ElMessageBox.alert(content, '系统提示', { type: 'success' });
  },
  // 警告提示
  alertWarning(content: ElMessageBoxOptions['message']) {
    ElMessageBox.alert(content, '系统提示', { type: 'warning' });
  },
  // 通知提示
  notify(content: NotificationParamsTyped) {
    ElNotification.info(content);
  },
  // 错误通知
  notifyError(content: NotificationParamsTyped) {
    ElNotification.error(content);
  },
  // 成功通知
  notifySuccess(content: NotificationParamsTyped) {
    ElNotification.success(content);
  },
  // 警告通知
  notifyWarning(content: NotificationParamsTyped) {
    ElNotification.warning(content);
  },
  // 确认窗体
  confirm(content: ElMessageBoxOptions['message']) {
    return ElMessageBox.confirm(content, '系统提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
  },
  // 提交内容
  prompt(content: ElMessageBoxOptions['message']) {
    return ElMessageBox.prompt(content, '系统提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
  },
  // 打开遮罩层
  loading(content: string) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      background: 'rgba(0, 0, 0, 0.7)',
    });
  },
  // 关闭遮罩层
  closeLoading() {
    loadingInstance.close();
  },
};
