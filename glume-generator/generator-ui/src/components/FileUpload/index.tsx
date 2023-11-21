import { defineComponent, TransitionGroup } from "vue";
import { getToken } from "@/utils/auth";
import { ElButton, ElLink, ElUpload, LinkProps, UploadFile } from "element-plus";
import "./index.scss";

export default defineComponent({
  name: 'FileUpload',
  props: {
    modelValue: [String, Object, Array],
    limit: {            // 数量限制
      type: Number,
      default: 5,
    },
    fileSize: {         // 大小限制(MB)
      type: Number,
      default: 5,
    },
    fileType: {         // 文件类型, 例如['png', 'jpg', 'jpeg']
      type: Array,
      default: () => ["doc", "xls", "ppt", "txt", "pdf"],
    },
    isShowTip: {        // 是否显示提示
      type: Boolean,
      default: true
    }
  },
  setup(props) {
    // @ts-ignore
    const { proxy } = getCurrentInstance();
    const baseUrl = import.meta.env.VITE_APP_BASE_API;

    const emit = defineEmits();

    const number = ref(0);
    const uploadList = ref<UploadFile[]>([]);
    const uploadFileUrl = ref(import.meta.env.VITE_APP_BASE_API + "/common/upload"); // 上传文件服务器地址
    const headers = ref({ Authorization: "Bearer " + getToken() });
    const fileList = ref<Record<string, any>[]>([]);

    const showTip = computed(() => props.isShowTip && (props.fileType || props.fileSize));

    watch(() => props.modelValue, (val: Object) => {
      if (val) {
        let temp = 1;
        // 首先将值转为数组
        const list = (Array.isArray(val) ? val : () => {
          return (props.modelValue && typeof props.modelValue === "string") ? props.modelValue.split(',') : [];
        }) as Array<Record<string, any> | string>;

        if (list && list.length > 0) {
          // 然后将数组转为对象数组
          fileList.value = list.map((item: string | Record<string, any>) => {
            if (typeof item === "string") {
              item = { name: item, url: item };
            }
            item.uid = item.uid || new Date().getTime() + temp++;
            return item;
          });
        }
      } else {
        fileList.value = [];
        return [];
      }
    },{ deep: true, immediate: true });

    /**
     * 上传前校检格式和大小
     */
    function handleBeforeUpload(file: File) {
      // 校检文件类型
      if (props.fileType.length) {
        const fileName = file.name.split('.');
        const fileExt = fileName[fileName.length - 1];
        const isTypeOk = props.fileType.indexOf(fileExt) >= 0;
        if (!isTypeOk) {
          proxy.$modal.msgError(`文件格式不正确, 请上传${props.fileType.join("/")}格式文件!`);
          return false;
        }
      }
      // 校检文件大小
      if (props.fileSize) {
        const isLt = file.size / 1024 / 1024 < props.fileSize;
        if (!isLt) {
          proxy.$modal.msgError(`上传文件大小不能超过 ${props.fileSize} MB!`);
          return false;
        }
      }
      proxy.$modal.loading("正在上传文件，请稍候...");
      number.value++;
      return true;
    }

    /**
     * 文件个数超出
     */
    function handleExceed() {
      proxy.$modal.msgError(`上传文件数量不能超过 ${props.limit} 个!`);
    }

    /**
     * 上传失败
     */
    function handleUploadError(err: Error) {
      proxy.$modal.msgError("上传文件失败");
    }

    /**
     * 上传成功回调
     * @param res
     * @param file
     */
    function handleUploadSuccess(res: Record<string, any>, file: UploadFile) {
      if (res.code === 200) {
        uploadList.value.push({ name: res.fileName, url: res.fileName, status: "success", uid:  new Date().getTime() });
        uploadedSuccessfully();
      } else {
        number.value--;
        proxy.$modal.closeLoading();
        proxy.$modal.msgError(res.msg);
        proxy.$refs.fileUpload.handleRemove(file);
        uploadedSuccessfully();
      }
    }

    /**
     * 删除文件
     */
    function handleDelete(index: number) {
      fileList.value.splice(index, 1);
      emit("update:modelValue", listToString(fileList.value));
    }

    /**
     * 上传结束处理
     */
    function uploadedSuccessfully() {
      if (number.value > 0 && uploadList.value.length === number.value) {
        fileList.value = fileList.value.filter((f: Record<string, any>) => f.url !== undefined).concat(uploadList.value);
        uploadList.value = [];
        number.value = 0;
        emit("update:modelValue", listToString(fileList.value));
        proxy.$modal.closeLoading();
      }
    }

    /**
     * 获取文件名称
     */
    function getFileName(name: string) {
      if (name.lastIndexOf("/") > -1) {
        return name.slice(name.lastIndexOf("/") + 1);
      } else {
        return "";
      }
    }

    /**
     * 对象转成指定字符串分隔
     */
    function listToString(list: Record<string, any>[], separator?: string) {
      let strs = "";
      separator = separator || ",";
      for (let i in list) {
        if (list[i].url) {
          strs += list[i].url + separator;
        }
      }
      return strs != '' ? strs.substr(0, strs.length - 1) : '';
    }

    const renderLink = (slotsCallback: Function, clickCallback: Function, options: LinkProps) => <ElLink
        type={options.type}
        href={options.href}
        underline={options.underline}
        onClick={(e: any) => clickCallback(e)}
        v-slots={{
      default: slotsCallback()
    }} />

    const renderUpload = () => <ElUpload ref="fileUpload"
                                         class="upload-file-uploader"
                                         multiple={true}
                                         action={uploadFileUrl.value}
                                         headers={headers}
                                         fileList={fileList.value as UploadFile[]}
                                         limit={props.limit}
                                         showFileList={false}
                                         beforeUpload={handleBeforeUpload}
                                         onError={handleUploadError}
                                         onExceed={handleExceed}
                                         onSuccess={handleUploadSuccess}
    >
      <ElButton type="primary">选取文件</ElButton>
    </ElUpload>

    const renderFileUpload = () => <div class="upload-file">
      {renderUpload()}
      {
        showTip.value && (<div class="el-upload__tip">
            请上传
            {props.fileSize && (<> 大小不超过 <b style="color: #f56c6c">{props.fileSize}MB</b> </>)}
            {props.fileType && (<> 格式为 <b style="color: #f56c6c">{props.fileType.join("/")}</b> </>)}
            的文件
          </div>)
      }
      { // @ts-ignore
        <TransitionGroup class="upload-file-list el-upload-list el-upload-list--text" name="el-fade-in-linear" tag="ul">
          {fileList.value.map((file: Record<string, any>, index: number) => <li class="el-upload-list__item ele-upload-list__item-content" key={file.uid}>
            {renderLink(
                () => <span class="el-icon-document">{getFileName(file.name)}</span>,
                () => {},
                { type: 'default', disabled: false, href: `${baseUrl}${file.url}`, underline: false })
            }
            <div class="ele-upload-list__item-content-action">
              {renderLink(
                  () => '删除',
                  () => handleDelete(index),
                  { type: 'danger', disabled: false, href: '', underline: false })
              }
            </div>
          </li>)}
        </TransitionGroup>
      }
    </div>

    return {
      renderFileUpload
    }
  },
  render() {
    return this.renderFileUpload();
  },
})
