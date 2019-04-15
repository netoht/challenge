package challenge.eda.subscriber

import challenge.eda.Topics.EMAIL_NOTIFICATION_CREATED
import challenge.eda.event.EmailNotificationCreated
import challenge.infra.eda.Message
import challenge.infra.eda.Subscriber
import challenge.infra.logger.LoggerFactory

class EmailNotificationCreatedSubscriber : Subscriber {

    private val logger = LoggerFactory.getLogger()

    override fun subscribe(): List<String> {
        return listOf(EMAIL_NOTIFICATION_CREATED.name)
    }

    override fun receiveMessage(topic: String, message: Message) {
        val emailNotification = message.body
        if (emailNotification is EmailNotificationCreated) {
            logger.info("msg=email was successfully sent, recipient=${emailNotification.recipient}, subject=${emailNotification.subject} body=${emailNotification.body}")
        }
    }
}
