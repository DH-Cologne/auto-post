package de.demmer.dennis.autopost.services.scheduling;


import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.user.Facebookuser;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.services.FacebookService;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.constraints.NotNull;
import java.util.TimerTask;

/**
 * The task that is put into the @{@link ScheduleService}
 *
 *
 */
@Log4j2
@ToString
@Getter
@Setter
@NoArgsConstructor
public class PostTask extends TimerTask {

    @NotNull
    private Facebookuser user;

    @NotNull
    private Facebookpost post;

    private FacebookpostRepository postRepository;
    private FacebookService facebookService;

    public PostTask(Facebookuser user, Facebookpost post, FacebookService facebookService, FacebookpostRepository postRepository) {
        this.user = user;
        this.post = post;
        this.facebookService = facebookService;
        this.postRepository = postRepository;
    }

    /**
     * Publishes @{@link Facebookpost} and update database
     */
    @Override
    public void run() {

        if (post != null && user != null && !post.isPosted() && post.isScheduled() && post.isEnabled()) {
            facebookService.post(user, post);

            Facebookpost posted = postRepository.findByIdAndFacebookuserId(post.getId(), user.getId());
            posted.setPosted(true);
            posted.setEnabled(false);
            posted.setScheduled(false);
            postRepository.save(posted);

        } else {
            log.info("Not valid to post: " + post);
        }

    }
}
