package bcd.solution.dvgKiprBot.core.services;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import bcd.solution.dvgKiprBot.core.models.Activity;
import bcd.solution.dvgKiprBot.core.models.CustomTour;
import bcd.solution.dvgKiprBot.core.models.Hotel;
import bcd.solution.dvgKiprBot.core.models.Resort;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MediaService {
    private final ResourcePatternResolver resourcePatternResolver;

    public MediaService(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    //TODO: absolute path is piece of shit has to be rewrote quickly!
    @SneakyThrows
    public InputMedia updateMediaForStart(){
        InputMedia file = new InputMediaPhoto();
        file.setMedia((new ClassPathResource("images/kiprstart.jpg")).getInputStream(), "kiprstart.jpg");
        //TODO logic
        return file;
    }

    @SneakyThrows
    private InputFile getFileByPath(String path) {
        Resource[] resources = this.resourcePatternResolver.getResources("classpath:" + path + "*");
        Optional<Resource> resource = Arrays.stream(resources).filter(Resource::isFile).findFirst();
        if (resource.isEmpty()) {
            throw new RuntimeException();
        }
        return new InputFile(resource.get().getInputStream(), resource.get().getFilename());
    }

    @SneakyThrows
    private List<List<InputMedia>> getMediasByPath(String path) {
        if(!(new ClassPathResource(path).exists())) {
            throw new FileNotFoundException();
        }
        Resource[] resources = this.resourcePatternResolver.getResources("classpath:" + path + "big/*");
        List<List<InputMedia>> result = new ArrayList<>();
        
        List<InputMedia> tmpList = new ArrayList<>();
        for (Resource resource : resources) {
            if (tmpList.size() == 10) {
                result.add(tmpList);
                tmpList.clear();
            }
            InputMedia media = new InputMediaPhoto();
            try {
                media.setMedia(resource.getInputStream(), resource.getFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            tmpList.add(media);
        }
        if (!tmpList.isEmpty()) {
            result.add(tmpList);
        }

        return result;
    }


    @SneakyThrows
    private InputMedia getMediaByPath(String path) {
        Resource[] resources = this.resourcePatternResolver.getResources("classpath:" + path + "*");
        Optional<Resource> resource = Arrays.stream(resources).filter(Resource::isFile).findFirst();
        if (resource.isEmpty()) {
            throw new RuntimeException();
        }
        InputMedia media = new InputMediaPhoto();
        media.setMedia(resource.get().getInputStream(), resource.get().getFilename());
        return media;
    }

    @SneakyThrows
    public List<List<InputMedia>> getHotelMedias(Hotel hotel) {
        return getMediasByPath(hotel.media);
    }

    @SneakyThrows
    public List<List<InputMedia>> getCustomTourMedias(CustomTour customTour) {
        return getMediasByPath(customTour.media);
    }

    @SneakyThrows
    public List<List<InputMedia>> getResortMedias(Resort resort) {
        return getMediasByPath(resort.media);
    }

    @SneakyThrows
    public InputMedia getActivityMedia(Activity activity) {
        return getMediaByPath(activity.media);
    }

    @SneakyThrows
    public InputMedia getHotelMedia(Hotel hotel) {
        return getMediaByPath(hotel.media);
    }

    @SneakyThrows
    public InputMedia getResortMedia(Resort resort) {
        return getMediaByPath(resort.media);
    }

    @SneakyThrows
    public InputMedia getCustomTourMedia(CustomTour customTour) {
        if (customTour.media == null) {
            return updateMediaForStart();
        }
        return getMediaByPath(customTour.media);
    }

    @SneakyThrows
    public InputFile getHotelFile(Hotel hotel) {
        return getFileByPath(hotel.media);
    }

    @SneakyThrows
    public InputFile getResortFile(Resort resort) {
        return getFileByPath(resort.media);
    }

    @SneakyThrows
    public InputFile getCustomTourFile(CustomTour customTour) {
        if (customTour.media == null) {
            return getStartMessageMedia();
        }
        return getFileByPath(customTour.media);
    }

    @SneakyThrows
    public InputMedia getFeedbackMedia() {
        InputMedia file = new InputMediaPhoto();
        file.setMedia((new ClassPathResource("images/hotelType_2.png")).getInputStream(), "finish.png");
        return file;
    }

    @SneakyThrows
    public InputFile getStartMessageMedia() {
        InputFile file = new InputFile();
        file.setMedia(new ClassPathResource("images/kiprstart.jpg").getInputStream(), "kiprstart.jpg");
        return file;
    }

    @SneakyThrows
    public InputFile getTourChoosingMedia() {
        return getStartMessageMedia();
    }

    @SneakyThrows
    public InputFile getAuthMedia(){
        return getStartMessageMedia();
    }
}
