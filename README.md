# AddressMemoryCore
Java layer of abstraction for AddressMemory app.

Using a Java layer of abstraction for an Android app, I am going to update this repository while it will serve as a dependency for the application's project. 

Dependencies for this app
  * [Dagger2](https://google.github.io/dagger/), usefull for Dependency Injection
  * [Realm](https://realm.io/docs/java/latest/)
  * [RxJava 1.x](https://github.com/ReactiveX/RxJava), Realm is not fit yet for RxJava2
  * ~~[Mocking-Realm](https://github.com/juanmendez/Mocking-Realm/), for testing realm queries on JVM.~~ (Moved to the Android project)
  * [Databinding](https://developer.android.com/topic/libraries/data-binding/index.html) this app went from MVP into MVP{VM}. So it's easier to do level of abstraction.

Read [User Stories](https://github.com/juanmendez/MapMemoryCore/wiki/User-Stories)

I got inspiration doing TDD level of abstraction from this video

[![Alt text for your video](https://img.youtube.com/vi/RLo6hs1uDLA/0.jpg)](http://www.youtube.com/watch?v=RLo6hs1uDLA)
