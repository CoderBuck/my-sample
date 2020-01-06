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

        long start = System.currentTimeMillis();
        logger.error("start");
        for (int i = 0; i < 10000; i++) {
            logger.debug("hello hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello {} {}", "buck", i);
        }
        logger.error("end");
        long end = System.currentTimeMillis();
        logger.error("end - start = {}", end-start);
    }
}
