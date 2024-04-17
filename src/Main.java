//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception {
        var res = calc("1 + 2 + 3");
        System.out.print(res);
    }

    public static String calc(String input) throws Exception {
        if (input.isBlank())
            throw new Exception("пустая строка");

        String[] buff = input.split(" ");
        if (buff.length < 3)
            throw new Exception("строка не является математической операцией");

        if (buff.length > 3)
            throw new Exception("формат математической операции не удовлетворяет заданию" +
                    " - два операнда и один оператор (+, -, /, *)");

        if (isDecimal(buff[0]) ^ isDecimal(buff[2]))
            throw new Exception("одновременное использование разных систем счисления");

        int firstNumber = ParseNumber(buff[0]);
        int secondNumber = ParseNumber(buff[2]);
        int res = switch (buff[1]) {
            case "+":
                yield firstNumber + secondNumber;
            case "-":
                yield firstNumber - secondNumber;
            case "*":
                yield firstNumber * secondNumber;
            case "/":
                yield firstNumber / secondNumber;
            default:
                throw new Exception("неверный оператор");
        };

        if (isDecimal(buff[0]) & isDecimal(buff[2]))
            return String.valueOf(res);
        else
            if (res < 1)
                throw new Exception("в римской системе нет отрицательных чисел");
            return decimalToRoman(res);
    }

    public static int ParseNumber(String input) throws Exception {
        int number;

        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            number = romanToDecimal(input);
        }

        if (number < 1 | number > 11)
            throw new Exception("допустимый диапозон чисел [1:10]");
        return number;
    }

    public static boolean isDecimal(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static int romanToDecimal(String input) throws Exception {
        int decimal = 0;
        int lastNumber = 0;
        for (int i = input.length() - 1; i >= 0; i--) {
            RomanNumeral numeral = RomanNumeral.valueOf(String.valueOf(input.charAt(i)));
            lastNumber = switch (numeral) {
                case I, V, X -> {
                    decimal = calculate(numeral.getNumber(), lastNumber, decimal);
                    yield numeral.getNumber();
                }
                default -> throw new Exception();
            };
        }

        return decimal;
    }

    public static String decimalToRoman(int input) {
        String[] hundreds = {"", "C"};
        String[] tens = { "", "X", "XX", "XXX", "XL", "L",
                "LX", "LXX", "LXXX", "XC" };
        String[] units = { "", "I", "II", "III", "IV", "V", "VI",
                "VII", "VIII", "IX", "X" };
        int numberOfHundreds = (input / 100) % 10;
        int numberOfTens = (input / 10) % 10;
        int numberOfUnits = input % 10;
        return hundreds[numberOfHundreds]
                + tens[numberOfTens] + units[numberOfUnits];
    }

    public static int calculate(int number, int lastNumber, int decimal) {
        if (number < lastNumber)
            return decimal - number;
        else
            return decimal + number;
    }
}

enum RomanNumeral {
    I(1), V(5), X(10), L(50), C(100);

    int decimal;

    RomanNumeral(int decimal){
        this.decimal = decimal;
    }

    public int getNumber() {
        return decimal;
    }
}