# Android_app_music_player 

The music player application with visualizations is designed, implemented, and tested on Android cell phones and tablets. The major features include flexible navigations, various visualizations, and common music controlling functionalities, such as play, pause, fast forward, fast rewind, play next, play previous, etc. 

The major work backend server does is to provide recommendation data for the app. It uses chaining mapreduce jobs to filter large data and save it on cloud storage. Android app clients get the data through http requests. 

(Support Android versions from Jelly Bean to Nougat, API level 18-24)

To run the whole project, there are several accounts needed: \r\n
-project in app engine  \r\n
-project in firebase   \r\n
-bucket in google cloud storage   \r\n
- follow the link below to get a configuration file and put it in the folder ../musicPlayer/mp/app   \r\n
https://developers.google.com/identity/sign-in/android/start-integrating    \r\n
-follow the link below to get SHA-1 of your app signing certificate    \r\n
https://developers.google.com/android/guides/client-auth      \r\n


The following gif shows how the visualizer is like when playing an audio file.

![alt tag](https://github.com/yingchenyingchen/Android_App_with_Appengine_MapReduce/blob/master/visualizer.gif)

Testing:
The following gif shows the test recording of user sign-in and sign-out using Espresso.
![alt tag](https://github.com/yingchenyingchen/Android_App_with_Appengine_MapReduce/blob/master/espressor_signIn_signOut_test.gif)

The following gif shows the test recording of submitting comments and deleting comments using Espresso.
![alt tag](https://github.com/yingchenyingchen/Android_App_with_Appengine_MapReduce/blob/master/espresso_comment_test.gif)
