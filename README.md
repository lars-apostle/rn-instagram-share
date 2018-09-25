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


## share(Type, mediaUrl)
You can share Image/Video

**Type**: **"video"** for video & **"image"** for all images.
**mediaUrl**: The URL of the location of the media file.

### example:

```js
import InstagramShare from "rn-instagram-share";
InstagramShare.share("video", "video-url");
```

