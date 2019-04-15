package challenge.infra.logger

import java.util.logging.LogManager
import java.util.logging.Logger

class LoggerFactory {
    companion object {
        init {
            configure()
        }

        private fun configure() {
            LogManager.getLogManager().readConfiguration(
                LoggerFactory::class.java.getResourceAsStream("/logging.properties")
            )
        }

        fun getLogger(): Logger {
            return Logger.getLogger("challenge")
        }
    }
}