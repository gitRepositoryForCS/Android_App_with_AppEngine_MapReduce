# Music player app for Android

A music player mobile application for Android (Android 4-7, API level 18-24) is developed along with cloud server using Google’s App Engine and Firebase. This music player application provides various ways of navigating to an audio file and different music visualizer options. Other than that, it also provides three features: <br />
1. user sign in and sign out, <br />
2. display the most popular songs based on input, <br />
3. users can submit comments and suggestions. <br />
These features are implemented by utilizing cloud services of Google’s App Engine and Firebase. Specifically, an application running on App Engine plays as a server’s role to verify user sign in. It also runs App Engine MapReduce jobs to consume large data stored in Google Cloud Storage and serves relatively small result about popular songs for the app. In addition, user’s comments and suggestions are automatically synchronized with Firebase which makes modifying and analyzing synchronized data really convenient.

To run the whole project, there are several accounts needed: 
- google account <br />
- project in app engine <br />
- project in firebase   <br />
- create a bucket in google cloud storage (hosting input and output files)  <br />
https://cloud.google.com/appengine/docs/java/googlecloudstorageclient/setting-up-cloud-storage#setting_up_your_project
- follow the link below to get a configuration file and put it in the folder ../musicPlayer/mp/app  <br />
https://developers.google.com/identity/sign-in/android/start-integrating    <br />
- follow the link below to get SHA-1 of your app signing certificate    <br />
https://developers.google.com/android/guides/client-auth     <br />
- OAuth 2.0 web client ID <br />


The following gif shows how the visualizer is like when playing an audio file. 

![alt tag](https://github.com/yingchenyingchen/Android_App_with_Appengine_MapReduce/blob/master/visualizer.gif)

Testing:
The following gif shows the test recording of user sign-in and sign-out using Espresso.
![alt tag](https://github.com/yingchenyingchen/Android_App_with_Appengine_MapReduce/blob/master/espressor_signIn_signOut_test.gif)

The following gif shows the test recording of submitting and deleting comments using Espresso.<br />
By clicking the submit button, user's comment is stored locally, and also sent to Firebase. <br />
By long clicking on a submitted comment, the comment will be deleted both on Firebase and Android app. <br />
![alt tag](https://github.com/yingchenyingchen/Android_App_with_Appengine_MapReduce/blob/master/espresso_comment_test.gif)
