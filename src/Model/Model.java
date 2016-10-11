package Model;

import Exceptions.*;

import java.io.FileNotFoundException;

public class Model {
    public static synchronized void printNegativeLog(Exception exception, String name, String[] args) {
        if (exception instanceof NoJSESSION) {
            System.out.println("FATAL: " + name + " не получил JSESSION.");
        } else if (exception instanceof NoLogin) {
            System.out.println("FATAL: " + name + " не выполнил вход.");
        } else if (exception instanceof FileNotFoundException) {
            System.out.println("ERROR: " + name + " не нашел файл для отправки задачи: " + args[0] + ".");
        } else if (exception instanceof WrongExampleException) {
            System.out.println("ERROR: " + name + " не прошел проверку задачи: " + args[0] + ".");
        } else if (exception instanceof ProgressException) {
            System.out.println("FATAL: " + name + ", не удалось получить уровень.");
        } else if (exception instanceof UPLVLException) {
            System.out.println("FATAL: " + name + ", не удалось повысить уровень, сейчас уровень " + args[0] + ".");
        }
    }

    public static synchronized void printPositiveLog(String Type, String name, String[] args) {
        switch (Type) {
            case "UPLVL":
                System.out.println("OK: " + name + " повысил уровень, теперь " + args[0] + ".");
                break;
            case "PROGRESSDONE":
                System.out.println("PERFECT: " + name + " прокачка закончена. Уровень " + args[0] + ".");
                break;
            case "SUCCESSEXAMPLE":
                System.out.println("GOOD: " + name + ", задача прошла проверку: " + args[0] + ".");
                break;
            case "LOGIN":
                System.out.println("OK: " + name + " вошел на сайт.");
                break;
            case "CREATE":
                System.out.println("OK: " + name + " создан.");
                break;
            case "WRONG":
                System.out.println("OK: " + name + ", решил ошибиться: " + args[0] + ".");
                break;
            case "NOTSEND":
                System.out.println("OK: " + name + ", решил не отправлять: " + args[0] + ".");
                break;
            case "MISTAKE":
                System.out.println("OK: " + name + ", решил исправить ошибки.");
                break;
            case "TIME":
                System.out.println("TIME: ждем " + Long.parseLong(args[0]) / 60000 + " минут(ы)");
        }
    }

    public static synchronized void println(String str){
        System.out.println(str);
    }
}
