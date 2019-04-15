package challenge.eda.event

class EmailNotificationCreated(
    val recipient: String,
    val subject: String,
    val body: String
)