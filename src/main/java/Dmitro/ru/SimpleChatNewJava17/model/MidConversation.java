package Dmitro.ru.SimpleChatNewJava17.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "midconversations")
public class MidConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long IdOfUser;
    private long IdOfConversation;
}
