package Dmitro.ru.SimpleChatNewJava17.repository;

import Dmitro.ru.SimpleChatNewJava17.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
    Page<User> findAll(Pageable pageable);
}
