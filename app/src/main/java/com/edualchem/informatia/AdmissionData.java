package com.edualchem.informatia;

public class AdmissionData {

    String name;

     String std;

    String div;

     String dob;

     String admission;

    public AdmissionData() {
    }

    public String getAdmission() {
        return admission;
    }

    public String getDiv() {
        return div;
    }

    public String getDob() {
        return dob;
    }

    public String getName() {
        return name;
    }

    public String getStd() {
        return std;
    }

    public void setAdmission(String admission) {
        this.admission = admission;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStd(String std) {
        this.std = std;
    }
}