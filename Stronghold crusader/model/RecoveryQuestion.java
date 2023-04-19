package model;

import java.lang.reflect.Constructor;

public enum RecoveryQuestion{
    FATHERNAME("What is your father\'s name?"),
    MOTHERNAME("What is your mother\'s last name?"),
    PETNAME("What was your first pet\'s name?");
    private String question;
    private RecoveryQuestion(String question){
        this.question = question;
    }
    public static String getQuestion(RecoveryQuestion recoveryQuestion){
        return recoveryQuestion.question;
    }
    @Override
    public String toString() {
        return this.question;
    }
}