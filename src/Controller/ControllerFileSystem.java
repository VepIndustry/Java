package Controller;

import Account.Account;
import Constants.Constants;
import Example.Example;
import Parser.Parser;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ControllerFileSystem {

    boolean addLetter(Account newAccount) throws Exception {
        return Serialization(newAccount);
    }

    boolean deleteLetter(String nameAccounts) {
        File file = new File(Constants.DirectoryName + "\\" + nameAccounts + ".dat");
        if (file.exists()) {
            try {
                file.delete();
            } catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    void save(Account account) {
        deleteLetter(account.getKeyInter());
        Serialization(account);
    }

    public Set<Example> getSetExamples(int lvl, int lesson) {
        Parser parser = new Parser();
        Example TempExample;
        Set<Example> setExamples = new HashSet<>();
        // определяем объект для каталога
        File dir = new File(Constants.DirectoryName + "\\level" + parser.cURL(lvl) + "\\lesson" + parser.cURL(lesson));
        // если объект представляет каталог
        if (dir.isDirectory()) {
            // получаем все вложенные объекты в каталоге
            for (File item : dir.listFiles()) {
                if (item.isDirectory()) {
                    setExamples.add(new Example(lvl, lesson, item.getName()));
                }
            }
        }
        return setExamples;
    }

    Set<Account> getSetAccounts() throws Exception {
        Account TempLetter;
        Set<Account> setAccounts = new HashSet<>();
        // определяем объект для каталога
        File dir = new File(Constants.DirectoryName);
        // если объект представляет каталог
        if (dir.isDirectory()) {
            // получаем все вложенные объекты в каталоге
            for (File item : dir.listFiles()) {

                if (!item.isDirectory()) {
                    setAccounts.add(Desirialization(item.getAbsolutePath()));
                }
            }
        }
        return setAccounts;
    }

    private boolean Serialization(Account account) {
        try {
            FileOutputStream fileOutput = new FileOutputStream(Constants.DirectoryName + "\\" + account + ".dat");
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutput);
            outputStream.writeObject(account);
            fileOutput.close();
            outputStream.close();


            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Account Desirialization(String currentWay) throws Exception {
        FileInputStream fiStream = new FileInputStream(currentWay);
        ObjectInputStream objectStream = new ObjectInputStream(fiStream);
        Object object = objectStream.readObject();
        fiStream.close();
        objectStream.close();

        return (Account) object;



    }


    String getCode(int lvl, int lsn, String nameExample) throws Exception {
        Parser parser = new Parser();
        File dir = new File(Constants.DirectoryName + "\\level" + parser.cURL(lvl) + "\\lesson" + parser.cURL(lsn) + "\\" + nameExample);
        String result = "";
        if (dir.isDirectory()) {
            // получаем все вложенные объекты в каталоге
            for (File item : dir.listFiles()) {
                BufferedReader readerFromFile;

                readerFromFile = new BufferedReader(new FileReader(item.getAbsoluteFile()));

                String inputStr;
                boolean start = false;
                while (null != (inputStr = readerFromFile.readLine())) {
                    char[] array = inputStr.toCharArray();
                    for (char simbol : array) {
                        if (simbol == 'p') start = true;

                        //if ((int)simbol < 128)
                        if (start)
                            if (simbol == '"') {
                                result += "\\\"";
                            } else {
                                result += simbol;
                            }

                    }

                    result += "\\n";
                }
                return result;
            }
        } else
            return "";
        return "";
    }

    boolean Exsister(int lvl, int lsn, String nameExample) {
        Parser parser = new Parser();
        File dir = new File(Constants.DirectoryName + "\\level" + parser.cURL(lvl) + "\\lesson" + parser.cURL(lsn) + "\\" + nameExample);
        String result = "";
        if (dir.isDirectory()) {
            return true;
        } else return false;
    }
}
