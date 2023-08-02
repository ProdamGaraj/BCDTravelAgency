package сore.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import сore.models.Activity;
import сore.models.CustomTour;
import сore.models.Hotel;
import сore.models.Resort;
import сore.repository.*;

import java.io.File;

@Service
public class MediaService {

    public static MediaRepository mediaRepository ;
    public static ActivityRepository activityRepository ;
    public static ResortRepository resortRepository ;
    public static HotelRepository hotelRepository ;
    public static CustomToursRepository customToursRepository ;


    public InputFile getStartMessageMedia() {
        InputFile file = new InputFile();
        file.setMedia("https://picsum.photos/id/237/200/300");
        return file;
    };
    public InputFile getMediaForActivity(Activity activity){
        InputFile file = new InputFile();
        file.setMedia("https://picsum.photos/id/10/200/300");
        //TODO logic
        return file;
    }
    public InputFile getMediaForCustomTour(CustomTour tour){
        InputFile file = new InputFile();
        file.setMedia("https://picsum.photos/id/16/200/300");
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

    public InputMedia updateMediaForActivity(Activity activity){
        InputMedia file = new InputMediaPhoto();
        file.setMedia("https://picsum.photos/id/10/200/300");
        //TODO logic
        return file;
    }
    public InputMedia updateMediaForCustomTour(CustomTour tour){
        InputMedia file = new InputMediaPhoto();
        file.setMedia("https://picsum.photos/id/16/200/300");
        //TODO logic
        return file;
    }
    public InputMedia updateMediaForResort(Resort resort){
        InputMedia file = new InputMediaPhoto();
        file.setMedia("https://picsum.photos/id/20/200/300");
        //TODO logic
        return file;
    }
    public InputMedia updateMediaForHotel(Hotel hotel){
        InputMedia file = new InputMediaPhoto();
        file.setMedia("https://picsum.photos/id/25/200/300");
        //TODO logic
        return file;
    }
    public InputMedia updateMediaForStart(){
        InputMedia file = new InputMediaPhoto();
        file.setMedia("https://picsum.photos/id/237/200/300");
        //TODO logic
        return file;
    }
}
