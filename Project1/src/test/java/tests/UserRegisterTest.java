package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {


    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";

        //Заполняем данными
        Map<String,String> userData = new HashMap<>();
        userData.put("email",email);
        userData.put("password","123");
        userData.put("username","learnqa");
        userData.put("firstName","learnqa");
        userData.put("lastName","learnqa");

        //Создаем пользователя
        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .andReturn();

        //Смотрим статус код ответа сервера
        //System.out.println(responseCreateAuth.statusCode());
        Assertions.assertResponseCodeEquals(responseCreateAuth,400);
        //Смотрим ответ сервера
        //System.out.println(responseCreateAuth.asString());
        Assertions.assertResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");

    }
}
