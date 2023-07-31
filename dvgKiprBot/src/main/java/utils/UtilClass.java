package utils;

import lombok.Getter;
import lombok.Setter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Setter
@Getter
@Service
public class UtilClass {

    @Autowired
    public UtilClass(UtilClass utilClass) {
        this.utilClass = utilClass;
    }

    private UtilClass utilClass;

    private static final String fill = "----------------------------------------------------------";
    private static final String easyToReadSeparator = fill + " \n \n \n" + fill;
    private static final String NastyaSaenkoChatId = "181304192";
    private static final String SashaUshakovaChatId = "1083415197";

    List<String> citizens;


    List<String> guests;

    public void fillCtitzens(List<String> citizens) {
        citizens.add(NastyaSaenkoChatId);
        citizens.add(SashaUshakovaChatId);
    }


    public void logMessage(Message message) {
        System.out.println(easyToReadSeparator + message.getFrom());
        System.out.println(easyToReadSeparator + message.getText() + easyToReadSeparator);
    }


}
