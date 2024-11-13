import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // получение данных от пользователя и проверка их на ошибки
        String fio = getData(scanner, true);
        String date = getData(scanner, false);

        // создаем объект класса Analysis для анализа данных
        Analysis analysis = new Analysis();

        String initials = analysis.getInitials(fio); // инициалы
        String gender = analysis.getGender(fio); // пол
        String age = analysis.countAge(date); // возраст

        // выводим полученные данные
        System.out.println("\nИнициалы: " + initials);
        System.out.println("Пол: " + gender);
        System.out.println("Возраст: " + age);
    }


    // метод получения данных от пользователя
    private static String getData(Scanner scanner, boolean fio) {
        // запрашиваем данные, пока они не будут корректные
        while (true) {
            System.out.print("Введите " + (fio ? "ФИО" : "дату рождения в формате дд-мм-гггг") + ": ");
            String data = scanner.nextLine().trim().toLowerCase(); // читаем строку, убираем лишние пробелы и регистр

            // обработка фио
            if (fio) {
                // проверка фио на корректность
                String message = checkFIO(data);
                if (message == null) {
                    return data;
                } else {
                    System.out.println(message);
                }
            // обработка даты рождения
            } else {
                // находим пробелы вокруг . / - и заменяем их на .
                data = data.replaceAll("\\s*([./-])\\s*", ".");

                // проверка даты рождения на корректность
                String message = checkBirth(data);
                if (message == null) {
                    return data;
                } else {
                    System.out.println(message);
                }
            }
        }
    }


    // метод проверки корректности фио
    private static String checkFIO(String fio) {
        // регулярное выражение - шаблон фио
        // допустимы только русские буквы и любые пробельные символы в количестве от одного
        // дефис может быть только внутри двойной фамилии без пробелов
        // отчества может не быть
        String fioPattern = "^[а-яё]+(-[а-яё]+)?\\s[а-яё]+(\\s[а-яёА-ЯЁ]+)?$";

        // проверяем, что фио соответствует шаблону
        Pattern pattern = Pattern.compile(fioPattern); // компилируем регулярное выражение в объект класса Pattern
        Matcher matcher = pattern.matcher(fio); // создаем объект класса Matcher для проверки строки от пользователя на совпадение с pattern

        // проверяем соответствует ли вся строка шаблону
        if (!matcher.matches()) {
            // проверка на корректность дефиса
            if (fio.matches("^[а-яё]+\\s*-\\s*[а-яё]+.*$")) {
                return "Дефис внутри двойной фамилии должен быть без пробелов.";
            }

            // разбиваем фио на отдельные слова по любым пробельным символам ('\\s')
            String[] words = fio.split("\\s+");

            // проверка количества слов
            if (words.length < 2 || words.length > 3) {
                return "Введите три слова или два, если нет отчества.";
            }

            // проверка на русские буквы
            if (!fio.matches("^[-а-яё\\s]+$")) {
                return "Используйте только русские буквы.";
            }

            // несовпадение с шаблоном
            return "Некорректный ввод фио.";
        }

        // возвращаем null, если все проверки пройдены
        return null;
     }


    // метод проверки корректности даты рождения
    private static String checkBirth(String date) {
        // создаем шаблон даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // проверяем дату на ошибки
        try {
            // преобразовываем строку в дату в нужном формате
            LocalDate correctDate = LocalDate.parse(date, formatter);

            // метод LocalDate.parse() автоматически нормализует некоторые несуществующие даты, например, 31 февраля
            // сохраняем число, месяц, год исходной строки и сравниваем их с преобразованными
            String[] initialDate = date.split("\\.");
            int day = Integer.parseInt(initialDate[0]);
            int month = Integer.parseInt(initialDate[1]);
            int year = Integer.parseInt(initialDate[2]);

            // проверка дня и месяца
            if (day != correctDate.getDayOfMonth() || month != correctDate.getMonthValue() || year != correctDate.getYear()) {
                return "Такой даты не существует.";
            }

            // проверка года
            if (year > LocalDate.now().getYear()) {
                return "Год не может быть больше текущего";
            }

            // возвращаем null, если все проверки пройдены
            return null;
        }
        // обрабатываем ошибку формата
        catch (DateTimeParseException e) {
            return "Неверный формат даты.";
        }
    }
}