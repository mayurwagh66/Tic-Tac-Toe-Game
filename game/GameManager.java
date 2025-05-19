package game;

import exceptions.InvalidPlayerNameException;

public class GameManager {
    private String playerXName;
    private String playerOName;
    private int totalRounds;

    public GameManager(String x, String o, int rounds) throws InvalidPlayerNameException {
        if (x == null || x.trim().isEmpty() || o == null || o.trim().isEmpty()) {
            throw new InvalidPlayerNameException("Player names cannot be empty.");
        }
        this.playerXName = x.trim();
        this.playerOName = o.trim();
        this.totalRounds = rounds;
    }

    public String getPlayerXName() {
        return playerXName;
    }

    public String getPlayerOName() {
        return playerOName;
    }

    public int getTotalRounds() {
        return totalRounds;
    }
}
