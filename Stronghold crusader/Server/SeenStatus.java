package Server;

public enum SeenStatus {
    SENT("✓"),
    SEEN("✓✓");
    private String symbol;
    private SeenStatus(String symbol){
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
