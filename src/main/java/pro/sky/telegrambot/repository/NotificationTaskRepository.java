package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegrambot.models.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    //todo здесь ошибка, спрингу не нравится мой поиск


//    @Query(value = "select id, chatId, message, dateTime from notificationtask" +
//            "where dateTime = :#{#dateTime}", nativeQuery = true)
//List<NotificationTask> findByDateTime(@Param("dateTime") LocalDateTime dateTime);


    List<NotificationTask> findByDatetime(LocalDateTime dateTime);

}
