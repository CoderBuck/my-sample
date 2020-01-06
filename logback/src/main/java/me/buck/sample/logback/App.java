package me.buck.sample.logback;

import android.app.Application;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.FileSize;
import ch.qos.logback.core.util.StatusPrinter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLog();
    }

    private void initLog() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusManager sm = lc.getStatusManager();
        if (sm != null) {
            sm.add(new InfoStatus("Setting up default configuration.", lc));
        }


        PatternLayoutEncoder tag = new PatternLayoutEncoder();
        tag.setContext(lc);
        tag.setPattern("%logger{0}");
        tag.start();

        // We don't need a trailing new-line character in the pattern
        // because logcat automatically appends one for us.
        PatternLayoutEncoder msg = new PatternLayoutEncoder();
        msg.setContext(lc);
        msg.setPattern("%msg");
        msg.start();

        LogcatAppender appender = new LogcatAppender();
        appender.setContext(lc);
        appender.setName("logcat");
        appender.setTagEncoder(tag);
        appender.setEncoder(msg);
        appender.start();



        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{0} - %msg%n");
        encoder.start();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setFileNamePattern(getFilesDir().getAbsolutePath() + "/app[%i]-%d.log");
        rollingPolicy.setMaxHistory(7);
        rollingPolicy.setMaxFileSize(FileSize.valueOf("500kb"));
        rollingPolicy.setTotalSizeCap(FileSize.valueOf("2mb"));
        rollingPolicy.setParent(rollingFileAppender);  // parent and context required!
        rollingPolicy.setContext(lc);
        rollingPolicy.start();

        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(lc);
        rollingFileAppender.setRollingPolicy(rollingPolicy);
        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();

        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(lc);
        asyncAppender.setName("ASYNC");
        asyncAppender.addAppender(rollingFileAppender);
        asyncAppender.start();


        Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
        rootLogger.addAppender(rollingFileAppender);
//        rootLogger.addAppender(asyncAppender);

        StatusPrinter.print(lc);
    }


}
