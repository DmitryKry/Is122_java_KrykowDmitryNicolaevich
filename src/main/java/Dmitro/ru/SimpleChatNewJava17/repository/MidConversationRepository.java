package Dmitro.ru.SimpleChatNewJava17.repository;

import Dmitro.ru.SimpleChatNewJava17.model.Conversation;
import Dmitro.ru.SimpleChatNewJava17.model.MidConversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MidConversationRepository extends JpaRepository<MidConversation, Long> {
}
