package com.ldd.e_noticeboarduma;

public class Student {
    private String name;
    private String matricule;
    private String password;
    private String department;
    private String program;
    private String level;

    public Student() {
    }

    public Student(String name, String matricule, String password, String department, String program, String level) {
        this.name = name;
        this.matricule = matricule;
        this.password = password;
        this.department = department;
        this.program = program;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
