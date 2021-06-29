# Vasu Common Code 
Common code that is required in every app of Vasundhara Infotech 	

#### Screenshots
<img src="https://github.com/jignesh8992/VasuCommonCode/blob/master/screenshots/1.png" width="250"/> <img src="https://github.com/jignesh8992/VasuCommonCode/blob/master/screenshots/4.png" width="250"/> <img src="https://github.com/jignesh8992/VasuCommonCode/blob/master/screenshots/3.png" width="250"/>
	
#### Following points are covered in this code:
1. [Force update](https://github.com/jignesh8992/VasuCommonCode/blob/a60c57b585a4b193385c23ccaade4d3f0c16c7b0/app/src/main/java/com/vasu/appcenter/SplashActivity.kt#L92)
2. [Open ad splash screen](https://github.com/jignesh8992/VasuCommonCode/blob/master/app/src/main/java/com/vasu/appcenter/OpenAdSplashActivity.kt) and [code](https://github.com/jignesh8992/VasuCommonCode/tree/master/app/src/main/java/com/vasu/appcenter/openad) added (Code verification pending)
3. [Theme for splash screen](https://github.com/jignesh8992/VasuCommonCode/blob/cd3f28d9743dc6a6cb48d1e5d56c6354d4c25b6e/app/src/main/res/values/styles.xml#L12) 
4. [Splash screen loading time must be max. 5 sec](https://github.com/jignesh8992/VasuCommonCode/blob/cd3f28d9743dc6a6cb48d1e5d56c6354d4c25b6e/app/src/main/java/com/vasu/appcenter/SplashActivity.kt#L29)
5. [Daily notification code](https://github.com/jignesh8992/VasuCommonCode/blob/80ece0b96b5e3ef9952407b3359cbd4ed1662895/app/src/main/java/com/vasu/appcenter/utilities/Utils.kt#L18)
6. [Exit and rate dailog with feedback module](https://github.com/jignesh8992/VasuCommonCode/tree/master/app/src/main/java/com/vasu/appcenter/rateandfeedback)
7. [More apps View for splash home screen](https://github.com/jignesh8992/VasuCommonCode/blob/master/appcenter/src/main/java/com/example/appcenter/widgets/MoreAppsView.kt)
8. [App center module](https://github.com/jignesh8992/VasuCommonCode/tree/master/appcenter/src/main/java/com/example/appcenter)
9. [Google admob ads](https://github.com/jignesh8992/VasuCommonCode/tree/master/app/src/main/java/com/vasu/appcenter/adshelper)
10. [Gift icon](https://github.com/jignesh8992/VasuCommonCode/blob/master/app/src/main/java/com/vasu/appcenter/adshelper/GiftIconHelper.kt)
11. [In-app purchase](https://github.com/jignesh8992/VasuCommonCode/tree/master/app/src/main/java/com/vasu/appcenter/inapp)
12. [R8 progaurd rules](https://github.com/jignesh8992/VasuCommonCode/commit/8a68208e5034926d5c0c9eca03df8f19e35f8de8)
13. [Firebase crashlytics](https://firebase.google.com/docs/crashlytics/get-started?platform=android)
14. [Firebase analytics](https://firebase.google.com/docs/analytics/get-started?platform=android)
15. [Firebase push notification](https://github.com/jignesh8992/VasuCommonCode/blob/master/app/src/main/java/com/vasu/appcenter/pushnotifications/MyFirebaseMessagingService.kt)
16. [OneSignal push notification](https://github.com/jignesh8992/VasuCommonCode/blob/master/app/src/main/java/com/vasu/appcenter/pushnotifications/OneSignalNotificationHandler.kt)
17. [Gallery](https://github.com/jignesh8992/VasuCommonCode/tree/master/gallery)
18. [Image crop](https://github.com/jignesh8992/VasuCommonCode/tree/master/gallery/src/main/java/com/example/gallery/imagecrop)


#### Notes for network call:
1. API must be protect through native code
2. API request must be using POST method

#### Splash screen follow must be below pattern <br>
<img src="https://github.com/jignesh8992/VasuCommonCode/blob/master/screenshots/flow_chart.png" height="auto" width="600"/>

#### Points to remember at app upload time:
1. Make sure   \<uses-permission android:name="com.android.vending.BILLING"/\> permission is added in [AndroidManifest.xml file](https://github.com/jignesh8992/VasuCommonCode/blob/93d89ef066b404bfb7c7460844e6fee57d662802/app/src/main/AndroidManifest.xml#L7)
2. Replace [google-services.json](https://github.com/jignesh8992/VasuCommonCode/blob/master/app/google-services.json) with your app
3. Change Ads id in [string.xml](https://github.com/jignesh8992/VasuCommonCode/blob/2b0159d4623caedb83bf953aeb89ff374651df3e/app/src/main/res/values/strings.xml#L6) file and must be live
4. minifyEnable must be true in [build.gradle](https://github.com/jignesh8992/VasuCommonCode/blob/2b0159d4623caedb83bf953aeb89ff374651df3e/app/build.gradle#L29) file
5. Change feedback base url in [string.xml](https://github.com/jignesh8992/VasuCommonCode/blob/36ff23d7db986d1bd8177c390079b7ac268221d0/app/src/main/res/values/strings.xml#L14) file (Take it from backend team)
6. [Firebase Analytics](https://firebase.google.com/docs/analytics/get-started?platform=android) and [Crashlytics](https://firebase.google.com/docs/crashlytics/get-started?platform=android) must be configured
7. All API call must be using POST method
8. [Force update](https://github.com/jignesh8992/VasuCommonCode/blob/a60c57b585a4b193385c23ccaade4d3f0c16c7b0/app/src/main/java/com/vasu/appcenter/SplashActivity.kt#L92) code must be added in splash screen
9. The [symbol file](https://github.com/jignesh8992/VasuCommonCode/blob/37d3d471cf82bb96cccdb8728bfd4ce2edbeeba6/app/build.gradle#L59) must be uploaded to make the native code crashes and ANRs easier to analyse and debug.
10. Violation of an interference with an application, third-party ads or device performance policy issue must be resolved by showing ads in the foreground only.
11. [OneSignal push notification](https://github.com/jignesh8992/VasuCommonCode/blob/master/app/src/main/java/com/vasu/appcenter/pushnotifications/OneSignalNotificationHandler.kt) must be added and [onesignal_app_id](https://github.com/jignesh8992/VasuCommonCode/blob/62bad8143c2319120d7ff05e7c0b13d73f383fad/app/build.gradle#L42) must be updated  
12. Add [symbol.zip](https://github.com/jignesh8992/VasuCommonCode/blob/master/README.md#generate-symbolzip-file-for-your-app) file in app folder inside Symbols File if your app contain native code
13. App folder must be follow [given structure](https://github.com/jignesh8992/VasuCommonCode/tree/master/sample) at smb://main_server//Android Group//\<your-name\>//\<app-name\>
	1. Details File
		1. App Name: 
		2. Package Name
		3. App Version:
		4. Alise Name:
		5. Password:
		6. Description Link:
		7. Product Key:
		8. Licence key:
		9. Admob Sheet:  \<sheet-name\>=>\<row-number\>
		10. Google Admob Ads IDs:
			1. App ID:
			2. Banner ID:
			3. Interstitial ID:
			4. Native Advance ID:
		11. Subscription keys:
			1. Weekly:
			2. Monyhly:
			3. Yearly:
	2. App Icon (512*512) File
	3. App Banner File
	4. Bundle	
		1. \<app-name\>_v\<version\>.aab	
	5. Keystore
		1. \<app-name\>.jks
		2. Screenshot of keystore
	6. Private Key
		1. private_key.pepk
	7. Screenshots
		1. 7 inch
		2. 10 inch
		3. Regular
	8. Symbols File
		1. symbols.zip <if required>
		
13. Details File must be in .txt format
14. Place your above app folder in project folder too


#### Message format for app testing:
	App name: <app-name>
	Version: <current-version>
	New apk : <apklist-app-upload-time>
	Changelog:
	1. <change-1>
	2. <change-2>

#### Message format for app upload:

###### Beta Testing:

	Sir need to upload for beta testing this app
	App Name: <app-name>
	App Version: <version>
	App Path: smb://main_server//Android Group//<your-name>//<app-name>//<Bundle>//<latest-bundle>
	App Folder Path: smb://main_server//Android Group//<your-name>//<app-name>
	
###### Live:

	Sir need to make live this app
	App Name: <app-name>
	App Version: <version>
	App Path: smb://main_server//Android Group//<your-name>//<app-name>//<Bundle>//<latest-bundle>
	App Folder Path: smb://main_server//Android Group//<your-name>//<app-name>
	
	

#### Generate symbol.zip file for your app
###### follow step in android studio terminal:

	1. cd app/build/intermediates/cmake/release/obj
	2. zip -r symbols.zip .

	this command will generate a zip file
	send that zip file and app bundle to respected sir.

For info visit : https://support.google.com/googleplay/android-developer/answer/9848633?hl=en
	
## Usage
You can [download](https://github.com/jignesh8992/VasuCommonCode/archive/master.zip)  this repo and start your demo in it or you can use the required code in your application
	
## Demo
[Download APK from here](https://github.com/jignesh8992/VasuCommonCode/raw/master/apk/VasuCommonCode.apk)

## Bugs Report
If you find any bug when using Vasu Common Code, please report [here](https://github.com/jignesh8992/VasuCommonCode/issues/new). Thanks for helping us building a better one.

## Libraries and tools 🛠
<li><a href="https://github.com/jignesh8992/Utilities">Utilities</a></li>
<li><a href="https://developer.android.com/training/data-storage/shared-preferences">Shared Preferences</a></li>
<li><a href="https://github.com/google/gson">Gson</a></li>
<li><a href="https://square.github.io/retrofit/">Retrofit</a></li>
<li><a href="https://developer.android.com/kotlin/coroutines">Coroutines</a></li>
<li><a href="https://github.com/square/okhttp">OkHttp</a></li>
<li><a href="https://developer.android.com/topic/libraries/architecture/room">RoomDB</a></li>
<li><a href="https://material.io/develop/android/docs/getting-started/">Material Design</a></li>
<li><a href="https://github.com/InflationX/Calligraphy">Calligraphy</a></li>
<li><a href="https://github.com/sujithkanna/SmileyRating">Smiley Rating</a></li>
<li><a href="https://github.com/bumptech/glide">Glide</a></li>
<li><a href="https://github.com/shallcheek/RatingBar">RatingBar</a></li>
<li><a href="https://github.com/loopeer/shadow">Shadow</a></li>


## Contributors ✨
<table>
<tr>
<td align="center">
	    <a href="https://github.com/parth0003">
	    <img src="https://avatars.githubusercontent.com/u/57611211?s=400&v=40" height="auto" width="50" style="border-radius:50%"/>
	    <br/>
  	    <b>Parth Sakhiya</b></sub></a>
	    <br/>
	    <sub>Sr. Android Developer<sub>
</td>	
	
<td align="center">
	    <a href="https://github.com/">
	    <img src="https://avatars3.githubusercontent.com/u/12335761?v=4?s=100" height="auto" width="50" style="border-radius:50%"/>
	    <br/>
  	    <b>Gopal Vegad</b></sub></a>
	    <br/>
	    <sub>Sr. Android Developer<sub>
</td>
<td align="center">
	    <a href="https://github.com/">
	    <img src="https://avatars3.githubusercontent.com/u/12335761?v=4?s=100" height="auto" width="50" style="border-radius:50%"/>
	    <br/>
  	    <b>Akshay Harsoda</b></sub></a>
	    <br/>
	    <sub>Sr. Android Developer<sub>
</td>		    
		    
</tr>
</table>

## Contributing
Feel free to contribute code to Vasu Common Code. You can do it by forking the repository via Github and sending pull request with changes.

When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible. Also be sure that all tests are passing.
 
## Developed By
[Jignesh N Patel](https://github.com/jignesh8992) - [jignesh8992@gmail.com](https://mail.google.com/mail/u/0/?view=cm&fs=1&to=jignesh8992@gmail.com&su=https://github.com/jignesh8992/Battery-Information&body=&bcc=jignesh8992@gmail.com&tf=1)

  <a href="https://github.com/jignesh8992" rel="nofollow">
  <img alt="Follow me on Google+" 
       height="50" width="50" 
       src="https://github.com/jignesh8992/Battery-Information/blob/master/social/github.png" 
       style="max-width:100%;">
  </a>
  
  <a href="https://www.linkedin.com/in/jignesh8992/" rel="nofollow">
  <img alt="Follow me on LinkedIn" 
       height="50" width="50" 
       src="https://github.com/jignesh8992/Battery-Information/blob/master/social/linkedin.png" 
       style="max-width:100%;">
  </a>
  
  <a href="https://twitter.com/jignesh8992" rel="nofollow">
  <img alt="Follow me on Facebook" 
       height="50" width="50"
       src="https://github.com/jignesh8992/Battery-Information/blob/master/social/twitter.png" 
       style="max-width:100%;">
  </a>
  
  <a href="https://www.facebook.com/jignesh8992" rel="nofollow">
  <img alt="Follow me on Facebook" 
       height="50" width="50" 
       src="https://github.com/jignesh8992/Battery-Information/blob/master/social/facebook.png" 
       style="max-width:100%;">
  </a>

