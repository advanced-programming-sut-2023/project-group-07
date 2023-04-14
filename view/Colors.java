package view;

public enum Colors {
    RESET("\033[0m"),
    RED_BACKGROUND("\033[41m"),
    GREEN_BACKGROUND("\033[42m"),
    YELLOW_BACKGROUND("\033[43m"),
    BLUE_BACKGROUND("\033[44m"),
    MAGENTA_BACKGROUND("\033[45m"),
    PURPLE_BACKGROUND("\033[45m"),
    CYAN_BACKGROUND("\033[46m"),
    WHITE_BACKGROUND("\033[47m"),
    GREEN_BACKGROUND_BRIGHT("\033[0;102m"),
    PURPLE_BACKGROUND_BRIGHT("\033[0;105m"),
    YELLOW_BACKGROUND_BRIGHT("\033[0;103m");
  

    private final String code;
    private Colors (String code){
        this.code = code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}