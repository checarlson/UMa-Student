package com.ldd.e_noticeboarduma;

public class Student {
    private String name;
    private String matricule;
    private String password;
    private String department;
    private String program;
    private String level;
    private String dob;
    private String email;
    private String phone;


    public Student() {
    }

    public Student(String name, String matricule, String password, String department, String program, String level, String dob, String email, String phone) {
        this.name = name;
        this.matricule = matricule;
        this.password = password;
        this.department = department;
        this.program = program;
        this.level = level;
        this.dob = dob;
        this.email = email;
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
