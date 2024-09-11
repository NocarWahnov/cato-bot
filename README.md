# !addhtml Boilerplate
## 9to5mac.com
```
!addhtml CID https://9to5mac.com/ article.article.standard h2.h1 h2.h1>a div.article__content>div.article__excerpt>p div.post-meta>span.meta__post-date
```
## guildwars2.com News
```
!addhtml CID https://www.guildwars2.com/en/news/ li.blog-post h3.blog-title h3.blog-title>a div.text>p:first-child p.blog-attribution
```
## swtorista.com
```
 !addhtml CID https://swtorista.com/ .news-card .title .title p 0
```
## Hunt Showdown News
```
 !addhtml CID https://www.huntshowdown.com/news .news-feature-text a>h3 a p h5>a>span
```

# To-Do
- Bookmark Command / Feature?
- !addhtml: Falls hrefs keine vollständige url haben, darf die angehängte url nur .com und nicht .com/de oder so sein
- Tutorial link für !editHTML

# Changelog
## 04.07.24
- !addthtml: If you do not want to provide a title, link, paragraph or date, you can now type 0 and those won't be added.
- AutoUpdateFeed got stuck because the "invalid parameter size" error of the Teamspeak API wasn't handled correctly. This should be fixed now.
- XML feed descriptions now have a maximum character size of 500 to avoid the "invalid parameter size" error of the Teamspeak API.
## 18.06.24
- XmlHandler now removes any tags of the description before returning the String
- Readme now contains Boilerplate for !addhtml
## 11.06.24
- Commented Code
## 10.06.24
- !refresh command added - Manually refresh your feeds once
## 09.06.24
- Added !makebackup
- Added !printbackup FILENAME
- !shutdown the command now removes all feeds from the channel descriptions

# Done that
- Mit .rss Dateien arbeiten
- HTML Websites mit Bot Command fetchen
- Login zum Server mit YAML / Environment Variables
- ID schöner im Chat ausgeben
- Yaml Speicherung anpassen, dass die Strings angehängt und nicht ersetzt werden.
- Regelmäßiges aktualisieren der Quellen
- Es gibt HTML Links, die nur den Dateipfad als href haben. Dann muss die URL vorne rangehängt werden
- Wenn das Projekt verpackt wird, funktioniert der ScheduledExecutorService nicht mehr
- Per Befehl aus Backup laden
- !h command schöner machen (Mit Überschriften)
- Print Backupfile as Message
- Refresh feeds on command
