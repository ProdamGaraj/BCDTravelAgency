package сore.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;

import java.io.File;

@Service
public class MediaService {
    public InputFile getMessageMedia() {
        InputFile file = new InputFile();
        file.setMedia("https://picsum.photos/id/237/200/300");
        return file;
    };
}
