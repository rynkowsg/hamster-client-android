Hamster Client for Android [![Build Status](https://travis-ci.org/rynkowsg/hamster-client-android.svg?branch=master)](https://travis-ci.org/rynkowsg/hamster-client-android)
==========================

A mobile app addressed to users of existing linux desktop application - Hamster Time Tracker.
Same as desktop app, Hamster Client allows a time tracking.
It means the app stores data about user activities in time.

The application allows the user to connect to their desktop
and synchronize data between Hamster Client and Hamster Time Tracker.

More information about Hamster Time Tracker you can find at [project's website][projecthamster]
and [GitHub page][projecthamster-github].



The Clean Architecture
----------------------

At the project I have applied [The Clean Architecture][the-clean-architecture]
proposed by Robert C. Martin.

About Android approaches of The Clean Architecture you can read [here][the-clean-way]
and [there][progmag-ca].

You can also look at other existed implementations:

 * [Android-CleanArchitecture][android-cleanarchitecture] by Fernando Cejas
 * [shoppinglist-clean-architecture-example][shoppinglist] by Michał Charmas
 * [Clean-Contacts][clean-contacts] by Christian Panadero



TODO
----

 * implement data layer based on [Virgil Dobjanschi' concepts][yt-rest-implementation]
 * improve MVP according to [implementation of Michał Charmas][shoppinglist-mvp]
 * make the UI modern and comfortable to use.



Libraries
---------

 * [ButterKnife][butterknife]
 * [Dagger 2][dagger2]
 * [Lombok][lombok]
 * [RxJava][rxjava]
 * *and [others][dependencies.gradle]*

Project provides communication with host by D-Bus. In order to do that I used:

 * [dbus-java][dbus-java] - D-Bus library for Java
     (the library was modified for the project purpose)
 * libmatthew-java - a dependency of dbus-java ([link1][libmatthew-java-1],
     [link2][libmatthew-java-2], [link3][libmatthew-java-3], [link4][libmatthew-java-4]).



License
-------

    Copyright (C) 2015 Grzegorz Rynkowski

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [projecthamster]: https://projecthamster.wordpress.com
 [projecthamster-github]: https://github.com/projecthamster/hamster
 [dagger2]: http://google.github.io/dagger/
 [butterknife]: http://jakewharton.github.io/butterknife
 [rxjava]: https://github.com/ReactiveX/RxJava
 [lombok]: https://projectlombok.org
 [dbus-java]: https://github.com/rynkowsg/dbus-java
 [libmatthew-java-1]: http://www.java2s.com/Code/Jar/l/Downloadlibmatthew08x86sourcesjar.htm
 [libmatthew-java-2]: http://www.java2s.com/Code/Jar/l/Downloadlibmatthewjava08jar.htm
 [libmatthew-java-3]: https://github.com/rynkowsg/libmatthew-java
 [libmatthew-java-4]: https://bitbucket.org/rynkowsg/libmatthew-java
 [the-clean-architecture]: http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html
 [the-clean-way]: http://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/
 [progmag-ca]: http://programistamag.pl/przejrzysty-i-testowalny-kod-na-androidzie-sprobujmy-z-clean-architecture/
 [android-cleanarchitecture]: https://github.com/android10/Android-CleanArchitecture
 [shoppinglist]: https://github.com/mcharmas/shoppinglist-clean-architecture-example
 [shoppinglist-mvp]: https://github.com/mcharmas/shoppinglist-clean-architecture-example/tree/master/presentation/src/main/java/pl/charmas/shoppinglist/presentation/base
 [yt-rest-implementation]: https://www.youtube.com/watch?v=xHXn3Kg2IQE
 [clean-contacts]: https://github.com/PaNaVTEC/Clean-Contacts
 [dependencies.gradle]: https://github.com/rynkowsg/hamster-client-android/blob/master/buildsystem/dependencies.gradle
