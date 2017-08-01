package su.levenetc.kbot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration

@SpringBootApplication
@EnableAutoConfiguration(exclude = arrayOf(
        DataSourceAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class
))
class KbotApplication

fun main(args: Array<String>) {
    SpringApplication.run(KbotApplication::class.java, *args)
}
