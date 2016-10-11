package Example;

import java.io.Serializable;

public class Example implements Serializable{
    public int level;
    public int lesson;
    public String namePackage;
    public boolean Broke = false;

    public Example(int lvl, int lsn, String name) {
        level = lvl;
        lesson = lsn;
        namePackage = name;
    }

    @Override
    public String toString() {
        return Integer.toString(level) + Integer.toString(lesson) + namePackage + Broke;
    }
}
