package su.levenetc.kbot.controllers

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import su.levenetc.kbot.models.User


/**
 * Created by eugene.levenetc on 13/07/2017.
 */
@Controller
class MainController {

    @RequestMapping("/")
    @ResponseBody
    fun index(): String {
        return "Proudly handcrafted by " + "<a href='http://netgloo.com/en'>netgloo</a> :)"
    }

    @RequestMapping("/users")
    @ResponseBody
    fun user(): User {
        return User()
    }

}