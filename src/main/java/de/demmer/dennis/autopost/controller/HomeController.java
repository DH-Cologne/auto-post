package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.entities.user.UserException;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.services.facebook.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.LoginService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controls the base page of the web app.
 * Paragraphs marked with 'DEV' can be uncommented to activate instant login of the admin for testing purposes
 */
@Controller
public class HomeController {

    @Autowired
    FacebookService facebookService;

    @Autowired
    SessionService sessionService;

    //----------DEV----------//
    @Autowired
    UserFactory userFactory;

    @Autowired
    LoginService loginService;

    @Value("${test.accessToken}")
    String devAccessToken;
    //----------DEV----------//

    @GetMapping(value = "/")
    public String home() {
        return "redirect:/home";
    }


    @GetMapping(value = "/home")
    public String home(Model model) {

        Facebookuser activeUser = sessionService.getActiveUser();

        if (activeUser != null) {
            model.addAttribute("pageList", activeUser.getPageList());
            model.addAttribute("pageName", "Choose Facebook page");
        }

        //----------DEV----------//
        else {
            try {
                activeUser = userFactory.getUser(devAccessToken);
            } catch (UserException e) {
                e.printStackTrace();
            }
            sessionService.addActiveUser(activeUser);
            loginService.updateUser(activeUser);
            model.addAttribute("pageList", activeUser.getPageList());
        }
        //----------DEV----------//


        model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());

        return "home";
    }


}
