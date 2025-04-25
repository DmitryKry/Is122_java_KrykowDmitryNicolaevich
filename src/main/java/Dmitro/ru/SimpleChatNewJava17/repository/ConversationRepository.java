package Dmitro.ru.SimpleChatNewJava17.repository;

import Dmitro.ru.SimpleChatNewJava17.model.Conversation;
import Dmitro.ru.SimpleChatNewJava17.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}
