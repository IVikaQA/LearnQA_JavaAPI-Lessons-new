package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {
    // Объявление метода, который возвращает случайный email в формате строки.
    public static String getRandomEmail() {
        // Создание строки с текущей датой и временем в формате "yyyyMMddHHmmss".
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        // Возвращает строку email, состоящую из "learnqa", текущего времени и домена "@example.com".
        return "learnqa" + timestamp + "@example.com";
    }

    // Объявление метода, который возвращает карту с данными для регистрации.
    public static Map<String, String> getRegistrationData() {
        // Создание пустой карты для хранения данных регистрации.
        Map<String, String> data = new HashMap<>();
        // Добавление пары "email" и случайного email в карту.
        data.put("email", DataGenerator.getRandomEmail());
        // Добавление пары "password" и значения "123" в карту.
        data.put("password", "123");
        // Добавление пары "username" и значения "learnqa" в карту.
        data.put("username", "learnqa");
        // Добавление пары "firstName" и значения "learnqa" в карту.
        data.put("firstName", "learnqa");
        // Добавление пары "lastName" и значения "learnqa" в карту.
        data.put("lastName", "learnqa");

        // Возвращает карту с данными регистрации.
        return data;
    }

    // Объявление метода, который принимает карту с пользовательскими значениями и возвращает карту с данными для регистрации.
    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {

        // Вызов метода для получения карты со значениями по умолчанию.
        Map<String, String> defaultValues = DataGenerator.getRegistrationData();

        // Создание пустой карты для хранения итоговых данных пользователя.
        Map<String, String> userData = new HashMap<>();
        // Создание массива с ключами, которые будут использоваться для заполнения карты.
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        // Начало цикла, который проходит по всем ключам в массиве keys.
        for (String key : keys) {
            // Проверка, содержит ли карта nonDefaultValues ключ key.
            if (nonDefaultValues.containsKey(key)) {
                // Если ключ есть в nonDefaultValues, добавляем его значение в карту userData.
                userData.put(key, nonDefaultValues.get(key));
            } else {
                // Если ключа нет в nonDefaultValues, добавляем значение по умолчанию из defaultValues.
                userData.put(key, defaultValues.get(key));
            }
        }

        // Возвращает заполненную карту с пользовательскими данными или значениями по умолчанию.
        return userData;
    }
}
