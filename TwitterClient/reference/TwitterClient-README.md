# Project UI Tips

Simple guide for improving the UI for any application including links to tools:

* Pick a creative name for your application
* Select a pleasant launcher icon for your application (create a [launcher icon](http://imgur.com/a/8cmLM) and update in manifest)
* Use images, icons and backgrounds for your UIs leveraging resources like [IconFinder](https://www.iconfinder.com/), [NounProject](http://thenounproject.com/) and [Google Image Search](http://www.google.com/imghp) to locate assets.
* Consider adding progress bars whenever you can (or use the [built-in actionbar progress bar](https://github.com/thecodepath/android_guides/wiki/Handling-ProgressBars#actionbar-progress-bar))
* Customize the ActionBar Theme with [this style generator](http://jgilfelt.github.io/android-actionbarstylegenerator/), copy over the files, and apply the theme within the manifest.
* Customize the View control colors using the [Holo Colors Tool](http://android-holo-colors.com/)
* Customize the buttons using the [Button Style Generator](http://angrytools.com/android/button/)
* Review the [Login Screen Styling](https://github.com/thecodepath/android_guides/wiki/Cloning-a-Login-Screen-Layout) and [Complete Drawables](https://github.com/thecodepath/android_guides/wiki/Drawables) cliffnotes.



* **Did you improve the performance of the GridView?
** You may notice that the GridView **flickers** and appears choppy. To fix this we need to take a look at how to optimize the way we display our results. Applying [Performance Tips for Android’s ListView](http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/) and the [ViewHolder](http://developer.android.com/training/improving-layouts/smooth-scrolling.html#ViewHolder) pattern along with [a few other tricks](http://stackoverflow.com/a/1541530) can go a long way.
