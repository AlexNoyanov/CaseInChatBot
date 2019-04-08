package dbService.entity;

public class Employee extends DataBaseObject{
    private String firstName;
    private String surName;
    private String middleName;

    private String position;
    private String department;

    private String login;
    private String pass;
    private String chatId;

    public Employee() {}

    public Employee(String firstName, String surName, String middleName, String position, String department, String login, String pass, String chatId) {
        this.firstName = firstName;
        this.surName = surName;
        this.middleName = middleName;
        this.position = position;
        this.department = department;
        this.login = login;
        this.pass = pass;
        this.chatId = chatId;
    }

    public String getShortName() {
        return surName + ' ' + Character.toUpperCase(firstName.charAt(0)) + '.'
                + Character.toUpperCase(middleName.charAt(0)) + '.';
    }

    public String getFullName() {
        return surName + " " + firstName + " " + middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String pos) {
        this.position = pos;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}
