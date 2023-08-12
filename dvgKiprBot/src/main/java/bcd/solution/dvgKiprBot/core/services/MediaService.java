package bcd.solution.dvgKiprBot.core.services;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

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
    private List<InputMedia> getMediasByPath(String path) {
        Resource[] resources = this.resourcePatternResolver.getResources("classpath:" + path + "*");
        return Arrays.stream(resources).map(resource ->
        {
            InputMedia media = new InputMediaPhoto();
            try {
                media.setMedia(resource.getInputStream(), resource.getFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return media;
        }).toList();
    }


    @SneakyThrows
    private InputMedia getMediaByPath(String path, String name) {
        InputMedia media = new InputMediaPhoto();
        media.setMedia(new ClassPathResource(path).getInputStream(), name);
        return media;
    }

    @SneakyThrows
    public List<InputMedia> getHotelMedias(Hotel hotel) {
        return getMediasByPath(hotel.media);
    }

    @SneakyThrows
    public InputMedia getActivityMedia(Activity activity) {
        String fileName = activity.media.split("/")[2];
        return getMediaByPath(activity.media, fileName);
    }

    @SneakyThrows
    public InputMedia getHotelMedia(Hotel hotel) {
        String fileName = hotel.media.split("/")[4];
        return getMediaByPath(hotel.media, fileName);
    }

    @SneakyThrows
    public InputMedia getResortMedia(Resort resort) {
        String fileName = resort.media.split("/")[2];
        return getMediaByPath(resort.media, fileName);
    }

    @SneakyThrows
    public InputMedia getCustomTourMedia(CustomTour customTour) {
        if (customTour.media == null) {
            return updateMediaForStart();
        }
        String fileName = customTour.media.split("/")[2];
        return getMediaByPath(customTour.media, fileName);
    }

    @SneakyThrows
    public InputMedia getFeedbackMedia() {
        String fileName = "finish.jpg";
        return getMediaByPath("images/hotelType_2.png", fileName);
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
