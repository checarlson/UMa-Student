package com.ldd.e_noticeboarduma;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("O7mE7YgW9KePT6XMtvbvSxf1ezRgdxLlUA7vlflV")
                // if defined
                .clientKey("oyfnNnopHAS9sOmkCc6gARzVHyXzCctOYu9mK5ba")
                .server("https://parseapi.back4app.com/")
                .enableLocalDataStore()
                .build()
        );
    }
}
