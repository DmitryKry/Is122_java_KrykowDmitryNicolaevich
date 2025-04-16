package Dmitro.ru.SimpleChatNewJava17.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    private long firstID;
    private long secondID;
}
