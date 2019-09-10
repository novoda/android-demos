package dependencies

final class Libraries {

    final AndroidX androidx = new AndroidX()
    final Test test = new Test()
    final Kotlin kotlin = new Kotlin()
    final Retrofit retrofit = new Retrofit()
    final Glide glide = new Glide()
    final OkHttp okHttp = new OkHttp()
    final Lifecycle lifecycle = new Lifecycle()
    final String moshi = 'com.squareup.moshi:moshi:1.8.0'
    final String rxJava = 'io.reactivex.rxjava2:rxjava:2.2.9'
    final String rxAndroid = 'io.reactivex.rxjava2:rxandroid:2.1.1'

    static final class OkHttp {
        private final String version = '3.14.2'

        final String client = "com.squareup.okhttp3:okhttp:${version}"
        final String loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${version}"
        final String mockWebServer = "com.squareup.okhttp3:mockwebserver:${version}"
    }


    static final class Glide {
        private final String version = '4.9.0'

        final String lib = "com.github.bumptech.glide:glide:${version}"
        final String compiler = "com.github.bumptech.glide:compiler:${version}"
    }

    static final class Retrofit {
        private final String version = '2.5.0'

        final String lib = "com.squareup.retrofit2:retrofit:${version}"
        final String rxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava2:${version}"
        final String moshiConverter = "com.squareup.retrofit2:converter-moshi:${version}"
    }

    static final class AndroidX {

        final String appCompat = "androidx.appcompat:appcompat:1.1.0"
        final String recyclerview = "androidx.recyclerview:recyclerview:1.0.0"
        final String constraintLayout = 'androidx.constraintlayout:constraintlayout:1.1.3'
    }

    static final class Lifecycle {
        private final String version = '2.1.0'

        final String extensions = "androidx.lifecycle:lifecycle-extensions:${version}"
        final String reactiveStreams = "androidx.lifecycle:lifecycle-reactivestreams:${version}"
    }

    static final class Test {

        private final String mockitoVersion = '2.28.2'

        final String junit = 'junit:junit:4.12'
        final String mockito = "org.mockito:mockito-core: ${mockitoVersion}"
        final String mockitoInline = "org.mockito:mockito-inline:${mockitoVersion}"
        final String mockitoKotlin = 'com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0'
        final String assertj = 'org.assertj:assertj-core:3.11.1'
    }

    static final class Kotlin {
        private final String version = '1.3.21'

        final String stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
        final String reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
    }

}
