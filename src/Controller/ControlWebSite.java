package Controller;

import Account.Account;
import Example.Example;
import Exceptions.*;
import Parser.Parser;

import static Constants.Constants.COOKIE_PART1;
import static Constants.Constants.COOKIE_PART2;
import static Model.Model.printNegativeLog;
import static Model.Model.printPositiveLog;


public class ControlWebSite {
    private Parser parser = new Parser();

    public String login(Account account) throws NoLogin {
        String Jses = "";

        try {
            String output = parser.getRequestForNewJSESSION();
            String result = Send.sendGet(output);
            Jses = parser.getJSession(result);
        } catch (NoJSESSION noJSESSION) {
            printNegativeLog(noJSESSION, account.getKeyInter(), new String[1]);
            throw new NoLogin();
        } catch (Exception e) {
            throw new NoLogin();
        }

        account.setJSES(Jses);

        String Cookie = COOKIE_PART1 + Jses + COOKIE_PART2;
        try {
            String data = parser.getDataForEnter(account.getKeyInter());
            String[] request = parser.getRequestForEnter(data.length(), Cookie);
            Send.sendPost(request, data);
        } catch (Exception e) {
            throw new NoLogin();
        }

        String output = parser.getRequestForCheck(Cookie);
        String result = Send.sendGet(output);
        printPositiveLog("LOGIN", account.getKeyInter(), new String[1]);
        return parser.getSessionID(result);
    }

    public void upLevel(Account account) throws UPLVLException {
        //запрос на повышение уровня
        String result = Send.GET("http://javarush.ru/api/rest/user/lessonComplete.json?sessionId=" + account.getSessionID());

        try {
            int[] res = getProgress(account.getSessionID());
            account.setProgress(res[0], res[1]);
        } catch (ProgressException PE) {
            //printNegativeLog(PE, account.getKeyInter(), new String[1]);
        }


        if (result.equals("false")) {
            UPLVLException exception = new UPLVLException();
            //printNegativeLog(exception, account.getKeyInter(), new String[]{"level" + account.getLevel() + ", lesson" + account.getLesson()});
            throw exception;
        } else {
            printPositiveLog("UPLVL", account.getKeyInter(), new String[]{"level" + account.getLevel() + ", lesson" + account.getLesson()});
        }
    }

    void gas(Account account) {
        // aaa.func(account.getJSES(), account.getSessionID());
        String Jses = account.getJSES();
        String Cookie = "_ym_uid=1475769453705393769; _ym_isad=2; javarush.internet.user.key=2497271; javarush.action.key=true; _gat=1; JSESSIONID=" + Jses + "; vk_app_3167756=expire=1475776600&mid=172762626&secret=oauth&sid=1bbba655892f68bf314c2764fed68f7616bd06a5e6c6e78153421a417407c4d618f3910aae6e81962e62c&sig=120ce7c4a67becf76841ff4b2847face; _ga=GA1.2.841430547.1475769453; _ym_visorc_23548852=w";
        String data = parser.getDataForGas(account.getSessionID());
        String[] request = parser.getRequestForGas(data.length(), Cookie);
        try {
            Send.sendPost(request, data);
        } catch (Exception e) {
        }
    }

    int[] getProgress(String ID) throws ProgressException {
        String input = Send.GET("http://javarush.ru/api/rest/user/profile.json?sessionId=" + ID);
        int[] resultArray = parser.getProgress(input);
        return resultArray;
    }

    public boolean sendExample(Account account, Example example, boolean WrongFlag) {
        Parser parser = new Parser();
        try {
            String data = parser.getDataForExample(account, example, WrongFlag);
            String[] request = parser.getRequestForExample(data.getBytes("utf-8").length);
            String result = Send.sendPost(request, data);

            if (parser.parseAnswerOnExample(result).equals("SUCCESS")) {
                return true;
            } else {
                printNegativeLog(new WrongExampleException(), account.getKeyInter(), new String[]{"level" + example.level + " lesson" + example.lesson + " " + example.namePackage});
                return false;
            }
        } catch (Exception e) {
            printNegativeLog(e, account.getKeyInter(), new String[]{"level" + example.level + " lesson" + example.lesson + " " + example.namePackage});
            return false;
        }
    }
}
