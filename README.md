#Piece  
##基本信息  
**Piece** 是一个基于地理位置的社交软件。**Piece**集树洞，留言簿，交友为一体，你可以交到朋友，参加同城活动，或者单单记录你的生活足迹。另一方面，你可以轻松控制纸条的可见范围，想要交朋友或是参加活动怎么能宅家里？去吧，Pikaqu！寻找一匹丝顶万匹丝的 `One Piece`。  
[详细介绍文档--保持更新--欢迎评论](https://docs.google.com/document/d/1BPh_Ir3cYUgYrWwvEzLnsyh5FhNhZwFx0bYajDmdsy0/edit?usp=sharing)  

##运行要求
- minSdkVersion：15
- Google Play Service
- 梯子 工工工工 [AD: shadowsocks](https://portal.shadowsocks.com/aff.php?aff=3661)

##如何开始  
**Piece**使用Firebase，在开始前请将确保该APP导入到你的Firebase项目中.另外需要以下几点：

- **Google Sign In** 
	- 需要用到：Certificate Fingerprints (SHA-1) Use keytool to get the SHA-1 hash of your signing certificate. [Learn more](https://developers.google.com/android/guides/client-auth).
	- 具体操作：[Google Sign-In](https://firebase.google.com/docs/auth/android/google-signin)
- **Facebook Login** 
	- 根据该APP的包名向[Facebook for Developers](https://developers.facebook.com/)中添加新应用。
	- 具体操作：[Facebook Login](https://firebase.google.com/docs/auth/android/facebook-login)
- **Google Map API**
	- 在 Google APIs 中获取 Google Maps Android API 的 Key
- 替换本例中的`google-services.json`
- 替换如下代码中的`facebook_app_id`，`google_map_api_key`：

```
...
<application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">
        <meta-data
            android:name="com.facebook.sdk.ApplicationId"
            android:value="facebook_app_id" />
        <meta-data
            android:name="com.google.android.maps.v2.API_KEY"
            android:value="google_map_api_key" />
...
```  

- 如遇到其他问题请联系我

##高清无码大图

![piece1](https://github.com/SJPiece/Piece/blob/master/screenshot/device-2016-08-07-1555541.png?raw=true)  
![piece2](https://github.com/SJPiece/Piece/blob/master/screenshot/device-2016-08-07-1614321.png?raw=true)  
![piece3](https://github.com/SJPiece/Piece/blob/master/screenshot/device-2016-08-07-1614101.png?raw=true)  
![piece4](https://github.com/SJPiece/Piece/blob/master/screenshot/device-2016-08-07-1615021.png?raw=true)  
![piece5](https://github.com/SJPiece/Piece/blob/master/screenshot/device-2016-08-07-1619101.png?raw=true)  
![piece6](https://github.com/SJPiece/Piece/blob/master/screenshot/device-2016-08-07-1620171.png?raw=true)  

- [**查看更多截图**](https://github.com/SJPiece/Piece/tree/master/screenshot)

##APP下载

- [![Get it on](https://github.com/SJPiece/Piece/blob/master/screenshot/google-play-badge.png?raw=true)](https://play.google.com/store/apps/details?id=com.studyjams.piece)  
- [Piece Releases](https://github.com/SJPiece/Piece/releases)