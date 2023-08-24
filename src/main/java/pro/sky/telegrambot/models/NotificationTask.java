package pro.sky.telegrambot.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificationtask")
public class NotificationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long chatid;
    private String message;
    private LocalDateTime datetime;

    public NotificationTask() {
    }

    public NotificationTask(Long id, long chatid, String message, LocalDateTime date) {
        this.id = id;
        this.chatid = chatid;
        this.message = message;
        this.datetime = date;
    }

    public NotificationTask(Long chatid, String message, LocalDateTime date) {
        this.chatid = chatid;
        this.message = message;
        this.datetime = date;
    }

    public Long getId() {
        return id;
    }

    public Long getChatid() {
        return chatid;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return datetime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setChatid(Long chatId) {
        this.chatid = chatId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.datetime = date;
    }
}
