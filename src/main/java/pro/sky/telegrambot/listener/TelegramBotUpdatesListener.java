package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.models.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskService notificationTaskService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotificationIfItIsTime() {
        logger.info("Пытаюсь создать коллекцию из уведомлений");
        List<NotificationTask> notificationTaskList = new ArrayList<>(notificationTaskService.findNotificationByTargetTime());
        logger.info("Создал коллекцию " + notificationTaskList.stream().map(elem ->elem.toString()).collect(Collectors.joining(",")) );
        if (!notificationTaskList.isEmpty()) {
            for (NotificationTask notificationTask: notificationTaskList) {
                SendMessage message = new SendMessage(notificationTask.getChatid(), notificationTask.getMessage());
                SendResponse response = telegramBot.execute(message);
            }
        }
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);

            Long chatId = update.message().chat().id();
            String text = update.message().text();
            var userName = update.message().from().firstName();

            if (text.equals("/start")){
                SendMessage message = new SendMessage(chatId, "Привет " + userName + "!");
                SendResponse response = telegramBot.execute(message);
            }

            if (!text.equals("/start")){
                Pattern pattern = Pattern.compile(("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)"));
                Matcher matcher = pattern.matcher(text);
                if (matcher.matches()) {
                    String dateString = matcher.group(1);
                    String task = matcher.group(3);
                    LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    notificationTaskService.saveNotificationTask(date,task,chatId);
                    SendMessage message = new SendMessage(chatId, "Напомню о событии " + task + " в дату " + dateString);
                    SendResponse response = telegramBot.execute(message);
                }
            }
            // Process your updates here
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }



//    public void sendText(Long who, String what){
//        SendMessage sm = SendMessage.builder()
//                .chatId(who.toString()) //Who are we sending a message to
//                .text(what).build();//Message content
//        try {
//            execute(sm);                        //Actually sending the message
//        } catch (TelegramApiException e) {
//            throw new RuntimeException(e);      //Any error will be printed here
//        }
//    }



}
