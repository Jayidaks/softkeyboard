# Binary Dictionaries #
Binary dictionary format is an efficient and compact way to store dictionaries. The format is defined in LatinIME input method (Android default keyboard), which is part of the open source Android platform.

# XML Dictionary #

XML Dictionary is intermediate format which can be easily transformed to binary dictionary format.

XML file should be stored using UTF-8 encoding for maximum compatibility. UTF-8 encoding is supported practically by all XML tools and it allows any character to appear in the document.

Each `w` element defines one word, which has frequency of `f`. Frequencies don't have to be normalized (different dictionaries have different frequency scales), but they should be integer values and at least 1.

## Samples ##
Some samples of XML dictionaries:
Hebrew (excerpt):
```
<?xml version="1.0" encoding="UTF-8" ?>
<wordlist>
<w f="3847">לא</w>
<w f="3344">את</w>
<w f="2288">של</w>
<w f="2114">זה</w>
<w f="2023">על</w>
<w f="1890">אני</w>
<w f="1496">לי</w>
<w f="1242">כל</w>
<w f="1143">עם</w>
<w f="1095">גם</w>
<w f="1061">מה</w>
<w f="1058">הוא</w>
<w f="1013">אבל</w>
<w f="901">שלי</w>
<w f="890">יש</w>
<w f="836">אם</w>
<w f="763">או</w>
<w f="703">היא</w>
</wordlist>
```

Finnish (excerpt):
```
<?xml version="1.0" encoding="UTF-8" ?>
<wordlist>
<w f="2">rkp</w>
<w f="1">ja</w>
<w f="1">on</w>
<w f="1">ei</w>
<w f="1">että</w>
<w f="1">oli</w>
<w f="1">se</w>
<w f="1">hän</w>
<w f="1">mutta</w>
<w f="1">ovat</w>
<w f="1">kuin</w>
<w f="1">myös</w>
<w f="1">kun</w>
<w f="1">ole</w>
<w f="1">sen</w>
<w f="1">tai</w>
<w f="1">joka</w>
<w f="1">niin</w>
<w f="1">mukaan</w>
<w f="1">jo</w>
<w f="1">vain</w>
<w f="1">ollut</w>
<w f="1">jos</w>
<w f="1">nyt</w>
<w f="1">olisi</w>
<w f="1">voi</w>
<w f="1">hänen</w>
<w f="1">sitä</w>
</wordlist>
```


# XML->Binary Dictionary conversion #
These steps guide you through to convert XML dictionaries to the binary format. Guide works with both **nix and Windows platforms. Probably OSX too.**

  1. Create directory for the language in `<project>/dict_creation/<lang>`. `<lang>` defines the dictionary language, e.g. `he` for Hebrew.
  1. Copy the XML dictionary (named `<lang>.xml`, e.g., `he.xml`) to  `<project>/dict_creation/<lang>/` folder.
  1. Open command prompt and navigate to path `<project>/dict_creation` using `cd` command.
    1. **(on nix)** Type `./makedict_Linux <lang>/<lang>.xml <lang>.dict` (e.g. `./makedict_Linux he/he.xml he.dict`) and press enter.
    1. **(on Windows)** Type `makedict_Windows <lang>\<lang>.xml <lang>.dict` (e.g. `makedict_Windows he/he.xml he.dict`) and press enter.
    * This converts XML dictionary in `<project>/dict_creation/<lang>/<lang>.xml` to binary dictionary `<project>/dict_creation/<lang>.dict`.

The resulting binary dictionary `<lang>.dict` can copied under `assets` folder.

# Old SQLite Database dictionary -> XML Dictionary conversion #
Please see [Issue 240](http://code.google.com/p/softkeyboard/issues/detail?id=240) before converting old dictionaries.
  1. Create directory for the language in `<project>/dict_creation/<lang>`. `<lang>` defines the dictionary language, e.g. `he` for Hebrew.
  1. Copy the SQLite dictionary (named `<lang>`, e.g., `he`) to  `<project>/dict_creation/conversion/` folder.
  1. Open command prompt and navigate to path `<project>/dict_creation/conversion` using `cd` command.
    1. **(on nix)** Type `./SQLiteDictionaryConverter_Linux <lang> ../<lang>/<lang>.xml` (e.g. `./SQLiteDictionaryConverter_Linux he ../he/he.xml`) and press enter.
    1. **(on Windows)** Type `SQLiteDictionaryConverter_Windows <lang> ..\<lang>\<lang>.xml` (e.g. `SQLiteDictionaryConverter_Windows he ..\he\he.xml`) and press enter.
    * This converts SQLite dictionary in `<project>/dict_creation/conversion/<lang>` to XML dictionary `<project>/dict_creation/<lang>/<lang>.xml`.
  1. Remove SQLite dictionary file `<project>/dict_creation/conversion/<lang>`.

The resulting XML Dictionary can be converted to Binary dictionary

# Where to get word-lists (thanks to Jacob Nordfalk) #
A good (but not perfect - since it is not an email/sms/im kind of source) source for word-lists is Wikipedia.
  * Download an archive of the language you want to create a word-list for from [here](http://en.wikipedia.org/wiki/Wikipedia_database)
  * Under Linux shell (this will take a long time):
```
bzcat archive.bz2 | grep -v '<[a-z]*\s' | grep -v '&[a-z0-9]*;' | tr '[:punct:][:blank:][:digit:]' '\n' | tr 'A-Z' 'a-z' | tr 'ÆØÅŜĴĤĜŬ' 'æøåŝĵĥĝŭ' | uniq | sort -f | uniq -c | sort -nr | head -50000 | tail -n +2 | awk '{print "<w f=\""$1"\">"$2"</w>"}'  > dict.xml
```
  * Or for output without accents characters:
```
bzcat archive.bz2 | grep -v '<[a-z]*\s' | grep -v '&[a-z0-9]*;' | tr '[:punct:][:blank:][:digit:]' '\n' | tr 'A-Z' 'a-z' | uniq | grep -o '^[a-z]*$' | sort -f | uniq -c | sort -nr | head -50000 | awk '{print "<w f=\""$1"\">"$2"</w>"}'  > en.xml
```

  * Add the XML header at the beginning of the file:
```
<?xml version="1.0" encoding="UTF-8" ?>
<wordlist>
```
  * Add the XML footer at the end of the file:
```
</wordlist>
```