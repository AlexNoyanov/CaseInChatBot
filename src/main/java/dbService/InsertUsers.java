package dbService;

import dbService.dao.EmployeeDAO;
import dbService.dao.EmployeeDAOImpl;
import dbService.entity.Employee;

public class InsertUsers {
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAOImpl();

        Employee e1 = new Employee("Андрей", "Сидоров", "Михайлович", "Руководитель отдела",
                "Отдел информационных технологий", "meovrovmoevme", "eriviermvoevmoer", "232334234");

        Employee e2 = new Employee("Евгений", "Кузнецов", "Александрович", "Руководитель отдела",
                "Отдел закупок", "gntyntynt", "weewcw2c2", "345034750");

        Employee e3 = new Employee("Александр", "Николаев", "Константинович", "Руководитель отдела",
                "Отдел кадров", "f2f2f22vc2", "67m6mymy", "347534753");

        Employee e4 = new Employee("Сергей", "Соколов", "Эдуардович", "Руководитель отдела",
                "Юридический отдел", "mpemrpvep", "wefwvwvbtrbtrbrt", "26634235");

        Employee e5 = new Employee("Виктор", "Ершов", "Сергеевич", "Руководитель отдела",
                "Отдел рекламы", "epvppmwpwec", ".wpc,pwe,cpwec,", "322649534");

        Employee e6 = new Employee("Анна", "Сторожева", "Александровна", "Руководитель отдела",
                "Финансовый отдел", "reovnerovno", "ieow329v92", "923426394");

        Employee e7 = new Employee("Дарья", "Полякова", "Дмитриевна", "Младший разработчик",
                "Отдел информационных технологий", "evonerovenv", "qwdqqwqxqwx", "374927494");

        Employee e8 = new Employee("Николай", "Соловьёв", "Михайлович", "Главный бухгалтер",
                "Финансовый отдел", "ververvre", "erbewvwcewxew", "84035304");

        Employee e9 = new Employee("Алексей", "Киселёв", "Андреевич", "Менеджер по персоналу",
                "Отдел кадров", "cmemvoermve", "meovervme", "273493742");

        Employee e10 = new Employee("Анастасия", "Синицына", "Анатольевна", "Главный маркетолог",
                "Отдел рекламы", "reovnerovno", "ieow329v92", "923426394");

        dao.insert(e1);
        dao.insert(e2);
        dao.insert(e3);
        dao.insert(e4);
        dao.insert(e5);
        dao.insert(e6);
        dao.insert(e7);
        dao.insert(e8);
        dao.insert(e9);
        dao.insert(e10);




    }
}
