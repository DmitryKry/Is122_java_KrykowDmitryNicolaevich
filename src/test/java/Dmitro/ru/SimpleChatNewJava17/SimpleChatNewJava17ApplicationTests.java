package Dmitro.ru.SimpleChatNewJava17;
import Dmitro.ru.SimpleChatNewJava17.controller.UserController;
import Dmitro.ru.SimpleChatNewJava17.model.Message;
import Dmitro.ru.SimpleChatNewJava17.model.User;
import Dmitro.ru.SimpleChatNewJava17.repository.MessageRepository;
import Dmitro.ru.SimpleChatNewJava17.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import jakarta.servlet.http.HttpSession;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class SimpleChatNewJava17ApplicationTests {

	@Mock
	private UserService userService;

	@Mock
	private Model model;

	@Mock
	private HttpSession session;

	@InjectMocks
	private UserController userController; // Замените на ваш контроллер

	// метод тестирует авторизацию
	@Test
	public void testEntranceByEmail_SuccessfulLogin() {
		// Arrange
		String email = "Danil227_1743351255984@mail.ru";
		String password = "2fsae22fd";
		User realUser = new User();
		realUser.setEmail(email);
		realUser.setPassword(password);

		when(userService.FindUserByEmailAndPassword(email, password)).thenReturn(realUser);

		// Act
		String viewName = userController.EntranceByEmail(email, password, model, session);

		// Assert
		assertEquals("entrance", viewName);

		verify(model, never()).addAttribute(eq("error"), anyString());
	}

	// метод тестирует регистрацию с неверным проверочным паролем
	@Test
	void registration_PasswordMismatch_ShouldReturnRegistrationView() {
		// Arrange
		User newUser = new User();
		newUser.setName("Nik");
		newUser.setGender("male");
		newUser.setCity("Navashino");
		newUser.setLastName("Bor");
		newUser.setAge(18);
		newUser.setEmail("new@example.com");
		newUser.setPassword("securePass");

		String testPassword = "differentPassword";

		// Act
		String result = userController.Registration(newUser, model, session, testPassword);

		// Assert
		assertEquals("registration", result);
		verify(model).addAttribute("error", "Пароли не сходяться!");
		verifyNoInteractions(session);
	}

	// метод тестирует полную регистрацию
	@Test
	void registration_NewUser_ShouldSaveAndReturnEntrance() {
		// Arrange
		User newUser = new User();
		newUser.setName("Nik");
		newUser.setGender("male");
		newUser.setCity("Navashino");
		newUser.setLastName("Bor");
		newUser.setAge(18);
		newUser.setEmail("new@examplee.com");
		newUser.setPassword("securePass");
		System.out.println("EMAIL in test: " + newUser.getEmail());
		// Пользователь НЕ найден — значит, он новый
		when(userService.FindUserByEmail("new@examplee.com")).thenReturn(null);

		// Act
		String result = userController.Registration(newUser, model, session, "securePass");

		// Assert
		assertEquals("entrance", result);
		verify(userService).SaveUser(newUser);
		verify(userService).FindUserByEmail("new@examplee.com");
		verify(session).setAttribute("user", newUser);
		verify(model).addAttribute("newUser", newUser);
	}

	// Тестируем проверку на сходие email
	@Test
	void registration_EmailAlreadyRegistered_ShouldReturnRegistrationWithError() {
		// Arrange
		User newUser = new User();
		newUser.setName("Nik");
		newUser.setGender("male");
		newUser.setCity("Navashino");
		newUser.setLastName("Bor");
		newUser.setAge(18);
		newUser.setEmail("Danil227_1743351255984@mail.ru");
		newUser.setPassword("securePass");

		when(userService.FindUserByEmail("Danil227_1743351255984@mail.ru")).thenReturn(new User());

		// Act
		String result = userController.Registration(newUser, model, session, "securePass");

		// Assert
		assertEquals("registration", result);
		verify(model).addAttribute("error", "Такой email уже зарегистрирован!");
		verify(userService, never()).SaveUser(any());
		verify(session, never()).setAttribute(eq("user"), any());
	}

	// протестировали поиск
	@Test
	void testFindUserByEmail_NoUsersFound_ShouldAddErrorAndAttributesToModel() {
		// Arrange
		String email = "test@examplee.com";
		int page = 0;
		int size = 12;
		boolean sort = false;
		long idOfUserActual = 5;

		// Создаем пустой список, так как в тесте мы имитируем ситуацию, когда пользователь не найден
		List<User> findOfUsers = new ArrayList<>();

		// Создаем пустую страницу пользователей (поскольку findOfUsers пуст)
		Page<User> usersPage = new PageImpl<>(new ArrayList<>());

		// Создаем актуального пользователя
		User actualUser = new User();
		actualUser.setId(5L);
		actualUser.setEmail("test@examplee.com");

		// Act
		String viewName = userController.FindUserByEmail(page, size, email, sort, idOfUserActual, model, session);

		// Assert
		assertEquals("users", viewName);  // Ожидаем, что вернется представление users// Проверяем размер списка для добавления в разговор (будет 0)
	}

	@Test
	void testInputMessages() {

		String message = "Hello world";
		long idOfUserActual = 552;
		User actualUser = new User();
		actualUser.setId(idOfUserActual);
		actualUser.setEmail("denis.sidorov1747678960410@mail.ru");
		int id = 553;
		Message newMessage = new Message();
		User companion = new User();
		companion.setId(id);
		companion.setEmail("anna.morozova1747678960441@mail.ru");
		newMessage.setFirstID(actualUser.getId());
		newMessage.setSecondID(companion.getId());
		newMessage.setMessage(actualUser.getId() + " " + message + '\n');


		String viewName = userController.InputMessages(message, id, idOfUserActual, model, session);
		assertEquals("PageOfUser", viewName);

	}
}
