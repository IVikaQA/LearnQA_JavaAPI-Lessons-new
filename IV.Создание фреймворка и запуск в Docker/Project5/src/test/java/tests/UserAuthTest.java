package tests;

import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import lib.Assertions;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

import lib.ApiCoreRequests;

/*
В тестах нам не нужно получать значение cookie и header
Здесь в тестах нам нужно просто убедиться что есть header и cookie
и равняется ожидаемому
Для этого создадим отдельный класс Assertions, в котором, например,
будет метод,который принимает на вход ответ от сервера,
получал бы ожидаемое значение,сам бы парсил ответ для получения
реального значения и сравнивал. Смотри класс
 */

//Тег @Epic говорит, что далее следующие тесты принадлежат большой единой части
@Epic("Authorization cases")
@Feature("Authorisation")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    /*
    Выносим в этот метод данные,которые нужно для прохождения тестов
    1.Заполняем переменные,которые будем передавать в запросе
    2.Выполнить запрос,который и вернет значения для переменных из пункта 1
    3.В переменные выше кладем значения, которые получили после выполнения пункта 2
    4.Нужно сделать проверки,что cookie,header с такими именами действмиельно ест.
    Смотри класс Assertions
     */
    @BeforeEach
    public void loginUser(){
        // 1) Создание карты для хранения данных авторизации пользователя (email и пароль).
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        /*
        2) Выполнение POST-запроса на авторизацию пользователя с использованием данных из authData.
        Ответ сохраняется в переменной responseGetAuth.
         */
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        /*
        Извлечение значений cookie, заголовка и идентификатора пользователя
        из ответа responseGetAuth и сохранение их в соответствующие переменные.
         */
        this.cookie = this.getCookie(responseGetAuth,"auth_sid");
        this.header = this.getHeader(responseGetAuth,"x-csrf-token");
        this.userIdOnAuth = this.getIntFromJson(responseGetAuth,"user_id");
    }

    /*
    Аннотации @Test, @Description и @DisplayName описывают тест,
    который проверяет успешную авторизацию пользователя.
     */
    @Test
    @Description("This test successfully authorize user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthUser(){
        /*
        1)Выполнение GET-запроса для проверки статуса авторизации пользователя
         с использованием ранее полученных cookie и заголовка.
         Ответ сохраняется в переменной responseCheckAuth.
         */
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/auth"
                        ,this.header
                        ,this.cookie);
        /*
        Используем статический метод класса Assertions
        Передав в метод asserJsonByName
        1.объект запроса - responseCheckAuth
        2.Имя параметра, которое ищем
        3.Ожидаемый результат
        Использование метода класса Assertions для проверки, что идентификатор пользователя
         в ответе совпадает с ожидаемым значением
         */
        Assertions.asserJsonByName(responseCheckAuth,"user_id",this.userIdOnAuth);
    }

    /*
    Аннотации описывают негативный тест, который проверяет статус авторизации
     без отправки cookie или заголовка. @ParameterizedTest позволяет запускать
     один и тот же тест с разными параметрами (в данном случае — "cookie" и "headers").
     */
    @Description("This test checks authorization status w/o sending auth cookie or token")
    @DisplayName("Test negattive auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie","header"})
    /*
    Этот метод, который принимает строковый параметр condition.
    Этот параметр определяет, как будет выполняться запрос для проверки аутентификации.
     */
    public void testNegativeAuthUser(String condition){
        /*
        Если condition равно "cookie", то выполняется метод makeGetRequestWithCookie,
        который делает GET-запрос к указанному URL с использованием cookie для аутентификации.
        Если condition равно "header", выполняется метод makeGetRequestWithToken,
        который делает аналогичный запрос, но с использованием заголовка для аутентификации.
         */
        if (condition.equals("cookie")) {
            /*
            Объявляется переменная responseForCheck, которая будет хранить
            ответ от API после выполнения запроса.
             */
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth"
                    ,this.cookie
            );
            Assertions.asserJsonByName(responseForCheck,"user_id",0);
        } else if (condition.equals("header")) {
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth"
                    ,this.header
            );
            /*
            Происходит проверка ответа с помощью метода Assertions.assertJsonByName. Этот метод проверяет, что значение поля "user_id" в JSON-ответе равно 0.
             */
            Assertions.asserJsonByName(responseForCheck,"user_id",0);
        } else {
            /*
            Если значение condition не соответствует ни одному из
            ожидаемых ("cookie" или "header"), выбрасывается исключение IllegalArgumentException
             с сообщением об ошибке.
             */
            throw new IllegalArgumentException("Condition value is not known: " + condition);
        }
    }
}