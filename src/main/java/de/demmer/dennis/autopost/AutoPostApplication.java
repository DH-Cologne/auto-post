package de.demmer.dennis.autopost;

import de.demmer.dennis.autopost.entities.user.User;
import de.demmer.dennis.autopost.entities.user.UserFactory;
import de.demmer.dennis.autopost.repositories.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Log4j2
@SpringBootApplication
public class AutoPostApplication implements CommandLineRunner{

    @Autowired
    UserRepository repository;

    @Autowired
    UserFactory userFactory;




    public static void main(String[] args){SpringApplication.run(AutoPostApplication.class, args);}


    @Override
    public void run(String... args) throws Exception {

        User user = userFactory.getUser("EAAeJZARgEZAQMBAN6ZAtLRUrJFrbUXNdYIQGv2wzznneENDnGirEaHIOYb2E7iofKZAYzqSEVzRtQY2fzjl9xSHWRrVjSqdHtuZCVQbL6YIWyXHT70gFBtMDnFAIVZCjdLiGMwMKRreZCqJG6y6e5MZAl17zYpFdyOYQ7PHZCMb4ERgZDZD");

        repository.save(user);
        log.info("All users  -> {}", repository.findAll());
    }
}


