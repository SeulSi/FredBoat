package fredboat.event

import com.fredboat.sentinel.QueueNames
import com.fredboat.sentinel.entities.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
@RabbitListener(queues = [QueueNames.JDA_EVENTS_QUEUE])
class RabbitConsumer {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(RabbitConsumer::class.java)
    }
    private val shardStatuses = ConcurrentHashMap<Int, ShardStatus>()

    /* Shard lifecycle */

    @RabbitHandler
    fun receive(event: ShardStatusChange) {
        event.shard.apply {
            log.info("Shard [$id / $total] status ${shardStatuses.getOrDefault(id, "<new>")} => $status")
            shardStatuses[id] = status
        }
    }

    /* Guild events */

    @RabbitHandler
    fun receive(event: GuildJoinEvent) {
        log.info("Joined guild ${event.guildId}")
    }

    @RabbitHandler
    fun receive(event: GuildLeaveEvent) {
        log.info("Left guild ${event.guildId}")
    }

    /* Voice events */

    fun receive(event: VoiceJoinEvent) {
        // TODO
    }

    fun receive(event: VoiceLeaveEvent) {
        // TODO
    }

    fun receive(event: VoiceMoveEvent) {
        // TODO
    }

    /* Message events */

    fun receive(event: MessageReceivedEvent) {
        // TODO
    }

    fun receive(event: PrivateMessageReceivedEvent) {
        // TODO
    }

    @RabbitHandler(isDefault = true)
    fun default(msg: Any) {
        log.warn("Unhandled event $msg")
    }
}