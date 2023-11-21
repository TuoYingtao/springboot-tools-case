<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <blockquote class="flex justify-between" style="font-size: 14px">
          <div class="flex-1">
            <h2>代码生成后台管理框架</h2>
            <p>
              之所以做一款这样的后台管理系统，是为了跟便捷方便搭建自己的开发框架。这前端框架是基于若衣框架二次开发的，使用Vue3
              + TS + Vite技术栈。
            </p>
            <p>
              <b>当前版本:</b> <span>v{{ version }}</span>
            </p>
            <p>
              <el-tag type="danger">&yen;免费开源</el-tag>
            </p>
            <p>
              <el-button type="primary" icon="Cloudy" plain @click="goTarget('https://github.com/TuoYingtao')">
                访问码云
              </el-button>
              <el-button icon="HomeFilled" plain @click="goTarget('https://github.com/TuoYingtao')">
                访问主页
              </el-button>
            </p>
          </div>
          <div class="flex-1">
            <el-row>
              <el-col :span="12">
                <h2>技术选型</h2>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <h4>后端技术</h4>
                <ul>
                  <li>SpringBoot</li>
                  <li>MyBatis</li>
                  <li>Freemarker</li>
                  <li>Hutool-all</li>
                  <li>Commons-lang3</li>
                  <li>Servlet-api</li>
                  <li>...</li>
                </ul>
              </el-col>
              <el-col :span="6">
                <h4>前端技术</h4>
                <ul>
                  <li>Vue</li>
                  <li>Pinia</li>
                  <li>Element-plus</li>
                  <li>Axios</li>
                  <li>Sass</li>
                  <li>Quill</li>
                  <li>Echarts</li>
                  <li>...</li>
                </ul>
              </el-col>
            </el-row>
          </div>
        </blockquote>
        <hr/>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>联系信息</span>
            </div>
          </template>
          <div class="body">
            <p>
              <i class="el-icon-s-promotion">邮箱：tuoyingtao@163.com</i>
            </p>
            <p>
              <i class="el-icon-chat-dot-round">lssues：<el-link href="https://github.com/TuoYingtao" target="_blank">https://github.com/TuoYingtao</el-link></i>
            </p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>更新日志</span>
            </div>
          </template>
          <el-collapse accordion>
            <el-collapse-item title="v1.0.0 - 2023-11-21">
              <ol>
                <li>代码生成后台管理框架正式发布</li>
              </ol>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="8">
        <el-card class="update-log">
          <template v-slot:header>
            <div class="clearfix">
              <span>捐赠支持</span>
            </div>
          </template>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import { VERSION } from "@/config/global";

const version = ref(VERSION)

const checkboxList = ref()

function goTarget(url) {
  window.open(url, '__blank')
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

