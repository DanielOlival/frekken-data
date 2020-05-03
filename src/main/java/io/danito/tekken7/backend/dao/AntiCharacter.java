package io.danito.tekken7.backend.dao;

public class AntiCharacter {
    private static final long serialVersionUID = 1L;

    private String move;
    private String information;

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return "AntiCharacter{" +
                "move='" + move + '\'' +
                ", information='" + information + '\'' +
                '}';
    }
}
