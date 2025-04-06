package Dmitro.ru.SimpleChatNewJava17.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

@Data
@Entity
@Table(name = "users")
public class User {
    public User(){}
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    private String gender;
    private String city;
    @Column(unique = true)
    private String email;
    private String password;
    @Transient
    private int age;

    public int getAge() {
        if (dateOfBirth == null) {
            return 0; // или другое значение по умолчанию
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}