package model;

public enum Slogan {
    ENTEZ("UNDER THE SAME FLAG, FOR A SINGLE ENTEZ"),
    UNDEFEATABLE("UNDEFEATABLE LORD"),
    GHOOZ("HARKI DAR DAD KHODESH KHABAR DAD");

    String string;

    private Slogan(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

}