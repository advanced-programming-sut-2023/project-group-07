package Server.model;

public enum MessageReaction {
    HAPPY(":)"),
    POKER(":("),
    SAD(":|");
    private String symbol;

    MessageReaction(String symbol) {
        this.symbol = symbol;
    }
}
