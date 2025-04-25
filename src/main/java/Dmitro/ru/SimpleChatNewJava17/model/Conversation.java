package Dmitro.ru.SimpleChatNewJava17.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long ID;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;
    String nameOfConversation;
    long IDOwner;
    boolean adminIsOwner;
}
