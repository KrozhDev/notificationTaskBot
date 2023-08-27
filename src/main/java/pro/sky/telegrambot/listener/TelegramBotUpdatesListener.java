package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
