const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    client: {
      overlay: false
    },
    allowedHosts: [
      '12306.free.idcfengye.com', // 允许访问的域名地址，即花生壳内网穿透的地址
      'idcfengye.com'   // .是二级域名的通配符
    ],
  }
})
