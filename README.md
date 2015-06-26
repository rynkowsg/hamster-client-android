Hamster Client for Android [![Build Status](https://travis-ci.org/rynkowsg/hamster-client-android.svg?branch=master)](https://travis-ci.org/rynkowsg/hamster-client-android)
==========================

The project is an extension of desktop application named Hamster.
It provides the mobile application that extends a reach of the Hamster Time Tracker.

More information about Hamster you can find at [project's website][projecthamster]
or [GitHub page][projecthamster-github].



Libraries
---------

 * [ButterKnife][butterknife]
 * [Dagger 2][dagger2]
 * [Lombok][lombok]
 * [RxJava][rxjava]

Project provides communication with host by D-Bus. In order to do that I used:

 * [dbus-java][dbus-java] - D-Bus library for Java
     (the library was modificated for the project purpose)
 * libmatthew-java - a dependency of dbus-java ([link1][libmatthew-java-1],
     [link2][libmatthew-java-2], [link3][libmatthew-java-3], [link4][libmatthew-java-4]).



Thanks
------

At the project I have applied [The Clean Architeture][the-clean-architecture]
proposed by Robert C. Martin.
To do so I based on articles:

 * [The Clean Architeture][the-clean-architecture] by Robert C. Martin
 * [Architecting Android…The clean way?][the-clean-way] by Fernando Cejas
 * [Przejrzysty i testowalny kod na Androidzie? Spróbujmy z Clean Architecture][progmag-ca]
     by Michał Charmas

and two existed implementations of that approach:

 * [Android-CleanArchitecture][android-cleanarchitecture] by Fernando Cejas
 * [shoppinglist-clean-architecture-example][shoppinglist] by Michał Charmas



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
 [libmatthew-java-1]: http://www.java2s.com/Code/JarDownload/libmatthew/libmatthew-0.8-x86_64-sources.jar.zip
 [libmatthew-java-2]: https://speakerdeck.com/jakewharton/android-apps-with-dagger-devoxx-2013
 [libmatthew-java-3]: https://github.com/rynkowsg/libmatthew-java
 [libmatthew-java-4]: https://bitbucket.org/rynkowsg/libmatthew-java
 [the-clean-architecture]: http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html
 [the-clean-way]: http://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/
 [progmag-ca]: http://programistamag.pl/przejrzysty-i-testowalny-kod-na-androidzie-sprobujmy-z-clean-architecture/
 [android-cleanarchitecture]: https://github.com/android10/Android-CleanArchitecture
 [shoppinglist]: https://github.com/mcharmas/shoppinglist-clean-architecture-example
