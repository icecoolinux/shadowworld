# Shadow World
The first 3D audio videogame in Android  

<img src="https://github.com/icecoolinux/shadowworld/blob/main/imgs/logo.png" data-canonical-src="https://github.com/icecoolinux/shadowworld/blob/main/imgs/logo.png" width="400" height="400" />

## Binary
**app-debug.apk**
  

## Compile project and generate apk  

Import project using Android Studio  

### Compile OpenAL  
* Install Cygwin and the make package  
* Download Android NDK (I used android-ndk-r9d)  

* You must to edit:  
**compilaropenal.bat**: Fix cygwin, this project and NDK paths.  
**crearjniheader.bar**: Fix android sdk and project java classes paths.  

* Make project in Android Studio  
* From console and in the project path, execute **compilaropenal.bat**  
* Make project again  

