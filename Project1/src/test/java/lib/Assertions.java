package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Assertions {

    /*
    Проверить, что код ответа сервера равен ожидаемому
    */
    public static void assertResponseCodeEquals(Response Response, int expectedStatusCode){
        assertEquals(
                expectedStatusCode,
                Response.statusCode(),
                "Response status code is not as expected"
        );
    }

    /*
    На вход метод получает объект с ответом сервера, имя которое нужно найти в ответе,ожидаемое значение
     */
    public static void asserJsonByName(Response Response,String name, int expectedValue){
        //1) Получаем объект с ответом сервера
        Response.then().assertThat().body("$",hasKey(name));
        //2)Получаем значение нужного параметра,передав его имя
        int value = Response.jsonPath().getInt(name);
        //3) Сравниваем ожидаемое значение со значением из ответа сервера,если не совпадает
        //то выводим сообщение
        assertEquals(expectedValue,value,"JSON value is not equal to expected value");
    }

    /*
  Проверить, что текст ответа сервера равен ожидаемому
   */
    public static void assertResponseTextEquals(Response Response, String expectedAnswer){
        assertEquals(
                expectedAnswer,
                Response.asString(),
                "Response text is not as expected"
        );
    }
}
