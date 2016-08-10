import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%level %logger - %msg%n"
    }
}

appender('FILE_APPENDER', RollingFileAppender) {
    file = "E:/My_Work/SpringToken/logs/testFile.log"
    append = true
    rollingPolicy(SizeAndTimeBasedRollingPolicy) {
        fileNamePattern = "E:/My_Work/SpringToken/logs/testFile-%d{yyyy-MM-dd}.%i.log"
        maxHistory = 30
        maxFileSize = "5MB"
    }
        encoder(PatternLayoutEncoder) {
        pattern = "%C -> %level %logger - %msg%n"
    }
}

root(ERROR, ['STDOUT'])
/*logger("org.springframework.jms.core", DEBUG, ['STDOUT'], false)
logger("org.springframework.jms.support", DEBUG, ['STDOUT'], false)
logger("org.springframework.jms.annotation", DEBUG, ['STDOUT'], false)
logger("org.springframework.jms.config", DEBUG, ['STDOUT'], false)
logger("org.springframework.jms.support", DEBUG, ['STDOUT'], false)
logger("org.springframework.jms.support", DEBUG, ['STDOUT'], false)*/

logger('org.springframework.security', DEBUG, ['STDOUT','FILE_APPENDER'], false)
logger("com.subha", DEBUG, ['STDOUT','FILE_APPENDER'], false)
