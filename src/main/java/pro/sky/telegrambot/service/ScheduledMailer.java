package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.models.NotificationTask;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduledMailer {

    @Autowired
    private NotificationTaskService notificationTaskService;

    @Autowired
    TelegramBot telegramBot;

    private Logger logger = LoggerFactory.getLogger(ScheduledMailer.class);

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendNotificationIfItIsTime() {
        logger.info("Пытаюсь создать коллекцию из уведомлений");
        List<NotificationTask> notificationTaskList = new ArrayList<>(notificationTaskService.findNotificationByTargetTime());
        logger.info("Создал коллекцию " + notificationTaskList.stream().map(NotificationTask::toString).collect(Collectors.joining(",")) );
        if (!notificationTaskList.isEmpty()) {
            for (NotificationTask notificationTask: notificationTaskList) {
                SendMessage message = new SendMessage(notificationTask.getChatid(), notificationTask.getMessage());
                SendResponse response = telegramBot.execute(message);
            }
        }
    }
}
