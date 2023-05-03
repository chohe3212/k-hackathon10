package org.taehyeon.welcome_pet_khackathon.Auth;

public class UserAccount {
    // check는 프개르먼트 유지 job은 직업
    private String pw,name,email,phone,check,Idtoken,job,count,progress,arch;

    public UserAccount(){ }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }


    public String getCheck() {
        return check;
    }
    public void setCheck(String check) {
        this.check = check;
    }


    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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


    public String getIdtoken() {
        return Idtoken;
    }
    public void setIdtoken(String Idtoken) {
        this.Idtoken = Idtoken;
    }


}
