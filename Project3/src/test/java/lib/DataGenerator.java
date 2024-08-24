package lib;

import java.text.SimpleDateFormat;

public class DataGenerator {
    // Объявление метода, который возвращает случайный email в формате строки.
    public static String getRandomEmail() {
        // Создание строки с текущей датой и временем в формате "yyyyMMddHHmmss".
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        // Возвращает строку email, состоящую из "learnqa", текущего времени и домена "@example.com".
        return "learnqa" + timestamp + "@example.com";
    }
}
