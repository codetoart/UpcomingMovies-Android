# Upcoming Movie App
This is an assignment app which we ask to developers to complete before interview. New CodeToArt developers also study our coding style and MVP design pattern using this assignment.

####Libraries used
<ul><li>Support libraries</li>
<li>RecyclerView</li>
<li>CardView</li>
<li><a href="https://github.com/JakeWharton/butterknife">Butterknife</a></li>
<li><a href="https://github.com/ReactiveX/RxJava">RxJava</a> & <a href="https://github.com/ReactiveX/RxAndroid">RxAndroid</a></li>
<li><a href="http://google.github.io/dagger">Dagger2</a></li>
<li><a href="https://github.com/square/retrofit">Retrofit2</a></li>
<li><a href="https://github.com/nostra13/Android-Universal-Image-Loader">Universal Image Loader</a></li>
</ul>

#### Developed using MVP pattern

![alt tag](https://cloud.githubusercontent.com/assets/1277242/19315243/8c3929e2-90ba-11e6-86fe-e9f67fc7910c.png)

#### Quality check
<b>PMD</b> - PMD code analysis tool finds common programming flaws like unused variables, empty catch blocks, unnecessary object creation, and so forth.

``` 
./gradlew pmd
```
<b>findBugs</b> - findBugs is static code analysis to look for bugs in Java code. Unlike PMD, it analyses java byte code.

``` 
./gradlew findBugs
```
<b>checkStyle</b> - checkStyle is a development tool to help programmers write Java code that adheres to a coding standard. Checkout <a href="https://github.com/codetoart/upcomingmovies/blob/master/config/quality/checkstyle/checkstyle-config.xml">CheckStyle config file</a>

``` 
./gradlew checkStyle
```

