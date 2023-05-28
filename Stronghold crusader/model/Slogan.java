package model;

import controller.Controller;

public enum Slogan {
    ENTEZ("UNDER THE SAME FLAG, FOR A SINGLE ENTEZ"),
    UNBEATABLE("UNBEATABLE LORD"),
    GHOOZ("HARKI DAR DAD KHODESH KHABAR DAD");

    String string;

    private Slogan(String string) {
        this.string = string;
    }

    public static Slogan getRandomSlogan() {
        return Slogan.values()[Controller.randomNumber(Slogan.values().length)];
    }

    @Override
    public String toString() {
        return this.string;
    }

}