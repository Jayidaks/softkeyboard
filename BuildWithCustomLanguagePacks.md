# How to set up your environment? #

  1. download and install Helios (http://www.eclipse.org)
  1. download and install android SDK (http://developer.android.com/sdk/index.html)
  1. install ADT plugin (http://developer.android.com/sdk/index.html) (or make sure it's up to date)
  1. nstall SDK components: SDK 1.5, SDK 2.3.3. (Menny confirmed me that should be enough). But at the time I'm writing this doc, I've installed:
    * 1.5, API 3, rev4
    * 1.6, API 4, rev3
    * 2.1-update1, API 7, rev2
    * 2.2, API 8, rev2
    * 2.3, API 9, rev1
    * 2.3.3 API 10, rev1
  1. Install svn and checkout the code: svn checkout http://softkeyboard.googlecode.com/svn/trunk/ softkeyboard-read-only. Alternatively, you can use a SVN gui like TortoiseSVN or use one provided an eclipse plugin. At the time I'm writing this doc it's [r1641](https://code.google.com/p/softkeyboard/source/detail?r=1641). Menny told me you can also use one of the latest tag (which corresponds to market versions)
  1. In eclipse import the following projects from what you've just checkouted:
    * the one under the project directory
    * the one under !AnySoftKeyboardAPI
Project called AnySoftKeyboard-trunk should compile with no errors
I have an error in !AnySoftKeyboardAPI but it's working anyhow

# How to create APK and test it on 2.2, 2.3, or 2.3.3 #
  1. Right Click on AnySoftKeyboard-trunk then Android Tools/Export signed application package export signed APK
  1. Project should be AnySoftKeyboard-trunk, Next, Choose to create a new keystore
  1. Choose a file name, you have an apk that should run on 2.2, 2.3, and 2.3.3 android (that may run on other flavours, I haven't tested yet)
  1. Create an AVD with an android flavour
  1. Launch emulator with your favourite android flavour
  1. install the app: adb install -r your.apk
  1. Configuration is explained here

# How to bundle Anysoft keyboard + english and lao keyboards in a single APK #
  1. duplicate the 'project' directory to 'project\_sabai'
  1. delete AnySoftKeyboard-trunk in eclipse (strike suppr) but do not delete files on disk!
  1. import the project under 'project\_sabai'. AnySoftKeyboard-trunk is back in workspace :)
  1. import lao language pack under LanguagePacks/Lao
  1. you have a new AnySoftKeyboardLanguagePackLao with 1 error (Conversion to Dalvik format failed with error 1) but it's working anyhow.
  1. Merge AnySoftKeyboardLanguagePackLao strings.xml file in AnySoftKeyboard-trunk strings.xml file
  1. Merge AnySoftKeyboardLanguagePackLao keyboards.xml file in AnySoftKeyboard-trunk keyboards.xml file (you can choose order of keyboards with index attribute). I also removed
  1. Copy AnySoftKeyboardLanguagePackLao  res/drawable images in AnySoftKeyboard-trunk res/drawable directory
  1. Copy AnySoftKeyboardLanguagePackLao lao\_qwerty.xml file in AnySoftKeyboard-trunk res/xml directory
  1. Build a new apk. You're done :)

Note: To install fonts on emulator you need to follow this doc: http://code.google.com/p/softkeyboard/wiki/InstallFonts and launch manually emulator with this option "-partition-size 128".