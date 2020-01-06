package me.buck.sample.logback;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 10000; i++) {
            logger.debug("hello hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello {} {}", "buck", i);
        }
    }
}
