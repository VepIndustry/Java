package Account;

import Example.Example;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Account implements Serializable {
    public Set<Example> CurrentExamples;
    public Set<Example> WrongExamples;
    private String keyInter;
    private String SessionID;
    private int level;
    private int lesson;
    private int maxLvl;
    private byte proffecional; //from 20 to 100
    private String JSES;


    public Account(String keyInter, int level, int lesson, int maxLvl, byte newProf) {
        this.keyInter = keyInter;
        this.lesson = lesson;
        this.level = level;

        this.maxLvl = maxLvl;
        this.proffecional = newProf;
        CurrentExamples = new HashSet<>();
        WrongExamples = new HashSet<>();
    }

    public String getJSES() {
        return JSES;
    }

    public void setJSES(String JSES) {
        this.JSES = JSES;
    }

    public byte getProffecional() {
        return proffecional;
    }

    public void setProffecional(byte proffecional) {
        this.proffecional = proffecional;
    }

    public String getKeyInter() {
        return keyInter;
    }

    public int getLesson() {
        return lesson;
    }


    public int getLevel() {
        return level;
    }

    public void setProgress(int newLevel, int newLesson) {
        level = newLevel;
        lesson = newLesson;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int newMaxLvl) {
        this.maxLvl = newMaxLvl;
    }

    @Override
    public String toString() {
        return keyInter;
    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String newSession) {
        this.SessionID = newSession;
    }

    public String print() {
        String result = (keyInter + " : уровень " + level + " : лекция " + lesson + ". Кач до " + maxLvl + ".\n   Актуальные задачи:\n");
        for (Example example : CurrentExamples){
            result +=  "        level" + example.level + " lesson" + example.lesson + " " + example.namePackage + "\n";
        }
        result += "   Нерешённые задачи:\n";
        for (Example example : WrongExamples){
            result +=  "        level" + example.level + " lesson" + example.lesson + " " + example.namePackage + ". Broke = " + example.Broke + "\n";
        }
        return result;
    }
}
