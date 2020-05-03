package io.danito.tekken7.backend.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PunishmentMove {
    private static final long serialVersionUID = 1L;

    private boolean main;
    private Integer frames;
    private String move;
    @JsonProperty("in_crouching_state")
    private boolean inCrouchingState;

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public Integer getFrames() {
        return frames;
    }

    public void setFrames(Integer frames) {
        this.frames = frames;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public boolean isInCrouchingState() {
        return inCrouchingState;
    }

    public void setInCrouchingState(boolean inCrouchingState) {
        this.inCrouchingState = inCrouchingState;
    }

    @Override
    public String toString() {
        return "PunishmentMove{" +
                "main=" + main +
                ", frames=" + frames +
                ", move='" + move + '\'' +
                ", inCrouchingState=" + inCrouchingState +
                '}';
    }
}
