package Controller;

import Account.Account;
import Example.Example;
import Exceptions.NoLogin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static Model.Model.printNegativeLog;
import static Model.Model.printPositiveLog;

public class Controller extends Thread {
    ControllerFileSystem controllerFileSystem = new ControllerFileSystem();
    ControlWebSite controlWebSite = new ControlWebSite();
    Map<String, Thread> setAccountsThread;

    Set<Account> setAccounts;

    public Controller() {
        setAccounts = getSetAccounts();
    }

    //Test Mod
    public static void main(String[] args) throws Exception {
        ControllerFileSystem controllerFileSystem = new ControllerFileSystem();
        ControlWebSite controlWebSite = new ControlWebSite();
        Controller controller = new Controller();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String interCommand;
        Account account = null;
        do {
            switch (interCommand = reader.readLine()) {
                case "addAccount":
                    System.out.println("Ключ для входа: ");
                    String key = reader.readLine();
                    account = controller.addAccount(key, 0, (byte) 100);
                    //account = new Account("veppev", 1, 2, 3, (byte)100);
                    if (account != null) {
                        controlWebSite.login(account);
                        controlWebSite.gas(account);
                    }
                    break;
                case "sendExample": //переделать parseOnExample
                    if (account != null) {
                        System.out.print("Level: ");
                        int lvl = Integer.parseInt(reader.readLine());
                        System.out.print("Lesson: ");
                        int lsn = Integer.parseInt(reader.readLine());
                        System.out.print("Name Example: ");
                        String nameEx = reader.readLine();
                        account.setProgress(lvl, lsn);
                        Example example = new Example(lvl, lsn, nameEx);
                        System.out.println(controller.controlWebSite.sendExample(account, example, false));
                    }
                    break;
                case "ser":
                    if (account != null) {
                        account.CurrentExamples.add(new Example(1, 2, "ll"));
                        account.CurrentExamples.add(new Example(2, 2, "ll"));
                        account.CurrentExamples.add(new Example(1, 3, "ll"));

                        account.WrongExamples.add(new Example(1, 2, "ll"));
                        account.WrongExamples.add(new Example(1, 3, "ll"));
                        account.WrongExamples.add(new Example(1, 4, "ll"));
                        controllerFileSystem.save(account);
                    }
            }
        } while (!interCommand.equals("exit"));
        controller.interrupt();
    }

    public void printAll() {
        for (Account acc : setAccounts) Model.Model.println(acc.print());
    }

    public boolean stopUp(String key) {

        if (setAccountsThread.containsKey(key)){
            Thread thread = setAccountsThread.get(key);
            thread.interrupt();
            try {
                thread.join();
            } catch (Exception e){
                return false;
            }
            setAccountsThread.remove(key);
return true;
        } else
            return false;
    }

    public Set<Account> getSetAccounts() {
        try {
            return controllerFileSystem.getSetAccounts();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    public Account addAccount(String key, int lvl, byte prof) throws Exception {
        Account account = new Account(key, 0, 0, lvl, prof);

        try {
            account.setSessionID(controlWebSite.login(account));

        } catch (NoLogin noLogin) {
            printNegativeLog(noLogin, key, new String[1]);
            return null;
        }

        int[] arrayPro = controlWebSite.getProgress(account.getSessionID());
        if (arrayPro != null) {
            controllerFileSystem.addLetter(new Account(key, arrayPro[0], arrayPro[1], lvl, prof));
            setAccounts.add(new Account(key, arrayPro[0], arrayPro[1], lvl, prof));
            printPositiveLog("CREATE", account.getKeyInter(), new String[1]);
            return account;
        } else {
            return null;
        }
    }

    public void run() {
        setAccountsThread = new HashMap<>();

        for (Account account : setAccounts) {
            if (account.getMaxLvl() != -1) {
                setAccountsThread.put(account.getKeyInter(), new ControllerProgress(account));
            }
        }
        while (true) {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                for (Thread thread : setAccountsThread.values()) {
                    thread.interrupt();
                    try {
                        thread.join();
                    } catch (InterruptedException r) {
                    }
                }
                break;
            }

        }
    }
}
