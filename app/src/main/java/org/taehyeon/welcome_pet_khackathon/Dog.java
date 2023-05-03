package org.taehyeon.welcome_pet_khackathon;

public class Dog {
    private int Sociality,Activity,bark,Loyalty,Iq;

    public Dog(int sociality, int activity, int bark, int loyalty, int iq) {
        Sociality = sociality;
        Activity = activity;
        this.bark = bark;
        Loyalty = loyalty;
        Iq = iq;
    }

    public int getSociality() {
        return Sociality;
    }

    public void setSociality(int sociality) {
        Sociality = sociality;
    }

    public int getActivity() {
        return Activity;
    }

    public void setActivity(int activity) {
        Activity = activity;
    }

    public int getBark() {
        return bark;
    }

    public void setBark(int bark) {
        this.bark = bark;
    }

    public int getLoyalty() {
        return Loyalty;
    }

    public void setLoyalty(int loyalty) {
        Loyalty = loyalty;
    }

    public int getIq() {
        return Iq;
    }

    public void setIq(int iq) {
        Iq = iq;
    }
}
