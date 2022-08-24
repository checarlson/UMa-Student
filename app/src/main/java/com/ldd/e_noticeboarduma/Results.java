package com.ldd.e_noticeboarduma;

public class Results {
    private int sn;
    private String matricule;
    private String semester;
    private String academicYear;
    private String courseCode;
    private String courseTitle;
    private String cc;
    private String ee;
    private String tpe;
    private String total;
    private String moyen;
    private String decision;
    private String mension;

    public Results() {
    }

    public Results(int sn, String matricule, String semester, String academicYear, String courseCode, String courseTitle, String cc, String ee, String tpe, String total, String moyen, String decision, String mension) {
        this.sn = sn;
        this.matricule = matricule;
        this.semester = semester;
        this.academicYear = academicYear;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.cc = cc;
        this.ee = ee;
        this.tpe = tpe;
        this.total = total;
        this.moyen = moyen;
        this.decision = decision;
        this.mension = mension;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getEe() {
        return ee;
    }

    public void setEe(String ee) {
        this.ee = ee;
    }

    public String getTpe() {
        return tpe;
    }

    public void setTpe(String tpe) {
        this.tpe = tpe;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMoyen() {
        return moyen;
    }

    public void setMoyen(String moyen) {
        this.moyen = moyen;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getMension() {
        return mension;
    }

    public void setMension(String mension) {
        this.mension = mension;
    }
}
