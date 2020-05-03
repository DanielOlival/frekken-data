package io.danito.tekken7.backend.dao;

import java.util.ArrayList;
import java.util.List;

public class AntiSummary {
    private static final long serialVersionUID = 1L;

    private List<Move> punishableStandardMoveList = new ArrayList<>();
    private List<Move> punishableLowsMoveList = new ArrayList<>();
    private List<Move> plusFramesMoveList = new ArrayList<>();
    private List<Move> commandThrowList = new ArrayList<>();
    private AdditionalNotes additionalNotes;

    public List<Move> getPunishableStandardMoveList() {
        return punishableStandardMoveList;
    }

    public void setPunishableStandardMoveList(List<Move> punishableStandardMoveList) {
        this.punishableStandardMoveList = punishableStandardMoveList;
    }

    public List<Move> getPunishableLowsMoveList() {
        return punishableLowsMoveList;
    }

    public void setPunishableLowsMoveList(List<Move> punishableLowsMoveList) {
        this.punishableLowsMoveList = punishableLowsMoveList;
    }

    public List<Move> getPlusFramesMoveList() {
        return plusFramesMoveList;
    }

    public void setPlusFramesMoveList(List<Move> plusFramesMoveList) {
        this.plusFramesMoveList = plusFramesMoveList;
    }

    public List<Move> getCommandThrowList() {
        return commandThrowList;
    }

    public void setCommandThrowList(List<Move> commandThrowList) {
        this.commandThrowList = commandThrowList;
    }

    public AdditionalNotes getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(AdditionalNotes additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
}
