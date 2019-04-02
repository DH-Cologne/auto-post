package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@Transactional
@Log4j2
@Controller
public class PageController {


    @Autowired
    SessionService sessionService;

    @Autowired
    PageRepository pageRepository;

    @Autowired
    FacebookService facebookService;




    @GetMapping(value = "/schedule/{id}")
    public String postList(@PathVariable(value = "id") String id, Model model) {

        User user = sessionService.getActiveUser();

        model.addAttribute("page", pageRepository.findByFbId(id));

        if (user != null) {
            List<Post> posts = pageRepository.findByFbId(id).getPosts();
            Collections.sort(posts);
            model.addAttribute("pageList", user.getPageList());
            model.addAttribute("postList", posts);
        } else {
            model.addAttribute("loginlink", facebookService.createFacebookAuthorizationURL());
            return "no-login";
        }

        return "page";
    }

}