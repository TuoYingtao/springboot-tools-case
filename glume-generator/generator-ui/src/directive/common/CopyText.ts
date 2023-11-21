/**
 * v-copyText 复制文本内容
 */
import {App, DirectiveBinding} from 'vue';
export default {
  install(app: App<Element>) {
    app.directive('copyText', {
      mounted(el: any, binding: DirectiveBinding){
        const eventType = Object.keys(binding.modifiers)[0] || 'click';
        if (binding.arg === "callback") {
          el.$copyCallback = binding.value;
        } else {
          el.$copyValue = binding.value;
          const handler = () => {
            copyTextToClipboard(el.$copyValue);
            if (el.$copyCallback) {
              el.$copyCallback(el.$copyValue);
            }
          };
          el.addEventListener(eventType, handler);
        }
      },
      beforeMount(el, binding: DirectiveBinding) {
        const eventType = Object.keys(binding.modifiers)[0] || 'click';
        const handler = () => {
          copyTextToClipboard(el.$copyValue);
          if (el.$copyCallback) {
            el.$copyCallback(el.$copyValue);
          }
        };
        el.$destroyCopy = () => el.removeEventListener(eventType, handler);
      }
    })
  },
}

function copyTextToClipboard(input: any, { target = document.body } = {}) {
  const element = document.createElement('textarea');
  const previouslyFocusedElement = document.activeElement;

  element.value = input;

  // Prevent keyboard from showing on mobile
  element.setAttribute('readonly', '');

  element.style.contain = 'strict';
  element.style.position = 'absolute';
  element.style.left = '-9999px';
  element.style.fontSize = '12pt'; // Prevent zooming on iOS

  target.append(element);
  element.select();

  // Explicit selection workaround for iOS
  element.selectionStart = 0;
  element.selectionEnd = input.length;

  let isSuccess = false;
  try {
    isSuccess = document.execCommand('copy');
  } catch { }

  element.remove();

  const selection = document.getSelection();
  if (selection) {
    const originalRange = selection.rangeCount > 0 && selection.getRangeAt(0);
    if (originalRange) {
      selection.removeAllRanges();
      selection.addRange(originalRange);
    }
  }

  // Get the focus back on the previously focused element, if any
  if (previouslyFocusedElement) {
    // @ts-ignore
    previouslyFocusedElement.focus();
  }

  return isSuccess;
}
