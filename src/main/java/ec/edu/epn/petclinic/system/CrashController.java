package ec.edu.epn.petclinic.system;

import ec.edu.epn.petclinic.exceptions.OupsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class CrashController {

    @GetMapping("/oups")
    public String triggerException() {
        throw new OupsException(
                "Expected: controller used to showcase what " + "happens when an exception is thrown");
    }
}
