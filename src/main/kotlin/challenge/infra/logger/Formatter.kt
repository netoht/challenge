package challenge.infra.logger

import java.text.MessageFormat
import java.util.*
import java.util.logging.Formatter
import java.util.logging.LogRecord

class Formatter : Formatter() {

    private val messageFormat = MessageFormat("[{0,date} {0,time}] {3} [{2}] {1}\t: [T:{4}] {5}\n")

    override fun format(record: LogRecord): String {
        val arguments = arrayOfNulls<Any>(6)
        arguments[0] = Date(record.millis)
        arguments[1] = record.sourceClassName.substringAfterLast(".")
        arguments[2] = record.sourceMethodName
        arguments[3] = record.level
        arguments[4] = Thread.currentThread().id
        arguments[5] = record.message
        return messageFormat.format(arguments)
    }
}