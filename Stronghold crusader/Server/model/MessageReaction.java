package Server.model;

public enum MessageReaction {
    HAPPY(":)"),
    POKER(":/"),
    SAD(":(");
    private String symbol;

    MessageReaction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return this.symbol;
    }

    public static MessageReaction getReactionBySymbol(String symbol){
        for(MessageReaction reaction : MessageReaction.values()){
            if (reaction.symbol.equals(symbol)) return reaction;
        }
        return null;
    }
}
