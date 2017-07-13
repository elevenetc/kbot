package su.levenetc.kbot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KbotApplication

fun main(args: Array<String>) {
    SpringApplication.run(KbotApplication::class.java, *args)
}
