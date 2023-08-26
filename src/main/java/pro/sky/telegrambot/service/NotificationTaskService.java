package pro.sky.telegrambot.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.listener.TelegramBotUpdatesListener;
import pro.sky.telegrambot.models.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.management.Notification;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class NotificationTaskService {

    private Logger logger = LoggerFactory.getLogger(NotificationTaskService.class);

    @Autowired
    NotificationTaskRepository notificationTaskRepository;

    public NotificationTask saveNotificationTask(LocalDateTime localDateTime, String task, long chatId) {
        NotificationTask notificationTask = new NotificationTask(chatId,task,localDateTime);
        notificationTaskRepository.save(notificationTask);
        return notificationTask;
    }


    public List<NotificationTask> findNotificationByTargetTime() {
        LocalDateTime nowTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        logger.info("Сервис уведомлений работает, ищу по дате " + nowTime.toString());
        return notificationTaskRepository.findByDatetime(nowTime);
    }


}
