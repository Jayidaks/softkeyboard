# How to install fonts in Android #

**Rooting an unrooted device will revoke your warranty!**

## Note ##
The latest Cyanogen build (4.1.9.2 or higher) include the many glyphs! Including Hebrew and Arabic.
So, if you have a rooted phone, I suggest to install CyanogenMOD ROM [get it here](http://forum.xda-developers.com/forumdisplay.php?f=448).
Or you can read on...

# Option one: install fonts manually #
## Required ##
  1. A rooted Android. If you want to know how to root an Android, read about it [here](http://haykuro.theiphoneproject.org/?page_id=35).
  1. fonts (Cyanogen's fonts can be downloaded from [here](http://softkeyboard.googlecode.com/files/cm_fonts.rar))
  1. [ADB tool](http://softkeyboard.googlecode.com/files/usb_driver_1.6-r1.rar), which is a part of the Android SDK.
  1. You may need to install the drivers. Get them from [here](http://softkeyboard.googlecode.com/files/usb_driver_1.6-r1.rar).

## Steps ##
  * Connect your phone using USB to the computer.
  * Unzip the fonts to the ADB folder, e.g., "c:\adb\"
  * Change directory to the ADB folder.
  * Make the system partition writable:
    * adb root
    * adb shell mount -o rw,remount -t yaffs2 /dev/block/mtdblock3 /system
  * Copy the fonts to the system partition, by typing the following command (in this example I assume the fonts are stored on the local computer at "c:\adb\"):
    * adb push c:\adb\ /system/fonts/
  * We're done - just reboot the device:
    * adb shell reboot

# Option two: use recovery mode to install update #
## Required ##
  1. A rooted Android. If you want to know how to root an Android, read about it [here](http://haykuro.theiphoneproject.org/?page_id=35).
  1. The update archive: you can download it [here](http://softkeyboard.googlecode.com/files/CM_fonts_install_ROM_UPDATE.zip)

## Steps ##
  * Copy the 'install\_CM\_fonts.zip' file into you SDCARD root directory.
  * Boot into recovery mode (probably pressing HOME while phone starts)
  * Select "Flash ZIP from SDCARD"
  * Select 'install\_CM\_fonts.zip'
  * Reboot your phone

## More information ##
You can get help on how to do it at various forums across the internet.
  * http://forum.xda-developers.com/showthread.php?t=442480 (The best!)
  * http://iandroid.co.il/phpBB3/viewtopic.php?f=5&t=601 (Hebrew)