package de.demmer.dennis.autopost.services;

import de.demmer.dennis.autopost.entities.Facebookpost;
import de.demmer.dennis.autopost.entities.ImageFile;
import de.demmer.dennis.autopost.entities.PostDto;
import de.demmer.dennis.autopost.repositories.FacebookpageRepository;
import de.demmer.dennis.autopost.repositories.FacebookpostRepository;
import de.demmer.dennis.autopost.repositories.ImageRepository;
import de.demmer.dennis.autopost.services.image.ImageStorageException;
import de.demmer.dennis.autopost.services.image.ImageStorageService;
import de.demmer.dennis.autopost.services.userhandling.SessionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for @{@link Facebookpost}
 */
@Log4j2
@Service
@Transactional
public class PostUtilService {


    @Autowired
    SessionService sessionService;

    @Autowired
    FacebookpageRepository pageRepository;

    @Autowired
    FacebookpostRepository postRepository;

    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    ImageRepository imageRepository;


    /**
     * Updates the atributes of a @{@link Facebookpost} via a @{@link PostDto}
     *
     * @param post
     * @param postDto
     * @param pageFbId
     * @return
     */
    public Facebookpost updatePost(Facebookpost post, PostDto postDto, String pageFbId, List<MultipartFile> files) {
        if (postDto.getLongitude() != null && !postDto.getLongitude().isEmpty())
            post.setLongitude(Float.parseFloat(postDto.getLongitude()));
        if (postDto.getLongitude() != null && !postDto.getLatitude().isEmpty())
            post.setLatitude(Float.parseFloat(postDto.getLatitude()));


        post.setEnabled(postDto.isEnabled());
        post.setPosted(false);
        post.setError(false);

        post.setContent(postDto.getContent());
        post.setImg(postDto.getImg());
        post.setPageID(pageFbId);
        post.setFacebookuser(sessionService.getActiveUser());
        post.setDate(postDto.getDate());
        post.setTime(postDto.getTime());

        post.setFacebookpage(pageRepository.findByFbIdAndFacebookuser_Id(pageFbId,sessionService.getActiveUser().getId()));

        List<ImageFile> convertedImages = new ArrayList<>();
        for (MultipartFile file: files) {
            ImageFile image= null;
            if (file != null && file.getSize() > 0L){
                try {
                    image = imageStorageService.storeFile(file, sessionService.getActiveUser());
                    post.getImageFile().add(image);
                    convertedImages.add(image);
                } catch (ImageStorageException e) {
                    e.printStackTrace();
                }
            }
        }

        postRepository.save(post);

        for (ImageFile image: convertedImages) {
            if(image!=null){
                image.setFacebookpost(post);
                imageRepository.save(image);
            }
        }




        return post;
    }

}
