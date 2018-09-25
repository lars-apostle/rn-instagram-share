# rn-instagram-share
Instagram Media (Video, Image) sharing module (Only for Android, forked from rajatb94/rn-instagram-share).

## Installation

Add to your package.json

"rn-instagram-share": "https://github.com/lars-apostle/rn-instagram-share.git"

**Now link it using react-native tool**
```react-native link```

## Importing
```js
var InstagramShare = require("rn-instagram-share");
```


## share(Type, FilePath)
You can share Image/Video

**Type**: **"video/\*"** for all video formats & **"image/\*"** for all image formats.
**FilePath**: Absoulte path of the media file stored in your phone.

### example:

```js
var InstagramShare = require("rn-instagram-share");
InstagramShare.share("video", "video-url");
```

