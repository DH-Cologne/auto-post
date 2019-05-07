package de.demmer.dennis.autopost.controller;

import de.demmer.dennis.autopost.entities.Post;
import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.repositories.PageRepository;
import de.demmer.dennis.autopost.repositories.PostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import de.demmer.dennis.autopost.services.tsvimport.MalformedTsvException;
import de.demmer.dennis.autopost.services.tsvimport.TsvService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
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
    PostRepository postRepository;

    @Autowired
    FacebookService facebookService;

    @Autowired
    TsvService tsvService;




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

    @PostMapping(value="/schedule/{id}/tsvform/upload")
    public String uploadTSV(@PathVariable(value = "id") String id, @RequestParam("file") MultipartFile multiFile, Model model){

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

        File file = null;
        try {
            file = new File(multiFile.getOriginalFilename());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multiFile.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            List<Post> tsvPosts = tsvService.parseTSV(file, id);

            for (Post post : tsvPosts) {
                postRepository.save(post);
            }

        } catch (MalformedTsvException e) {
            e.printStackTrace();
            model.addAttribute("line",e.getRow());
            model.addAttribute("linecontent",e.getContent());
            model.addAttribute("error",true);
            return "tsvform";
        }


        return "redirect:/schedule/"+ id;
    }



    @GetMapping(value="/schedule/{id}/tsvform")
    public String tsvForm(@PathVariable(value = "id") String id, Model model){

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

        return "tsvform";
    }










}
