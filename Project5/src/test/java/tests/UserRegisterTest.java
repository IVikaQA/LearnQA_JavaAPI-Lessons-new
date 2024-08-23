package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {

    /*
    Тест-1:Проверить метод "Create user" для существующего пользователя
    Только здесь данные берутся из метода getRandomEmail(),getRegistrationData()
     */
    @Test
    public void testCreateUserWithExistingEmail2(){
        //String email = "vinkotov@example.com";
        //Генерим почту
        String email = DataGenerator.getRandomEmail();

        Map<String,String> userData = new HashMap<>();
        userData.put("email",email);
        //Все остальные данные для user берутся из метода getRegistrationData()
        userData = DataGenerator.getRegistrationData(userData);
    }

    /*
    Тест-2:Проверять метод "Create user" для нового пользователя
    Но здесь в отличии от первого варианта,почта генерится каждый раз новая и поэтому
    пользователь создается
     */
    @Test
    public void testCreateUserSuccesfully2(){
        //Генерим почту
        String email = DataGenerator.getRandomEmail();

        Map<String,String> userData = DataGenerator.getRegistrationData();

        Response responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        //Смотрим,что код ответа сервера - 200
        Assertions.assertResponseCodeEquals(responseCreateAuth,200);
        //Смотрим ответ сервера
        //System.out.println(responseCreateAuth.asString());
        /*
        Смотрим ответ сервера: Проверяем наличие аргумента
         */
        Assertions.assertJsonHasKey(responseCreateAuth,"id");
    }
}
