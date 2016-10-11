package Controller;

import Account.Account;
import Example.Example;
import Exceptions.NoLogin;
import Exceptions.ProgressException;
import Exceptions.UPLVLException;
import Model.Model;
import Parser.Parser;

import java.util.Iterator;
import java.util.Set;

class ControllerProgress extends Thread {

    private static ControlWebSite controlWebSite = new ControlWebSite();
    private static ControllerFileSystem controllerFileSystem = new ControllerFileSystem();
    private static Parser parser = new Parser();
    private Account account;

    ControllerProgress(Account account) {
        this.account = account;
        this.start();
    }

    private long Random(long center) {
        return (long) (Math.random() * center + center / 2);
    }

    private long Random(long begin, long end) {
        return (long) (Math.random() * (end - begin) + begin);
    }

    private String getCurentTypeLesson(Account account) {
        Set<Example> setExamples = controllerFileSystem.getSetExamples(account.getLevel(), account.getLesson());
        return setExamples.size() == 0 ? "Lector" : "Example";
    }

    private long randomVEP(int speed, String Type) {
        long temp = 0;
        switch (Type) {
            case "Example":
                temp = Random((100 - speed) * 45000);
                break;
            case "Lector":
                temp = Random((100 - speed) * 70000);
                break;
            case "WrongExample":
                return Random((100 - speed) * 30000);
        }
        Model.printPositiveLog("TIME", "", new String[]{Long.toString(temp)});
        return temp;
    }

    private void gasShip() {
        controlWebSite.gas(account);
    }

    @Override
    public void run() {
        String ID;
        try {
            ID = controlWebSite.login(account);
        } catch (NoLogin noLogin) {
            return;
        }

        account.setSessionID(ID);

        String Type;
        int[] arr;

        try {
            arr = controlWebSite.getProgress(account.getSessionID());
        } catch (ProgressException PE) {
            Model.printNegativeLog(PE, account.getKeyInter(), new String[1]);
            return;
        }

        if ((account.getLevel() != arr[0]) || (account.getLesson() != arr[1])) {
            account.setProgress(arr[0], arr[1]);
        }
        account.CurrentExamples = controllerFileSystem.getSetExamples(arr[0], arr[1]);
        Type = getCurentTypeLesson(account);
        gasShip();


        boolean Broke = false;

        boolean wrongFlag = false;
        boolean correct;

        while (true) {
            long time_sleep = randomVEP(account.getProffecional(), Type);

            if (account.getLevel() > account.getMaxLvl()) {
                Model.printPositiveLog("PROGRESSDONE", account.getKeyInter(), new String[]{"level" + account.getLevel() + ".lesson" + account.getLesson()});
                account.setMaxLvl(-1);
                controllerFileSystem.save(account);
                break;
            }

            try {
                sleep(time_sleep);
            } catch (InterruptedException e) {
                return;
            }


            if (Type.equals("Lector")) {
                controllerFileSystem.save(account);
            } else if (Type.equals("Example")) {
                if (Random(0, 99) <= account.getProffecional()) {
                    Iterator<Example> iterator = account.CurrentExamples.iterator();
                    Example example = iterator.next();
                    account.CurrentExamples.remove(example);
                    Broke = true;
                    wrongFlag = false;

                    if (Random(0, 99) >= account.getProffecional()) { //отправляет заранее неверную задачу
                        wrongFlag = true;
                        Model.printPositiveLog("WRONG", account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLesson()) + " " + example.namePackage});
                        Broke = false;
                    }



                    if (controlWebSite.sendExample(account, example, wrongFlag)) {
                        Model.printPositiveLog("SUCCESSEXAMPLE", account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLesson()) + " " + example.namePackage});
                        gasShip();
                    } else {
                        example.Broke = Broke;
                        account.WrongExamples.add(example);
                        //Model.printNegativeLog(new WrongExampleException(), account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLevel())});
                    }
                } else {

                    Iterator<Example> iterator = account.CurrentExamples.iterator();
                    Example example = iterator.next();
                    Model.printPositiveLog("NOTSEND", account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLesson()) + " " + example.namePackage});
                    account.CurrentExamples.remove(example);
                    account.WrongExamples.add(example);
                    controllerFileSystem.save(account);
                }
                controllerFileSystem.save(account);
            } else if (Type.equals("WrongExample")) {
                Iterator<Example> iterator = account.WrongExamples.iterator();
                Example example = null;
                while (iterator.hasNext()) {
                    example = iterator.next();
                    if (!example.Broke) break;
                }

                if (example.Broke || example == null) {
                    Model.printNegativeLog(new UPLVLException(), account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLesson())});
                    return;
                }

                try {
                    sleep(randomVEP(account.getProffecional(), "WrongExample"));
                } catch (InterruptedException e) {
                    return;
                }
                wrongFlag = false;
                Broke = true;
                if (Random(0, 99) >= account.getProffecional()) { //отправляет заранее неверную задачу
                    wrongFlag = true;
                    Model.printPositiveLog("WRONG", account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLesson()) + " " + example.namePackage});
                    Broke = false;
                }
                account.WrongExamples.remove(example);
                if (controlWebSite.sendExample(account, example, wrongFlag)) {
                    Model.printPositiveLog("SUCCESSEXAMPLE", account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLesson()) + " " + example.namePackage});
                    gasShip();
                } else {
                    example.Broke = Broke;
                    account.WrongExamples.add(example);
                    //Model.printNegativeLog(new WrongExampleException(), account.getKeyInter(), new String[]{"level" + parser.cURL(account.getLevel()) + ",lesson" + parser.cURL(account.getLevel())});
                }
                controllerFileSystem.save(account);
            }
            if (account.CurrentExamples.size() == 0) {
                try {
                    controlWebSite.upLevel(account);
                    int[] array = controlWebSite.getProgress(account.getSessionID());
                    account.setProgress(array[0], array[1]);
                    account.CurrentExamples = controllerFileSystem.getSetExamples(array[0], array[1]);
                    Type = getCurentTypeLesson(account);
                    controllerFileSystem.save(account);
                } catch (UPLVLException up) {
                    Model.printPositiveLog("MISTAKE", account.getKeyInter(), new String[2]);
                    Type = "WrongExample";
                } catch (ProgressException pe) {
                    Model.printNegativeLog(pe, account.getKeyInter(), new String[1]);
                    return;
                }
            }
        }
    }
}

