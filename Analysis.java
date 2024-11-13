import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Analysis {
    // метод определения инициалов из фио
    public String getInitials(String fio) {
        // разбиваем фио на отдельные слова по любым пробельным символам ('\\s') в количестве от одного ('+')
        String[] words = fio.split("\\s+");

        // фамилия - первое слово в списке
        // разбиваем двойную фамилию слова по дефису
        String[] surname = words[0].split("-");
        // делаем первую букву заглавной в первом слове фамилии
        String surnameFull = surname[0].substring(0, 1).toUpperCase() + surname[0].substring(1);
        // добавляем второе слово фамилии с заглавной буквы, если оно есть
        if (surname.length > 1) {
            surnameFull += "-" + surname[1].substring(0, 1).toUpperCase() + surname[1].substring(1);
        }

        // имя - первая буква второго слова в списке
        String nameInitial = words[1].toUpperCase().substring(0, 1);

        // если отчество есть, выводим полные инициалы
        if (words.length > 2) {
            // отчество - первая буква третьего слова в списке
            String patronymicInitial = words[2].toUpperCase().substring(0, 1);
            return surnameFull + " " + nameInitial + "." + patronymicInitial + ".";
        }
        // если отчества нет, выводим сокращенные инициалы
        else {
            return surnameFull + " " + nameInitial + ".";
        }
    }


    // метод определения пола из фио
    public String getGender(String fio) {
        // разбиваем фио на отдельные слова по любым пробельным символам ('\\s') в количестве от одного ('+')
        String[] words = fio.split("\\s+");

        // если отчество есть, определяем пол
        if (words.length > 2) {
            String patronymic = words[2]; // отчество - третье слово в списке

            // если отчество заканчивается на "а", то пол женский
            if (patronymic.endsWith("а")) {
                return "Ж";
            }
            // если отчество заканчивается на "ч", то пол мужской
            else if (patronymic.endsWith("ч")) {
                return "М";
            }
            // в остальных случаях выводим сообщение
            else {
                return "НЕ УДАЛОСЬ ОПРЕДЕЛИТЬ";
            }
        }
        // если отчества нет, выводим сообщение
        else {
            return "НЕ УДАЛОСЬ ОПРЕДЕЛИТЬ";
        }
    }


    // метод расчета возраста
    public String countAge(String date) {
        // создаем шаблон даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        // преобразовываем строку в дату в нужном формате
        LocalDate birthDate = LocalDate.parse(date, formatter);

        // создаем текущую дату
        LocalDate currentDate = LocalDate.now();

        // вычисляем период между датой рождения и текущей датой
        Period period = Period.between(birthDate, currentDate);

        // получаем количество годов в периоде
        String age = String.valueOf(period.getYears());

        // если возраст больше 0 лет, выводим возраст в годах
        if (!age.equals("0")) {
            // определяем правильное окончание по последней цифре
            String years = switch (age.substring(age.length() - 1)) {
                case "1" -> "год";
                case "2", "3", "4" -> "года";
                default -> "лет";
            };

            return age + " " + years;
        }
        // если возраст равен 0 лет, получаем количество месяцев в периоде
        else {
            age = String.valueOf(period.getMonths());

            // если возраст больше 0 месяцев, выводим возраст в месяцах
            if (!age.equals("0")) {
                // определяем правильное окончание по последней цифре
                String months = switch (age.substring(age.length() - 1)) {
                    case "1" -> "месяц";
                    case "2", "3", "4" -> "месяца";
                    default -> "месяцев";
                };

                return age + " " + months;
            }
            // если возраст равен 0 месяцев, получаем количество дней в периоде
            else {
                age = String.valueOf(period.getDays());

                // не рассматриваем числа 11, 12, 13 или 14
                if (!(age.equals("11") || age.equals("12") || age.equals("13") || age.equals("14"))) {
                    // определяем правильное окончание по последней цифре
                    String days = switch (age.substring(age.length() - 1)) {
                        case "1" -> "день";
                        case "2", "3", "4" -> "дня";
                        default -> "дней";
                    };

                    return age + " " + days;
                }
                // отдельно обрабатываем окончание для чисел 11, 12, 13 или 14
                else {
                    return age + " дней";
                }
            }
        }
    }
}
