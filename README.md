# Projectivy Plugin : Wallpaper Provider

This is a sample project for developing a wallpaper provider plugin for Projectivy Launcher.
- /sample : sample code for the plugin service and its setting activity
- /api : api used to communicate with Projectivy through AIDL (don't change it)
- Version: 1
 
# Usage
- Select "use this template" (right to the fork and start buttons) to fork this as a template
- adapt the sample manifest according to your needs (at least modify the unique id)
- customize the Wallpaper provider service and Settings fragment

# Manifest parameters
- apiVersion: api version used in your code. Projectivy will use it to detect if your plugin is compatible with its own code
- uuid: unique id (format : UUID V4) used to identify your plugin. *You must generate one*
- name: plugin name as displayed in Projectivy plugins list
- settingsActivity: class name of your settings activity. Projectivy will launch it when users click on settings button
- itemsCacheDurationMillis: how long should Projectivy keep the last wallpapers returned by this plugin before considering them expired
- updateMode: int representing the events your plugin is interested in. This should be a combination of TIME_ELAPSED, NOW_PLAYING_CHANGED, CARD_FOCUSED, PROGRAM_CARD_FOCUSED, LAUNCHER_IDLE_MODE_CHANGED (check class WallpaperUpdateEventType)

# How it works
## Timer event
For standard wallpaper providers such as the stock Reddit wallpaper provider, the wallpapers will change according to time events.
First, Projectivy will request wallpaper(s) to the provider selected in the settings, and keep them in cache for itemsCacheDurationMillis.
Then, each X minutes (depending on user settings), Projectivy will refresh the wallpaper by fetching a random one in its cache.
When itemsCacheDurationMillis has expired, Projectivy will ask the plugin again.

## Other events
Other events might lead to wallpaper requests if you set the corresponding updateMode (in that case, there is no cache on Projectivy side, these events are considered dynamic and only the provider knows how to react).
- card focused: each time a card is focused (used by the stock "dynamic colors" wallpaper provider)
- program card focused: each time a program card is focused (used by the stock "current focused program" wallpaper provider)
- now playing changed: each time the "now playing" metadata has changed (could be used to set wallpaper according to the music currently playing)
- launcher idle mode changed: when Projectivy enters/exits launcher idle mode

Each of these events will lead to a call to getWallpapers() depending on your updateMode. They will also provide this function an object containing details regarding this particular event (check the Event class for more details)

# Hints
- Be responsible : even though getWallpapers() isn't called from the UI thread, it doesn't mean you can waste precious device resources (keep in mind that many Android Tv devices have less memory or cpu power than most smartphones).
- If you're fetching wallpapers from an external source, consider using an http cache to prevent flooding it with requests.
- Don't request an update mode you won't use. This is particularly true with card focused events. Those requests might be sent each second or so when a user navigates in the launcher.
- Take advantage of the itemsCacheDurationMillis to limit requests to your plugin and leverage Projectivy's cache (the stock "Reddit" wallpaper provider uses a 12h cache, meaning you will cycle through the same wallpapers for 12 hours before they are updated)
- Don't send too many wallpapers to Projectivy: it will cache them and only use them for itemsCacheDurationMillis, so this will waste memory 
- Respect authors : the wallpaper class allows you to define an author and source uri, fill them to give credit when possible

# Note
This sample is provided as is. It is by no means perfect and should serve as a quick start.
