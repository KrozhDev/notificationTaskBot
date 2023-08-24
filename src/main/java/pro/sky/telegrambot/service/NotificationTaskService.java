package pro.sky.telegrambot.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.models.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;

@Service
public class NotificationTaskService {

    @Autowired
    NotificationTaskRepository notificationTaskRepository;

    public NotificationTask saveNotificationTask(LocalDateTime localDateTime, String task, long chatId) {
        NotificationTask notificationTask = new NotificationTask(chatId,task,localDateTime);
        notificationTaskRepository.save(notificationTask);
        return notificationTask;
    }

}
