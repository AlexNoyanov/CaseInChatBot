package dbService.entity;

import java.util.ArrayList;
import java.util.List;

public class Departments {
    private static List<String> departments;

    private Departments() {}

    static {
        departments = new ArrayList<>();
        departments.add("Финансовый отдел");
        departments.add("Отдел закупок");
        departments.add("Отдел кадров");
        departments.add("Юридический отдел");
        departments.add("Отдел рекламы");
        departments.add("Отдел информационных технологий");
    }

    public static String get(int index) {
        return departments.get(index);
    }

    public static boolean contains(String department) {
        return departments.contains(department);
    }

    public static int size() {
        return departments.size();
    }

    public static List<String> getDepartments() {
        return departments;
    }
}
