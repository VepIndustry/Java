package View;

import Controller.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class View {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static Controller controller = new Controller();

    static public void main(String[] args) throws Exception {
        String interCommand;
        do {
            switch (interCommand = reader.readLine()) {
                case "go": //переделать parseOnExample и сделать более информативным логирование
                    controller.start();
                    break;
                case "add":
                    System.out.println("Ключ для входа: ");
                    String key = reader.readLine();
                    System.out.println("До какого уровня качать: ");
                    int lvl = Integer.parseInt(reader.readLine());
                    System.out.println("Укажите в процентах (от 0 до 100) скорость прокачки: ");
                    controller.addAccount(key, lvl, Byte.parseByte(reader.readLine()));
                    break;
                case "watch":
                    controller.printAll();
                    break;
                case "stop":
                    Model.Model.println("Введите секретный ключ");
                    controller.stopUp(reader.readLine());
                    break;

            }
        } while (!interCommand.equals("exit"));
        controller.interrupt();
    }
}
