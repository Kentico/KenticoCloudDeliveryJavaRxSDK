Release Guide for Delivery JavaRX SDK

Before releasing a new version, ensure the following:

New tests have been created for any new functionality you have added
All existing tests pass
Pull latest from master branch

To release a new version of the SDK you will need to follow the below guide:

First, add the following lines to your local.properties within the Android SDK project:

BINTRAY_USER=<bintray username>
BINTRAY_KEY=<bintray api key>
BINTRAY_GPG=<gpg key for signing artifacts in order to be transferred to maven>
SONATYPE_USER=<sonatype user>
SONATYPE_PASSWORD=<sonatype password>
SDK_VERSION=<sdk version, i.e '3.0.2'>
RX_VERSION_DESC=<description for what is included in this version to rx sdk>
CORE_VERSION_DESC=<description for what is included in this version to core sdk>
ANDROID_VERSION_DESC=<description for what is included in this version to android sdk>

The Bintray user must be part of the "Kentico" organization on Bintray before publishing. To request access to 'Kentico' organization please email developerscommunity@kentico.com.

API Key is the Bintray API key for this specific user, you can locate this from your user profile page.

GPG Password is used for the public/ private GPG keypair, this is used for signing the library during upload to make it compatible with Maven. You will need to generate new GPG keys using git: https://help.github.com/articles/generating-a-new-gpg-key/ and then add the keys to your user profile on JCenter. The Password is the password you created while generating your GPG keys.

Once you have the Bintray local properties in place you are ready to push a new version to Bintray. To do this do the following:

1. Open 'Terminal' in Android Studio
2. Type 'gradlew clean build' and wait for 'BUILD SUCCESSFUL'
3. Type 'gradlew build bintrayUpload' and wait for 'BUILD SUCCESSFUL'
  
The artifacts should be pushed to both Bintray (jcenter) and Maven. Maven will take a bit to show the new versions, but Bintray should be almost instantaneous
