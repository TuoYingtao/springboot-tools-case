## CORS简介

​	[CORS](https://developer.mozilla.org/zh-CN/docs/Glossary/CORS)是一个W3C标准，全称是"跨域资源共享”（Cross-origin resource sharing）。它允许浏览器向跨源(协议 + 域名 + 端口)服务器，发出[XMLHttpRequest](https://developer.mozilla.org/zh-CN/docs/Web/API/XMLHttpRequest)请求，从而克服了[AJAX](https://developer.mozilla.org/zh-CN/docs/Web/Guide/AJAX)只能同源使用的限制。[CORS](https://developer.mozilla.org/zh-CN/docs/Glossary/CORS)需要浏览器和服务器同时支持。它的通信过程，都是浏览器自动完成，不需要用户参与。

​	对于开发者来说，[CORS](https://developer.mozilla.org/zh-CN/docs/Glossary/CORS)通信与同源的[AJAX](https://developer.mozilla.org/zh-CN/docs/Web/Guide/AJAX)/[Fetch](https://fetch.spec.whatwg.org/)通信没有差别，代码完全一样。浏览器一旦发现请求跨源，就会自动添加一些附加的头信息，有时还会多出一次附加的请求，但用户不会有感觉。因此，实现[CORS](https://developer.mozilla.org/zh-CN/docs/Glossary/CORS)通信的关键是服务器。只要服务器实现了[CORS](https://developer.mozilla.org/zh-CN/docs/Glossary/CORS)接口，就可以跨源通信。

​	跨源 [HTTP](https://developer.mozilla.org/zh-CN/docs/Web/HTTP) 请求的一个例子：运行在 `https://domain-a.com` 的 JavaScript 代码使用 [`XMLHttpRequest`](https://developer.mozilla.org/zh-CN/docs/Web/API/XMLHttpRequest) 来发起一个到 `https://domain-b.com/data.json` 的请求。

## 什么是跨域

​	当一个请求URL的协议、域名、端口三者之间任意一个与当前页面URL不同即为跨域。

| 当前页URL                 | 被请求URL                       | 是否跨域 | 原因                         |
| ------------------------- | ------------------------------- | -------- | ---------------------------- |
| http://www.test.com/      | http://www.test.com/index.html  | 否       | 同源（协议、域名、端口相同） |
| http://www.test.com/      | https://www.test.com/index.html | 是       | 协议不同（`http`/`https`）   |
| http://www.test.com/      | http://www.baidu.com/           | 是       | 主域名不同（`test`/`baidu`） |
| http://www.test.com/      | http://blog.test.com/           | 是       | 子域名不同（`www`/`blog`）   |
| http://www.test.com:8080/ | http://www.test.com:7001/       | 是       | 端口号不同（`8080`/`7001`）  |

## 非同源限制

1. 无法读取非同源网页的 Cookie、LocalStorage 和 IndexedDB
2. 无法接触非同源网页的 DOM
3. 无法向非同源地址发送 AJAX 请求

## 测试跨域请求

```javascript
let xhr = new XMLHttpRequest();
xhr.open('GET', 'http://192.168.50.117/cors/annotationCors')
// 让Ajax请求都带上Cookie, 注意：在同一个站点下使用 withCredentials 属性是无效的
xhr.withCredentials = true
xhr.setRequestHeader('token', 'SFR13456')
xhr.send()
xhr.onload = function(e) {
    // 打印错误信息
    console.log(e.responseText)
}
```

## 参考文献

【1】[跨源资源共享（CORS）](https://developer.mozilla.org/zh-CN/docs/Web/HTTP/CORS)

【2】[跨域资源共享 CORS 详解](http://www.ruanyifeng.com/blog/2016/04/cors.html)

