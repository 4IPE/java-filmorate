package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ExceedingDate;
import ru.yandex.practicum.filmorate.exception.ExceedingTheLimit;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
class FilmorateApplicationTests {
	private static UserController userController ;
	private  Validator validator;
	private static FilmController filmController ;

	@BeforeAll
	public static void beforeAll() {
		userController = new UserController();
		filmController = new FilmController();
	}

	@BeforeEach
	public void beforeEach(){
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	void userControllerTest() {
		User user2 = new User(0,"dan",LocalDate.of(2021,12,12));
		User user1 = new User(4,"dan",LocalDate.of(2021,12,12));
		User user3 = new User(1,"   ",LocalDate.of(2021,12,12));
		User user4 = new User(2,"dan",LocalDate.of(2025,12,12));

		user2.setEmail("@FDSGF");
		Set<ConstraintViolation<User>> violations = validator.validate(user2);
		assertFalse(violations.isEmpty());

		userController.addUser(user1);
		assertEquals(userController.allUser().get(user1.getId()).getName(),userController.allUser().get(user1.getId()).getLogin(),"Не выполняется условие login = name если name null");

		Throwable exp2 = assertThrows(ValidationException.class,()->userController.addUser(user3));
		assertEquals("Некоректный login",exp2.getMessage());

		Throwable exp3 = assertThrows(ValidationException.class,()->userController.addUser(user4));
		assertEquals("День рождения не может быть в будущем",exp3.getMessage());
	}

	@Test
	public void filmControllerTest(){
		Film film1 = new Film(0,"Описание",LocalDate.of(1894,12,12),12);

		film1.setName(null);
		Set<ConstraintViolation<Film>> violations = validator.validate(film1);
		assertFalse(violations.isEmpty());

		Film film2 = new Film(0,"“Забытый мир” - это захватывающий приключенческий фильм, действие которого разворачивается в постапокалиптическом мире. В центре сюжета - группа отважных героев, пытающихся разгадать тайну древнего пророчества и спасти остатки человечества от гибели.\n" +
				"\n" +
				"Главный герой, молодой воин по имени Дамиан, отправляется в опасное путешествие, чтобы найти мистический артефакт, способный вернуть равновесие в разрушенный мир. На своем пути он встречает множество препятствий и врагов, но также обретает верных друзей и союзников.\n" +
				"\n" +
				"Фильм наполнен зрелищными боями, неожиданными сюжетными поворотами и глубоким философским подтекстом. Зрители окажутся в мире, где каждый выбор героя имеет последствия и может изменить ход истории.\n" +
				"\n" +
				"“Забытый мир” подарит незабываемые эмоции всем любителям качественных фэнтези-фильмов и заставит задуматься о ценности человеческой жизни и важности принятия верных решений.",LocalDate.of(2021,12,12),12);
		Film film3 = new Film(0,"Описание",LocalDate.of(1894,12,12),12);
		Film film4 = new Film(0,"Описание",LocalDate.of(1896,12,12),-1);


		Throwable exp2 = assertThrows(ExceedingTheLimit.class,()->filmController.addFilm(film2));
		assertEquals(ExceedingTheLimit.class,exp2.getClass());

		Throwable exp1 = assertThrows(ExceedingDate.class,()->filmController.addFilm(film3));
		assertEquals(ExceedingDate.class,exp1.getClass());

		Throwable exp3 = assertThrows(IllegalArgumentException.class,()->filmController.addFilm(film4));
		assertEquals("Продолжительность должна быть положительной",exp3.getMessage());


	}


}
