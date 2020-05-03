package io.danito.tekken7.backend.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A entity object, like in any other Java application. In a typical real world
 * application this could for example be a JPA entity.
 */
@SuppressWarnings("serial")
public class Move implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String move;
    @JsonProperty("hit_level")
    private String hitLevel;
    private String damage;
    @JsonProperty("startup_range")
    private String startup;
    @JsonProperty("on_block")
    private String onBlock;
    private Integer onBlockValue;
    @JsonProperty("on_hit")
    private String onHit;
    @JsonProperty("on_counter_hit")
    private String onCounterHit;
    private String notes;
    private boolean endsWithLow;
    private boolean plusFramesOnBlock;
    private String punishment;
    private boolean additionalNote = false;

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public String getHitLevel() {
        return hitLevel;
    }

    public void setHitLevel(String hitLevel) {
        this.hitLevel = hitLevel;
        this.endsWithLow = checkEndsWithLow(hitLevel);
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getStartup() {
        return startup;
    }

    public void setStartup(String startup) {
        this.startup = startup;
    }

    public String getOnBlock() {

        return onBlock;
    }

    public void setOnBlock(String onBlock) {
        this.onBlock = onBlock;
        this.plusFramesOnBlock = checkPlusFramesOnBlock(onBlock);
        setOnBlockValue(convertFrames(onBlock));
    }

    public Integer getOnBlockValue() {
        return onBlockValue;
    }

    public void setOnBlockValue(Integer onBlockValue) {
        this.onBlockValue = onBlockValue;
    }

    public String getOnHit() {
        return onHit;
    }

    public void setOnHit(String onHit) {
        this.onHit = onHit;
    }

    public String getOnCounterHit() {
        return onCounterHit;
    }

    public void setOnCounterHit(String onCounterHit) {
        this.onCounterHit = onCounterHit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isEndsWithLow() {
        return endsWithLow;
    }

    public boolean isPlusFramesOnBlock() {
        return plusFramesOnBlock;
    }

    public String getPunishment() {
        return punishment;
    }

    public void setPunishment(String punishment) {
        this.punishment = punishment;
    }

    public boolean isAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(boolean additionalNote) {
        this.additionalNote = additionalNote;
    }

    @Override
    public Move clone() throws CloneNotSupportedException {
        return (Move) super.clone();
    }

    private boolean checkEndsWithLow(String hitLevel) {
        boolean isLastHitLow = false;
        Pattern pattern = Pattern.compile("(?<!\\()\\b(l|m|h)\\b(?![\\w\\s]*[\\)])");
        Matcher matcher = pattern.matcher(hitLevel);
        while (matcher.find()) {
            isLastHitLow = "l".equals(matcher.group(0));
        }
        return isLastHitLow;
    }

    private boolean checkPlusFramesOnBlock(String onBlock) {
        Pattern pattern = Pattern.compile("^(\\+[0-9]+|[0-9]+)");
        Matcher matcher = pattern.matcher(onBlock);
        if (matcher.find())
            return true;
        return false;
    }

    private Integer convertFrames(String frames) {
        Pattern pattern = Pattern.compile("-[0-9]+");
        Matcher matcher = pattern.matcher(frames);
        if (matcher.find())
            return Integer.decode(matcher.group());
        return 0;
    }

    @Override
    public String toString() {
        return "Move{" +
                "move='" + move + '\'' +
                ", hitLevel='" + hitLevel + '\'' +
                ", damage='" + damage + '\'' +
                ", startup='" + startup + '\'' +
                ", onBlock='" + onBlock + '\'' +
                ", onBlockValue=" + onBlockValue +
                ", onHit='" + onHit + '\'' +
                ", onCounterHit='" + onCounterHit + '\'' +
                ", notes='" + notes + '\'' +
                ", endsWithLow=" + endsWithLow +
                ", plusFramesOnBlock=" + plusFramesOnBlock +
                ", punishment='" + punishment + '\'' +
                ", additionalNote=" + additionalNote +
                '}';
    }

    public String toString(boolean moveColumn, boolean hitLevelColumn, boolean damageColumn, boolean startupColumn, boolean onBlockColumn,
                           boolean onHitColumn, boolean onCounterHitColumn, boolean notesColumn, boolean punishmentColumn) {
        String toString = " ";
        if (moveColumn)
            toString = toString + move;
        if (hitLevelColumn)
            toString = toString + hitLevel;
        if (damageColumn)
            toString = toString + damage;
        if (startupColumn)
            toString = toString + startup;
        if (onBlockColumn)
            toString = toString + onBlock;
        if (onHitColumn)
            toString = toString + onHit;
        if (onCounterHitColumn)
            toString = toString + onCounterHit;
        if (notesColumn)
            toString = toString + notes;
        if (punishmentColumn)
            toString = toString + punishment;
        return toString;
    }
}