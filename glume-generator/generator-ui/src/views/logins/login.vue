<template>
  <div class="login">
    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">{{ VITE_APP_NAME }}</h3>
      <el-form-item prop="username">
        <el-input v-model="loginForm.username" type="text" size="large" auto-complete="off" placeholder="账号">
          <template #prefix>
            <svg-icon icon-class="user" class="el-input__icon input-icon" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          size="large"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter="handleLogin">
          <template #prefix>
            <svg-icon icon-class="password" class="el-input__icon input-icon" />
          </template>
        </el-input>
      </el-form-item>
      <el-form-item class="password-form-item" prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          size="large"
          auto-complete="off"
          placeholder="验证码"
          style="flex: 3"
          @keyup.enter="handleLogin">
          <template #prefix>
            <svg-icon icon-class="validCode" class="el-input__icon input-icon" />
          </template>
        </el-input>
        <div class="login-code" style="flex: 1">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <el-checkbox class="checkbox" v-model="loginForm.rememberMe">记住密码 </el-checkbox>
      <el-form-item style="width: 100%">
        <el-button
          class="button-login"
          :loading="loading"
          size="large"
          type="primary"
          style="width: 100%"
          @click.prevent="handleLogin">
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>{{ COPYRIGHT_INFO }}</span>
    </div>
  </div>
</template>

<script setup>
import { JSEncryptUtils } from '@/utils/security/jsencrypt';
import useUserStore from '@/stores/modules/user';
import { CookiesUtils } from '@/utils/request/utils/Cookies';
import { COPYRIGHT_INFO, VITE_APP_NAME } from '@/config/global';
import { LoginApiService } from "@/api/logins/LoginApiService";

const loginApi = new LoginApiService();
const userStore = useUserStore();
const route = useRoute();
const router = useRouter();
const { proxy } = getCurrentInstance();
let cookiesUtils = CookiesUtils.getCooliesUtilsInstance();
const LOGIN_FIELD = {
  USERNAME: 'username',
  PASSWORD: 'password',
  REMEMBER_ME: 'rememberMe',
};

const loginForm = ref({
  username: 'admin',
  password: 'admin123',
  rememberMe: false,
  code: '',
  uuid: '',
});

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入您的账号' }],
  password: [{ required: true, trigger: 'blur', message: '请输入您的密码' }],
  code: [{ required: true, trigger: 'change', message: '请输入验证码' }],
};

const codeUrl = ref('');
const loading = ref(false);
// 验证码开关
const captchaEnabled = ref(true);
// 注册开关
const register = ref(false);
const redirect = ref(undefined);

watch(
  route,
  (newRoute) => {
    redirect.value = newRoute.query && newRoute.query.redirect;
  },
  { immediate: true },
);

function handleLogin() {
  proxy.$refs.loginRef.validate((valid) => {
    if (valid) {
      loading.value = true;
      // 勾选了需要记住密码设置在 cookie 中设置记住用户名和密码
      if (loginForm.value.rememberMe) {
        cookiesUtils.set({ key: LOGIN_FIELD.USERNAME, value: loginForm.value.username, expires: 30 });
        cookiesUtils.set({
          key: LOGIN_FIELD.PASSWORD,
          value: JSEncryptUtils.encrypt(loginForm.value.password),
          expires: 30,
        });
        cookiesUtils.set({ key: LOGIN_FIELD.REMEMBER_ME, value: loginForm.value.rememberMe, expires: 30 });
      } else {
        // 否则移除
        cookiesUtils.batchRemove(LOGIN_FIELD.USERNAME, LOGIN_FIELD.PASSWORD, LOGIN_FIELD.REMEMBER_ME);
      }
      // 调用action的登录方法
      userStore
        .login(loginForm.value)
        .then(() => {
          const query = route.query;
          const otherQueryParams = Object.keys(query).reduce((acc, cur) => {
            if (cur !== 'redirect') {
              acc[cur] = query[cur];
            }
            return acc;
          }, {});
          router.push({ path: redirect.value || '/', query: otherQueryParams });
        })
        .catch(() => {
          loading.value = false;
          // 重新获取验证码
          if (captchaEnabled.value) {
            getCode();
          }
        });
    }
  });
}

function getCode() {
  loginApi.getCodeImg().then((res) => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled.value) {
      codeUrl.value = 'data:image/gif;base64,' + res.img;
      loginForm.value.uuid = res.uuid;
    }
  });
}

function getCookie() {
  const username = cookiesUtils.get(LOGIN_FIELD.USERNAME);
  const password = cookiesUtils.get(LOGIN_FIELD.PASSWORD);
  const rememberMe = cookiesUtils.get(LOGIN_FIELD.REMEMBER_ME);
  loginForm.value = {
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : JSEncryptUtils.decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
  };
}

getCode();
getCookie();
</script>

<style lang="scss" scoped>
:deep(.el-form-item) {
  margin-bottom: 14px;
}

.checkbox {
  float: left;
  font-weight: 600;
  letter-spacing: 0.5px;
  padding-bottom: 10px;
}

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url('~/src/assets/images/login-background.jpg');
  background-size: cover;
}

.title {
  margin: 0px auto 20px auto;
  text-align: center;
  color: #000;
  font-weight: bold;
}

.login-form {
  position: relative;
  z-index: 9999;
  border-radius: 6px;
  backdrop-filter: blur(3px);
  background: rgba(255, 250, 250, 0.5);
  transition: 0.5s ease;
  width: 420px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.password-form-item {
}

.login-code-img {
  height: 38px;
  padding-left: 12px;
}

// @import url('https://rsms.me/inter/inter-ui.css');
/* 解决双击选择样式背景颜色
  ::selection {
  background: #2D2F36;
  }
  ::-webkit-selection {
  background: #2D2F36;
  }
  ::-moz-selection {
  background: #2D2F36;
  }
*/

.button-login {
  z-index: 1;
  position: relative;
  font-size: inherit;
  font-family: inherit;
  color: white;
  outline: none;
  border: none;
  width: 90%;
  height: 35px;
  background-color: hsl(236, 32%, 26%) !important;
  overflow: hidden;
  transition: color 0.4s ease-in-out;
}

.button-login::before {
  content: '';
  z-index: -1;
  position: absolute;
  top: 50%;
  left: 50%;
  width: 2em;
  height: 2em;
  border-radius: 50%;
  background-color: hsl(236, 28%, 18%);
  transform-origin: center;
  transform: translate3d(-50%, -50%, 0) scale3d(0, 0, 0);
  transition: transform 0.45s ease-in-out;
}

.button-login:hover {
  cursor: pointer; // 鼠标指针样式
  color: white;
}

.button-login:hover:before {
  transform: translate3d(-50%, -50%, 0) scale3d(15, 15, 15);
}

.button-login:active:before {
  background-color: hsl(236, 28%, 18%);
  transform: translate3d(-50%, -50%, 0) scale3d(15, 15, 15);
}
</style>
