## Welcome to Hamster Client for Android ##


### Download ###

1. **hamster-client-android**

		git clone https://github.com/rynkowsg/hamster-client-android.git

2. **dbus-java** *(required by hamster-client-android)*

		git clone git@github.com:rynkowsg/dbus-java.git --branch android

3. **libmatthew-java** *(required by dbus-java)*

		wget http://www.java2s.com/Code/JarDownload/libmatthew/libmatthew-0.8-x86_64-sources.jar.zip
		unzip libmatthew-0.8-x86_64-sources.jar.zip & rm libmatthew-0.8-x86_64-sources.jar.zip
		unzip libmatthew-0.8-x86_64-sources.jar -d libmatthew-0.8-x86_64-sources & rm libmatthew-0.8-x86_64-sources.jar

### Project Setup ###

#### On Linux ####
1. Download sources.
2. Make links:
		
		ln -s dbus-java\src\main\java\org\freedesktop\dbus      hamster-client-android\app\src\main\java\org\freedesktop\dbus      
		ln -s dbus-java\src\main\java\org\freedesktop\DBus.java hamster-client-android\app\src\main\java\org\freedesktop\DBus.java
		ln -s libmatthew-0.8-x86_64-sources\cx                  hamster-client-android\app\src\main\java\cx


3. Import project in the Android Studio.



#### On Windows ####
1. Download sources (on Windows to use wget and unzip you should install Cygwin)

2. Make links:

		cmd
		mklink /j hamster-client-android\app\src\main\java\org\freedesktop\dbus      dbus-java\src\main\java\org\freedesktop\dbus
		mklink /h hamster-client-android\app\src\main\java\org\freedesktop\DBus.java dbus-java\src\main\java\org\freedesktop\DBus.java
		mklink /j hamster-client-android\app\src\main\java\cx                        libmatthew-0.8-x86_64-sources\cx
		exit

3. Import project in the Android Studio.


