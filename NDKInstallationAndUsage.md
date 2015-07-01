# Introduction #

The Android NDK provides tools that allow Android application developers to embed components that make use of native code in their Android applications.

Android applications run in the Dalvik virtual machine. The NDK allows developers to implement parts of their applications using native-code languages such as C and C++. This can provide benefits to certain classes of applications, in the form of reuse of existing code and in some cases increased speed.

The NDK provides:

A set of tools and build files used to generate native code libraries from C and C++ sources
A way to embed the corresponding native libraries into application package files (.apks) that can be deployed on Android devices
A set of native system headers and libraries that will be supported in all future versions of the Android platform, starting from Android 1.5
Documentation, samples, and tutorials

_(From [Android NDK web site](http://developer.android.com/sdk/ndk/1.6_r1/index.html))_

Currently, anysoftkeyboard uses NDK only for dictionary querying. See issue [204](http://code.google.com/p/softkeyboard/issues/detail?id=204)

# Installation #

Prerequisites: SDK installed.

## Windows ##
_Tested with Windows 7, 64bit. Should also work with XP (32bit) and Vista (32bit & 64bit) according to NDK web site._

  1. Install Cygwin from [here](http://www.cygwin.com). Cygwin site has quite large [documentation](http://cygwin.com/cygwin-ug-net/setup-net.html#internet-setup) on installation if you feel unsure about it. Install Cygwin to path without spaces for maximum compatibility. Later we assume that install directory is "c:\cygwin\".
  1. Install latest "make"  package (GNU Make) by starting the Cygwin setup.exe again after installation. The make package can be found under "Devel" category, do use the search function. GNU Make version 3.81 or newer should work.
  1. Download NDK from [here](http://developer.android.com/sdk/ndk/). Choose zip file corresponding to Windows platform.
  1. Unzip NDK to folder of your liking. DO **NOT** unzip it to folder with spaces. Later we assume that unzip-directory is `D:\apps\Android_NDK`. After zip extraction, the folder should contain one folder named "android-ndk-[r4](https://code.google.com/p/softkeyboard/source/detail?r=4)".
  1. Put above folder (`D:\apps\Android_NDK\android-ndk-r4` to PATH environmental variable.

## Linux ##
Quite similar to Windows.

## OSX ##
Quite similar to Linux.

# Building (nativeime component) #

## Windows ##
_Tested with Windows 7, 64bit. Should also work with XP (32bit) and Vista (32bit & 64bit) according to NDK web site._

Assumptions: softkeyboard SVN root is in `d:\softkeyboard`. This folder should contain `project` folder, and file called `Application.mk` inside the project folder.

  1. Open C:\cygwin\Cygwin.bat and type `cd /cygdrive/d/softkeyboard/project` and press _Enter_. Note that folder names and commands are case-sensitive. You can use tab to auto-complete folder names. If you installed NDK to c drive remember to change `/cygdrive/d/` to `/cygdrive/c/`.
  1. Type `ndk-build` and press _Enter_. This will build the `nativeime` component. You should see the following output if everything goes well:
![http://softkeyboard.googlecode.com/files/ndk_usage_2.png](http://softkeyboard.googlecode.com/files/ndk_usage_2.png).

  * If everything goes OK, `libnativeime.so` should appear in `<SVN_ROOT>/project/libs/armeabi` where `<SVN_ROOT>` stands for root of anysoftkeyboard svn (`d:\softkeyboard`).
  * If there's compilation errors, they are shown.
  * You can force rebuild by `ndk-build -B`. By default compilation is done when it's needed (if the code is changed).
  1. Continue using Eclipse as normal, it includes the above component (the `so` file) automatically next time it builds the java code.

## Linux ##
Similar to Windows: Open console window instead of running `Cygwin.bat`.

## OSX ##
Similar to Windows: Open console window instead of running `Cygwin.bat`.