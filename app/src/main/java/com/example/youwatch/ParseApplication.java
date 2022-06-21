package com.example.youwatch;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("EFj9sAi7ElThEM8TSp0rNuldsGSRhl1oixjpHDBh")
                .clientKey("eCx8ranBw0MIkTqaOnD9G68Q9YorbLv1rQUanaaV")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
