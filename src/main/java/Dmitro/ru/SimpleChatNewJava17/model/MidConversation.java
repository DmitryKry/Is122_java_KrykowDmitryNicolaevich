package Dmitro.ru.SimpleChatNewJava17.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "midConversations")
public class MidConversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long ID;
    long IdOfUser;
    long IdOfConversation;
}
