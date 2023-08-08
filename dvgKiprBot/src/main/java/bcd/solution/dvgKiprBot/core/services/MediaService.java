package bcd.solution.dvgKiprBot.core.services;

import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
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
import java.io.InputStream;

@Service
public class MediaService {

    //TODO: absolute path is piece of shit has to be rewrote quickly!
    @SneakyThrows
    public InputFile getStartMessageMedia() {
        ClassPathResource test = new ClassPathResource("images/kiprstart.jpg");
        InputFile file = new InputFile();
        file.setMedia(test.getInputStream(), "kiprstart.jpg");
        return file;
    }

    @SneakyThrows
    public InputFile getMediaForActivity(Activity activity){
        InputFile file = new InputFile();
        file.setMedia((new ClassPathResource("images/00.jpg")).getInputStream(), "00.jpg");
        //TODO logic
        return file;
    }
    @SneakyThrows
    public InputFile getMediaForCustomTour(CustomTour tour){
        InputFile file = new InputFile();
        file.setMedia((new ClassPathResource("images/00.jpg")).getInputStream(), "IMG_7174.webp");
        //TODO logic
        return file;
    }
    public InputFile getMediaForResort(Resort resort){
        InputFile file = new InputFile();
        file.setMedia("https://picsum.photos/id/20/200/300");
        //TODO logic
        return file;
    }
    public InputFile getMediaForHotel(Hotel hotel){
        InputFile file = new InputFile();
        file.setMedia("https://picsum.photos/id/25/200/300");
        //TODO logic
        return file;
    }

    @SneakyThrows
    public InputMedia updateMediaForActivity(Activity activity){
        InputMedia file = new InputMediaPhoto();
        file.setMedia((new ClassPathResource("images/00.jpg")).getInputStream(), "00.jpg");
        //TODO logic
        return file;
    }
    @SneakyThrows
    public InputMedia updateMediaForCustomTour(CustomTour tour){
        InputMedia file = new InputMediaPhoto();
        file.setMedia((new ClassPathResource("images/00.jpg")).getInputStream(), "IMG_7174.webp");
        //TODO logic
        return file;
    }
    @SneakyThrows
    public InputMedia updateMediaForResort(Resort resort){
        InputMedia file = new InputMediaPhoto();
        file.setMedia((new ClassPathResource("images/00.jpg")).getInputStream(), "Beach.webp");

        //TODO logic
        return file;
    }
    @SneakyThrows
    public InputMedia updateMediaForHotel(Hotel hotel){
        InputMedia file = new InputMediaPhoto();
        file.setMedia((new ClassPathResource("images/00.jpg")).getInputStream(), "Double_-Twin_Rooms.webp");
        //file.setMedia("https://picsum.photos/id/25/200/300");
        //TODO logic
        return file;
    }
    @SneakyThrows
    public InputMedia updateMediaForStart(){
        InputMedia file = new InputMediaPhoto();
        file.setMedia((new ClassPathResource("images/kiprstart.jpg")).getInputStream(), "kiprstart.jpg");
        //TODO logic
        return file;
    }
}
