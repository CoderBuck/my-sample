package me.buck.sample.logback;

import android.app.Application;

import org.slf4j.LoggerFactory;

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
        LogcatAppender appender = new LogcatAppender();
        appender.setContext(lc);
        appender.setName("logcat");

        // We don't need a trailing new-line character in the pattern
        // because logcat automatically appends one for us.
        PatternLayoutEncoder pl = new PatternLayoutEncoder();
        pl.setContext(lc);
        pl.setPattern("%msg");
        pl.start();

        PatternLayoutEncoder tagPl = new PatternLayoutEncoder();
        tagPl.setContext(lc);
        tagPl.setPattern("%logger{20}");
        tagPl.start();

        appender.setTagEncoder(tagPl);
        appender.setEncoder(pl);
        appender.start();

        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setContext(lc);
        encoder1.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{0} - %msg%n");
        encoder1.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setContext(lc);
        fileAppender.setFile(getFilesDir().getAbsolutePath() + "/log/app.log");
        fileAppender.setEncoder(encoder1);
//        fileAppender.start();

        RollingFileAppender<ILoggingEvent> rollingFileAppender = new RollingFileAppender<ILoggingEvent>();
        rollingFileAppender.setAppend(true);
        rollingFileAppender.setContext(lc);
//        rollingFileAppender.setFile(getFilesDir().getAbsolutePath() + "/rolling-log/app.log");

        SizeAndTimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new SizeAndTimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setFileNamePattern(getFilesDir().getAbsolutePath() + "/app[%i]-%d.log");
        rollingPolicy.setMaxHistory(7);
        rollingPolicy.setMaxFileSize(FileSize.valueOf("500kb"));
        rollingPolicy.setTotalSizeCap(FileSize.valueOf("2mb"));
        rollingPolicy.setParent(rollingFileAppender);  // parent and context required!
        rollingPolicy.setContext(lc);
        rollingPolicy.start();
        rollingFileAppender.setRollingPolicy(rollingPolicy);

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern("%logger{35} - %msg%n");
        encoder.setContext(lc);
        encoder.start();

        rollingFileAppender.setEncoder(encoder);
        rollingFileAppender.start();


        Logger rootLogger = lc.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);
//        rootLogger.addAppender(fileAppender);
        rootLogger.addAppender(rollingFileAppender);

        StatusPrinter.print(lc);
    }
}
