# Tiny SDK for Android - Quick Start

Type-safe HTTP client for Android

## Features

- 
- 
- 
- 
- 

## Integration

### Dependencies

#### Gradle
Add a dependency in your module `build.gradle`:
```
compile 'com.tiny.tiny-android-sdk:tiny-api:1.0'
```
or
#### Maven

```
<dependency>
  <groupId>com.tiny.tiny-android-sdk</groupId>
  <artifactId>tiny-api</artifactId>
  <version>1.0</version>
  <type>pom</type>
</dependency>
```
### How to use

#### 1. Edit Your Manifest
 - Open your `/app/src/main/res/values/strings.xml` file
 - Add the following:
 ```
 <string name="base_url">https://tiny.com.vn/api/v1/</string>
 ```
 - Open the `/app/manifest/AndroidManifest.xml` file.
 - Add a `uses-permission` element to the manifest:
 ```
 <uses-permission android:name="android.permission.INTERNET"/>
 ```
 - Add the following `meta-data` element:
 ```
 <meta-data android:name="com.tiny.sdk.url" 
        android:value="@string/base_url"/>
 ```
 
 #### 2. Initialize TinySDK
 - Create `Application` class
 - Add the following code to initialize TinySDK:
 ```
 public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TinySDK.initialize(this);
    }
}
 ```
 
 #### 3. Use Tiny API
  - 
  - 
  -
 
## Developed By
TinyRush Team - tinyrush.team@gmail.com

## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
