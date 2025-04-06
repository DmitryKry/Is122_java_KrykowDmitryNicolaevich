package Dmitro.ru.SimpleChatNewJava17.repository;
import Dmitro.ru.SimpleChatNewJava17.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {
            int i = 0;
            User[] users = new User[5];
            if (repository.count() == 0) {
                users[i] = new User();
                users[i].setName("Danil");
                users[i].setLastName("Elhow");
                users[i].setDateOfBirth(LocalDate.of(2000, 6, 13));
                users[i].setGender("male");
                users[i].setCity("Navashino");
                users[i].setEmail("Danil227_" + System.currentTimeMillis() + "@mail.ru");
                users[i].setPassword("2fsae22fd");
                repository.save(users[i]);

                users[++i] = new User();
                users[i].setName("Dima");
                users[i].setLastName("Krykov");
                users[i].setDateOfBirth(LocalDate.of(2001, 3, 18));
                users[i].setGender("male");
                users[i].setCity("Navashino");
                users[i].setEmail("Diman777" + System.currentTimeMillis() + "@mail.ru");
                users[i].setPassword("d1dqad222");
                repository.save(users[i]);

                users[++i] = new User();
                users[i].setName("Dianna");
                users[i].setLastName("Kirova");
                users[i].setDateOfBirth(LocalDate.of(2003, 4, 11));
                users[i].setGender("woman");
                users[i].setCity("Navashino");
                users[i].setEmail("Dianna777" + System.currentTimeMillis() + "@mail.ru");
                users[i].setPassword("1dad2121");
                repository.save(users[i]);

                users[++i] = new User();
                users[i].setName("Nikita");
                users[i].setLastName("Borisow");
                users[i].setDateOfBirth(LocalDate.of(2001, 6, 10));
                users[i].setGender("male");
                users[i].setCity("Navashino");
                users[i].setEmail("Nikita777" + System.currentTimeMillis() + "@mail.ru");
                users[i].setPassword("23dfa22fdqa2");
                repository.save(users[i]);

                users[++i] = new User();
                users[i].setName("Nasty");
                users[i].setLastName("Maygkova");
                users[i].setDateOfBirth(LocalDate.of(2002, 2, 13));
                users[i].setGender("woman");
                users[i].setCity("Navashino");
                users[i].setEmail("Nasty277" + System.currentTimeMillis() + "@mail.ru");
                users[i].setPassword("1dq2daw2");
                repository.save(users[i]);
            }

        };
    }
}
