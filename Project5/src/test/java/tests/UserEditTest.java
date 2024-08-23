package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/*
Тест на создание нового пользователя
 */
public class UserEditTest extends BaseTestCase {

    @Test
    public void testEditJustCraetedTest(){
        //1)Сначала будем создавать пользователя - GENERATE USER
        Map<String,String> userData = DataGenerator.getRegistrationData();

        JsonPath responseCreateAuth = RestAssured
                .given()
                .body(userData)
                .post("https://playground.learnqa.ru/api/user/")
                //Метод jsonPath() используется для извлечения ответа в формате JSON.
                .jsonPath();

        //Извлечение ID пользователя
        String userId = responseCreateAuth.getString("id");

        //LOGIN
        Map<String,String> authData = new HashMap<>();
        authData.put("email",userData.get("email"));
        authData.put("password",userData.get("password"));

        Response responseGetAuth = RestAssured
                .given()
                //Данные для авторизации берем из сгенерированных методом getRegistrationData()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        //Edit Name
        String newName = "changedName";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstname",newName);

        /*
        Здесь мы передаем авторизационый header и cookie
         */
        Response responseEditName = RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseGetAuth,"auth_sid"))
                //Передаем название параметра и новое значение в этот параметр
                .body(editData)
                .put("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();


        //GET
        /*
        Получение данных пользователя и сравнение его имени с новым
        Делаем авторизованнный get-запрос и убеждаемся что в ответе
        firstname содержит новое значение
         */
        Response responseUserData = RestAssured
                .given()
                .header("x-csrf-token",this.getHeader(responseGetAuth,"x-csrf-token"))
                .cookie("auth_sid",this.getCookie(responseGetAuth,"auth_sid"))
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();


    }
}
