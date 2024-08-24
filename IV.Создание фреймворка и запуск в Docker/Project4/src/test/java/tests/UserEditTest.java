package tests;


import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/*
    В этом классе методы для редактирования существующих пользователей
    Замечание: Нельзя редактировать пользователей с id < 10
    */
public class UserEditTest extends BaseTestCase {
    /*
    В этом тесте будем:
    1)Создавать пользоватееля
    2)Редактировать этого пользователя и проверить, что успешно отредактировали
    3)Проверять,что успешно отредактировали
    4)Авторизоваться под созданным пользователем,чтобы получить его данные
     */
    @Test
    public void testEditJustCreated(){
        //1)GENERATE USER - Создание нового пользователя
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                .jsonPath();

        //Достаем из JSON-ответа значение поля id
        String userId = responseCreateAuth.getString("id");

        //2)LOGIN -Авторизация пользователя
        Map<String, String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //EDIT - Редактирование данных пользователя
        /*
         * Токен,авторизационый куки и значение,нна которое хотим поменять
         */
        String newName = "Changed Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseGetAuth,"auth_sid"))
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //GET - Получение данных пользователя и сравнение
        //Для этого мы делаем авторизованный запрос
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseGetAuth,"auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        //Смотрим ответ от сервера с новым значением поля - firstName
        //System.out.println(responseUserData.asString());
        /*
        Проверяем,что в JSON-ответе, поле firstName имеет значение = newName
         */
        Assertions.asserJsonByName(responseUserData,"firstName",newName);
    }
}
