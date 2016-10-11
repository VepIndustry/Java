package Parser;

import Account.Account;
import Constants.Constants;
import Example.Example;
import Exceptions.FileBrock;
import Exceptions.NoJSESSION;
import Exceptions.NoLogin;
import Exceptions.ProgressException;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Parser {
    public String getJSession(String input) throws NoJSESSION {
        int index = input.indexOf("Set-Cookie:") + 23;
        if (index == 22) {
            throw new NoJSESSION();
        }
        return input.substring(index, index + 32);
    }

    public String getSessionID(String input) throws NoLogin {
        int index = input.indexOf("\"sessionId\":") + "\"sessionId\":".length() + 1;
        String SessionID = input.substring(index, index + "dd1828e3-75e9-4367-a890-85e81939580c".length());
        if (SessionID.indexOf("ull") != -1) {
            throw new NoLogin();
        } else {
            return SessionID;
        }
    }

    public String getRequestForCheck(String Cookie) {
        return "GET /api/rest/user/server/statistics.json?v=2.1475778969863 HTTP/1.1\n"
                + "Host: javarush.ru\n"
                + "Connection: keep-alive\n"
                + "Upgrade-Insecure-Requests: 1\n"
                + "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 YaBrowser/16.9.1.1192 Yowser/2.5 Safari/537.36\n"
                + "Accept: */*\n"
                + "Accept-Encoding: gzip, deflate, sdch\n"
                + "Accept-Language: ru,en;q=0.8\n"
                + "Cookie: " + Cookie + "\n\n";
    }

    public String getRequestForNewJSESSION() {
        return "GET /user/profile HTTP/1.0\n"
                + "Host: httpbin.org\n"
                + "Accept: text/html\n\n";
    }

    public int[] getProgress(String input) throws ProgressException {
        int[] resultArray = new int[2];
        if (!input.equals("false")) {
            input = input.substring(1, input.length() - 2);
            String[] ArrayStr = input.split(",");
            resultArray[0] = Integer.parseInt(ArrayStr[5].substring(12));
            resultArray[1] = Integer.parseInt(ArrayStr[6].substring(13));

            return resultArray;
        } else {
            throw new ProgressException();
        }
    }

    public String getDataForExample(Account account, Example example, boolean wrongFlag) throws FileNotFoundException, FileBrock {
        String code = getCode(example.level, example.lesson, example.namePackage);
        if (wrongFlag){
            code = "VepIndustry";
        }

        String data = "<?xml version=\"1.0\"?>" +
                "<S:Envelope xmlns:S=\"http://www.w3.org/2003/05/soap-envelope\">" +
                "<S:Body>" +
                "<ns2:ValidateForPlugin xmlns:ns2=\"http://api1.controller.diana.javarush.com/\">" +
                "<sessionId>" + account.getSessionID() + "</sessionId>" +
                "<taskKey>" + getTaskKey(example.level, example.lesson, example.namePackage) + "</taskKey>" +
                "<classList>" +
                "<list>" +
                "<Package>" + getPackage(example.level, example.lesson, example.namePackage) + "</Package>" +
                "<FileName>" + getNameFile(example.level, example.lesson, example.namePackage) + "</FileName>" +
                "<ContentCode>" + code + "</ContentCode>" +
                "</list>" +
                "</classList>" +
                "</ns2:ValidateForPlugin>" +
                "</S:Body>" +
                "</S:Envelope>";
        return data;
    }

    public String getDataForGas(String ID) {
        return "7|0|6|http://javarush.ru/com.javarush.diana.ui.profile.Profile/|FE6B219E4F3DE9EBF9E5B863539FE436|com.javarush.diana.ui.common.client.GwtUserService|refuelSpacecraft|java.lang.String/2004016611|" + ID + "|1|2|3|4|1|5|6|";
    }


    public String[] getRequestForGas(int LengthData, String Cookie) {
        String[] result = new String[16];

        result[0] = "POST /api/api2/gwt/gwt.user HTTP/1.1\r\n";
        result[1] = "Host: javarush.ru\r\n";
        result[2] = "Connection: keep-alive\r\n";
        result[3] = "Content-Length: " + LengthData + "\r\n";

        result[4] = "Origin: http: javarush.ru\r\n";

        result[5] = "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; ru; rv:1.8.1.12) Gecko/20080201 Firefox\r\n";
        result[6] = "Content-Type: text/x-gwt-rpc; charset=utf-8\r\n";
        result[7] = "Accept: */*\r\n";
        result[8] = "Referer: http://javarush.ru/Profile.html?v=8\r\n";
        result[9] = "Accept-Encoding: gzip, deflate\r\n";
        result[10] = "Accept-Language: en-US\r\n";

        result[11] = "Pragma: no-cache\r\n";
        result[12] = "X-GWT-Module-Base: http://javarush.ru/com.javarush.diana.ui.profile.Profile/\r\n";
        result[13] = "X-GWT-Permutation: DBBA90E7FC60353BB052F2DEA5411207\r\n";

        result[14] = "Referer: http://javarush.ru/api/api2/gwt/gwt.user\r\n";
        result[15] = "Cookie: " + Cookie + "\r\n";

        return result;
    }


    public String[] getRequestForExample(int LengthData) {
        String[] result = new String[7];
        String path = "/api/api1/JarCommonService";

        result[0] = "POST " + path + " HTTP/1.1\r\n";
        result[1] = "Accept: application/soap+xml, multipart/related\r\n";
        result[2] = "User-Agent: JAX-WS RI 2.2.9-b130926.1035 svn-revision#5f6196f2b90e9460065a4c2f4e30e065b245e51e\r\n";
        result[3] = "Content-Type: application/soap+xml; charset=utf-8;action=\"\"\r\n";
        result[4] = "Host: javarush.ru\r\n";
        result[5] = "Connection: keep-alive\r\n";
        result[6] = "Content-Length: " + LengthData + "\r\n";

        return result;
    }

    public String[] getRequestForEnter(int LengthData, String Cookie) {
        String[] result = new String[14];

        result[0] = "POST /user/signin/anonimous HTTP/1.1\r\n";
        result[1] = "Host: javarush.ru\r\n";
        result[2] = "Connection: keep-alive\r\n";
        result[3] = "Content-Length: " + LengthData + "\r\n";
        result[4] = "Cache-Control: max-age=0\r\n";
        result[5] = "Origin: http: javarush.ru\r\n";
        result[6] = "Upgrade-Insecure-Requests: 1\r\n";
        result[7] = "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.1; ru; rv:1.8.1.12) Gecko/20080201 Firefox\r\n";
        result[8] = "Content-Type: application/x-www-form-urlencoded\r\n";
        result[9] = "Accept: text/html,application/xhtml+xml;q=0.9,image/webp,*/*;q=0.8\r\n";
        result[10] = "Referer: http://javarush.ru/login.html\r\n";
        result[11] = "Accept-Encoding: gzip, deflate\r\n";
        result[12] = "Accept-Language: ru,en;q=0.8\r\n";
        result[13] = "Cookie: " + Cookie + "\r\n";

        return result;
    }

    public String getDataForEnter(String key) {
        return "privateKey=" + key;
    }


    private String getTaskKey(int lvl, int lsn, String nameExample) {
        return "level" + cURL(lvl) + ",lesson" + cURL(lsn) + "," + nameExample;
    }

    private String getPackage(int lvl, int lsn, String nameExample) {

        return "com.javarush.test.level" + cURL(lvl) + ".lesson" + cURL(lsn) + "." + nameExample;
    }

    //заточено для отправки только одного файла
    private String getNameFile(int lvl, int lsn, String nameExample) throws FileNotFoundException {
        File dir = new File(Constants.DirectoryName + "\\level" + cURL(lvl) + "\\lesson" + cURL(lsn) + "\\" + nameExample);
        if (dir.isDirectory()) {
            // получаем все вложенные объекты в каталоге
            for (File item : dir.listFiles()) {
                return item.getName();
            }
        }
        throw new FileNotFoundException();
    }

    private String getCode(int lvl, int lsn, String nameExample) throws FileNotFoundException, FileBrock {
        File dir = new File(Constants.DirectoryName + "\\level" + cURL(lvl) + "\\lesson" + cURL(lsn) + "\\" + nameExample);
        if (dir.isDirectory()) {
            // получаем все вложенные объекты в каталоге
            for (File item : dir.listFiles()) {
                try {
                    String str = new String(Files.readAllBytes(Paths.get(item.getAbsolutePath()))).replace("\n", "\r\n");
                    str = str.replace("&", "&amp;");
                    str = str.replace("<", "&lt;");
                    str = str.replace(">", "&gt;");

                    int index = str.indexOf("p");
                    return str.substring(index);
                } catch (Exception e) {
                    throw new FileBrock();
                }
            }
        }
        throw new FileNotFoundException();
    }

    public String cURL(int a) {
        String IntForURL = "" + a;
        if (a < 10) {
            IntForURL = "0" + a;
        }
        return IntForURL;
    }

    public String parseAnswerOnExample(String input) {
        int index = input.indexOf("<validationStatus>") + "<validationStatus>".length();
        String result = input.substring(index, "SUCCESS".length() + index);
        return result;

    }
}
