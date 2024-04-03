# Ice的视频平台

> 写此项目的原因：我突发奇想，想要有一个类似于BilBili的视频网站，于是去网上找一些开源项目，但是我发现近几年来并没有类似的平台，所以就自己写了一个相关的平台（本人代码写的有点乱，后期有时间将代码重新整理）。<u>后端只能和视频资源放在同一台服务器上运行</u>。本项目采用vue+springboot+element ui+Plyr播放器

>本项目为前后端分离，此页面为后端，前端请前往[Ice的视频平台前端](https://github.com/IceSniperring/video_manage_front_end)查看

### 一、项目部署

本项目支持在服务器上部署，条件是需要有至少三个Http(s)服务端口，源代码上使用的是10001作为后端API程序的Http(s)端口，10002作为前端的端口，10003作为视频的保存以及渲染端口，这需要您自己有相关的经验，等之后有空了，我会将项目进行优化，支持docker部署。

#### 1. 克隆前端代码

```
git clone https://github.com/IceSniperring/video_manage_front_end.git
```

#### 2. 安装node.js依赖

>此步骤需要电脑安装有`node.js`运行环境

```
npm i
```

#### 3. 修改`src/main.js`以及`src/utils/rsaEncrypt.js`

`main.js`代码中有如下两行，第一行时后端服务器的地址，第二行为视频资源地址，请你修改为相应的url

```js
app.provide("serverUrl", "http://192.168.31.200:10001") //服务器地址
app.provide("videoSourceUrl", "http://192.168.31.200:10003") //视频资源地址
```

`rsaEncrypt.js`中需要将变量`publicKey`修改为你的公钥（这里的公钥要和后端的私钥要成对，否则注册完成之后会出现无法登录的情况）

#### 4. 使用vite进行打包并上传服务器

打包命令：

```
vite build
```

将项目的打包完成的`dist`文件夹上传至服务器（dist文件夹是打包的根目录）

>dist文件夹需要是服务器运行的`root目录`，而不是子目录，你也可以将dist文件夹中的内容上传到服务器的root目录下

#### 5. 克隆后端代码

```
git clone https://github.com/IceSniperring/video_manage_back_end.git
```

#### 6. 导入数据库

数据库的资源在根目录下`videoManage_20240403114207ok6b7.sql`，表结构如下：

##### user表

![image-20240403114423116](https://cdn.icesniper.love/typora/image-20240403114423116.png)

`password`我使用了rsa加密，不会直接存储明文密码

`avatar_path`是头像的路径，和视频资源放在同一个目录下，这样方便读取

##### video表

![image-20240403114645568](https://cdn.icesniper.love/typora/image-20240403114645568.png)

`upload_date`是上传的时间

`kind`是分类目录

`file_path`是视频上传的相对于前端代码中的`videoSourceUrl`变量的路径

`post_path`是封面上传的相对于前端代码中的`videoSourceUrl`变量的路径

#### 7. 修改`src/main/resources/application.properties`

默认的内容如下所示

```properties
# 服务器运行的端口
server.port=10001
# 服务器IP地址
server.address=192.168.31.200
spring.application.name=video_manage_after
spring.devtools.add-properties=true
# 热重载配置
spring.devtools.remote.restart.enabled=true
spring.devtools.restart.additional-paths=src/main/java
# 单个视频的上传限制
spring.servlet.multipart.max-request-size=1000MB
# 多视频上传限制
spring.servlet.multipart.max-file-size=10GB
# 与后端服务在同一台服务器上运行的写入视频资源的文件夹地址
upload-video-dir=/opt/1panel/apps/openresty/openresty/www/sites/video_static_resource/index
```

可能大家对最后一个不怎么理解，我展开说一下：

本项目的后端代码只能和本地资源打交道，所以后端服务只能和视频资源服务运行在同一个服务器上，`upload-video-dir`指的是存放视频资源的文件夹，由于我使用的是1panel进行的部署，需要开一个新的http服务，而这个http的root目录就是`/opt/1panel/apps/openresty/openresty/www/sites/video_static_resource/index`所以需要将视频资源的上传地址设置为此目录，这样前端请求的视频资源（包括封面）才能被正确加载。

#### 8. 修改RSA验证的私钥

RSA验证密钥在`src/main/java/com/videomanage/video_manage_after/utils/RSAUtils.java`下修改

原代码为

```java
private static final String publicKeyText = "你的公钥";
private static String privateKeyText = "你的密钥";
```

这里需要你把与前端的公钥以及与其成对的私钥填入，只有这样，登录或者注册请求才能真确被加载。

#### 9. 使用maven打包为jar包

`mvn clean package`

由于`springboot`内置tomcat，所以不需要进行额外的配置，将打包完成的jar包（在项目的target目录下），传输到服务器，并使用` sudo java -jar video_manage_after-0.0.1-SNAPSHOT.jar`运行即可（这里的jar包可以修改名称，使用修改完成的jar包名称运行也可以），这里根据需要给予超级管理员的权限，因为我这里需要写入文件到/opt目录下

当然，你可以使用`systemd`进行服务相关配置，这样就可以在服务器开机时自动启动或者在任何目录下使用`systemctl`来运行后端服务

#### 10. 为视频资源开启一个http服务

视频资源的文件夹命名如下：`分类名/上传时间/视频.mp4`

视频封面的文件夹命名如下：`分类名/上传时间/image/视频封面.jpg`

> 至此，服务应该可以正常运行，如果遇到相关问题，可以在[我的博客](https://blog.icesniper.love)或者`issue`中进行反馈，感谢

###  二、项目本地化

如果你想让项目在本地而非服务器上，这需要

#### 1、修改前端代码的`src/main.js`

```js
app.provide("serverUrl", "http://localhost:10001") //服务器地址
app.provide("videoSourceUrl", "http://localhost:10001/resources") //视频资源地址
```

这时，你应该将`serverlUrl`修改为`localhost:后端运行地址`,`videoSourceUrl`修改为`服务器地址+/resources`，原理是通过WebMvcConfigurer映射文件夹，让前端可以进行访问

#### 2、修改后端代码的`src/main/resources/application.properties`的`upload-video-dir`

这时，和服务器上相似，你需要将`upload-video-dir`文件夹修改为`1`的本地视频资源文件夹的`绝对路径`,这样就并不需要读取http服务提供的视频资源地址，而是本地文件夹的视频资源

### 三、项目截图

#### 主页

![image-20240403124400677](https://cdn.icesniper.love/typora/image-20240403124400677.png)

#### 分类

![image-20240403124432105](https://cdn.icesniper.love/typora/image-20240403124432105.png)

#### 上传界面

![image-20240403124444996](https://cdn.icesniper.love/typora/image-20240403124444996.png)

#### 管理界面

![image-20240403124505630](https://cdn.icesniper.love/typora/image-20240403124505630.png)
