package Dmitro.ru.SimpleChatNewJava17.model;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "conversations")
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String message;
    private String nameOfConversation;
    private long IDOwner;
    private boolean adminIsOwner;
}
