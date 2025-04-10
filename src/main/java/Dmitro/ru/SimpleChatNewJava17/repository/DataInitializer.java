package Dmitro.ru.SimpleChatNewJava17.repository;
import Dmitro.ru.SimpleChatNewJava17.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    CommandLineRunner initDatabase(UserRepository repository) {
        return args -> {

            List<User> userList = new ArrayList<>();
            if (repository.count() == 0) {
                User users = new User();
                users.setName("Данил");
                users.setLastName("Елхов");
                users.setDateOfBirth(LocalDate.of(2000, 6, 13));
                users.setGender("male");
                users.setCity("Навашино");
                users.setEmail("Danil227_@mail.ru");
                users.setPassword("2fsae22fd");
                repository.save(users);
            }
            /*
            User[] users = new User[30];
            for (int i = 0; i < 30; i++) {
                users[i] = new User();

                // Уникальные данные для каждого пользователя
                switch (i) {
                    case 0:
                        users[i].setName("Денис");
                        users[i].setLastName("Сидоров");
                        users[i].setDateOfBirth(LocalDate.of(1992, 3, 10));
                        users[i].setGender("male");
                        users[i].setCity("Москва");
                        users[i].setEmail("denis.sidorov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("denis123");
                        break;
                    case 1:
                        users[i].setName("Анна");
                        users[i].setLastName("Морозова");
                        users[i].setDateOfBirth(LocalDate.of(1994, 5, 23));
                        users[i].setGender("female");
                        users[i].setCity("Санкт-Петербург");
                        users[i].setEmail("anna.morozova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("anna456");
                        break;
                    case 2:
                        users[i].setName("Максим");
                        users[i].setLastName("Павлов");
                        users[i].setDateOfBirth(LocalDate.of(1988, 7, 16));
                        users[i].setGender("male");
                        users[i].setCity("Новосибирск");
                        users[i].setEmail("maxim.pavlov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("maxim789");
                        break;
                    case 3:
                        users[i].setName("Екатерина");
                        users[i].setLastName("Дмитриева");
                        users[i].setDateOfBirth(LocalDate.of(1993, 9, 5));
                        users[i].setGender("female");
                        users[i].setCity("Воронеж");
                        users[i].setEmail("ekaterina.dmitrieva" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("katya0123");
                        break;
                    case 4:
                        users[i].setName("Игорь");
                        users[i].setLastName("Алексеев");
                        users[i].setDateOfBirth(LocalDate.of(1990, 12, 8));
                        users[i].setGender("male");
                        users[i].setCity("Тула");
                        users[i].setEmail("igor.alekseev" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("igor2345");
                        break;
                    case 5:
                        users[i].setName("Елена");
                        users[i].setLastName("Коваленко");
                        users[i].setDateOfBirth(LocalDate.of(1995, 2, 15));
                        users[i].setGender("female");
                        users[i].setCity("Краснодар");
                        users[i].setEmail("elena.kovalenko" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("elena6789");
                        break;
                    case 6:
                        users[i].setName("Владимир");
                        users[i].setLastName("Лебедев");
                        users[i].setDateOfBirth(LocalDate.of(1989, 4, 12));
                        users[i].setGender("male");
                        users[i].setCity("Сочи");
                        users[i].setEmail("vladimir.lebedev" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("vova1234");
                        break;
                    case 7:
                        users[i].setName("Мария");
                        users[i].setLastName("Смирнова");
                        users[i].setDateOfBirth(LocalDate.of(1997, 6, 19));
                        users[i].setGender("female");
                        users[i].setCity("Челябинск");
                        users[i].setEmail("maria.smirnova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("masha555");
                        break;
                    case 8:
                        users[i].setName("Никита");
                        users[i].setLastName("Романов");
                        users[i].setDateOfBirth(LocalDate.of(1992, 8, 3));
                        users[i].setGender("male");
                        users[i].setCity("Казань");
                        users[i].setEmail("nikita.romanov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("nikita123");
                        break;
                    case 9:
                        users[i].setName("Алина");
                        users[i].setLastName("Соколова");
                        users[i].setDateOfBirth(LocalDate.of(1994, 10, 7));
                        users[i].setGender("female");
                        users[i].setCity("Уфа");
                        users[i].setEmail("alina.sokolova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("alina987");
                        break;
                    case 10:
                        users[i].setName("Дмитрий");
                        users[i].setLastName("Николаев");
                        users[i].setDateOfBirth(LocalDate.of(1991, 11, 21));
                        users[i].setGender("male");
                        users[i].setCity("Пермь");
                        users[i].setEmail("dmitry.nikolaev" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("dmitry121");
                        break;
                    case 11:
                        users[i].setName("Андрей");
                        users[i].setLastName("Фролов");
                        users[i].setDateOfBirth(LocalDate.of(1990, 1, 16));
                        users[i].setGender("male");
                        users[i].setCity("Владивосток");
                        users[i].setEmail("andrey.frolov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("andrey1234");
                        break;
                    case 12:
                        users[i].setName("Татьяна");
                        users[i].setLastName("Громова");
                        users[i].setDateOfBirth(LocalDate.of(1993, 4, 25));
                        users[i].setGender("female");
                        users[i].setCity("Барнаул");
                        users[i].setEmail("tatiana.gromova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("tanya12345");
                        break;
                    case 13:
                        users[i].setName("Сергей");
                        users[i].setLastName("Михайлов");
                        users[i].setDateOfBirth(LocalDate.of(1987, 2, 20));
                        users[i].setGender("male");
                        users[i].setCity("Красноярск");
                        users[i].setEmail("sergey.mikhaylov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("sergey2345");
                        break;
                    case 14:
                        users[i].setName("Полина");
                        users[i].setLastName("Козлова");
                        users[i].setDateOfBirth(LocalDate.of(1995, 12, 11));
                        users[i].setGender("female");
                        users[i].setCity("Ростов-на-Дону");
                        users[i].setEmail("polina.kozlova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("polina678");
                        break;
                    case 15:
                        users[i].setName("Роман");
                        users[i].setLastName("Васильев");
                        users[i].setDateOfBirth(LocalDate.of(1992, 3, 3));
                        users[i].setGender("male");
                        users[i].setCity("Мурманск");
                        users[i].setEmail("roman.vasilyev" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("roman12345");
                        break;
                    case 16:
                        users[i].setName("Ксения");
                        users[i].setLastName("Чернова");
                        users[i].setDateOfBirth(LocalDate.of(1996, 6, 14));
                        users[i].setGender("female");
                        users[i].setCity("Владимир");
                        users[i].setEmail("ksenia.chernova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("ksenia6789");
                        break;
                    case 17:
                        users[i].setName("Юрий");
                        users[i].setLastName("Крылов");
                        users[i].setDateOfBirth(LocalDate.of(1994, 10, 30));
                        users[i].setGender("male");
                        users[i].setCity("Арзамас");
                        users[i].setEmail("yuri.krylov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("yuri12345");
                        break;
                    case 18:
                        users[i].setName("Алина");
                        users[i].setLastName("Жукова");
                        users[i].setDateOfBirth(LocalDate.of(1995, 1, 22));
                        users[i].setGender("female");
                        users[i].setCity("Ставрополь");
                        users[i].setEmail("alina.zhukova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("zhukova123");
                        break;
                    case 19:
                        users[i].setName("Константин");
                        users[i].setLastName("Давыдов");
                        users[i].setDateOfBirth(LocalDate.of(1992, 5, 27));
                        users[i].setGender("male");
                        users[i].setCity("Тамбов");
                        users[i].setEmail("constantin.davydov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("constantin123");
                        break;
                    case 20:
                        users[i].setName("Наталья");
                        users[i].setLastName("Савельева");
                        users[i].setDateOfBirth(LocalDate.of(1993, 9, 11));
                        users[i].setGender("female");
                        users[i].setCity("Нижний Новгород");
                        users[i].setEmail("natalia.saveleva" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("natalia567");
                        break;
                    case 21:
                        users[i].setName("Александр");
                        users[i].setLastName("Тимофеев");
                        users[i].setDateOfBirth(LocalDate.of(1994, 12, 6));
                        users[i].setGender("male");
                        users[i].setCity("Ульяновск");
                        users[i].setEmail("alexander.timofeev" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("alex12345");
                        break;
                    case 22:
                        users[i].setName("Маргарита");
                        users[i].setLastName("Федорова");
                        users[i].setDateOfBirth(LocalDate.of(1991, 3, 18));
                        users[i].setGender("female");
                        users[i].setCity("Псков");
                        users[i].setEmail("margarita.fedorova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("margarita123");
                        break;
                    case 23:
                        users[i].setName("Виталий");
                        users[i].setLastName("Сухов");
                        users[i].setDateOfBirth(LocalDate.of(1990, 2, 17));
                        users[i].setGender("male");
                        users[i].setCity("Набережные Челны");
                        users[i].setEmail("vitaly.sukhov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("vitaliy345");
                        break;
                    case 24:
                        users[i].setName("Даниил");
                        users[i].setLastName("Шевченко");
                        users[i].setDateOfBirth(LocalDate.of(1992, 8, 22));
                        users[i].setGender("male");
                        users[i].setCity("Чита");
                        users[i].setEmail("daniel.shevchenko" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("daniel54321");
                        break;
                    case 25:
                        users[i].setName("Евгения");
                        users[i].setLastName("Рогова");
                        users[i].setDateOfBirth(LocalDate.of(1993, 11, 17));
                        users[i].setGender("female");
                        users[i].setCity("Рязань");
                        users[i].setEmail("evgeniya.rogova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("evgeniya2345");
                        break;
                    case 26:
                        users[i].setName("Илья");
                        users[i].setLastName("Шарков");
                        users[i].setDateOfBirth(LocalDate.of(1994, 7, 25));
                        users[i].setGender("male");
                        users[i].setCity("Томск");
                        users[i].setEmail("ilya.sharkov" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("ilya7890");
                        break;
                    case 27:
                        users[i].setName("Валентина");
                        users[i].setLastName("Яковлева");
                        users[i].setDateOfBirth(LocalDate.of(1991, 4, 9));
                        users[i].setGender("female");
                        users[i].setCity("Магнитогорск");
                        users[i].setEmail("valentina.yakovleva" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("valentina654");
                        break;
                    case 28:
                        users[i].setName("Дмитрий");
                        users[i].setLastName("Королёв");
                        users[i].setDateOfBirth(LocalDate.of(1995, 10, 13));
                        users[i].setGender("male");
                        users[i].setCity("Калуга");
                        users[i].setEmail("dmitry.korolev" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("dmitry98765");
                        break;
                    case 29:
                        users[i].setName("Марина");
                        users[i].setLastName("Тарасова");
                        users[i].setDateOfBirth(LocalDate.of(1992, 9, 17));
                        users[i].setGender("female");
                        users[i].setCity("Омск");
                        users[i].setEmail("marina.tarasova" + System.currentTimeMillis() + "@mail.ru");
                        users[i].setPassword("marina654");
                        break;
                }

                // Сохраняем каждого пользователя в репозитории
                repository.save(users[i]);
            }*/
        };
    }
}
