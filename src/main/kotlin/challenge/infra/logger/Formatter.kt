package challenge.infra.logger

import java.text.MessageFormat
import java.util.*
import java.util.logging.Formatter
import java.util.logging.LogRecord

class Formatter : Formatter() {

    private val messageFormat = MessageFormat("[{0,date} {0,time}] {1} [{2}]: #{3} {4}\n")

    override fun format(record: LogRecord): String {
        var className = record.sourceClassName.substringAfterLast(".") + "." + record.sourceMethodName

        val limit = 60
        while (className.length < limit) {
            className += " "
        }
        if (className.length > limit) {
            className = className.substring(0, limit)
        }

        val arguments = arrayOfNulls<Any>(5)
        arguments[0] = Date(record.millis)
        arguments[1] = record.level
        arguments[2] = className
        arguments[3] = Thread.currentThread().id
        arguments[4] = record.message

        return messageFormat.format(arguments)
    }
}