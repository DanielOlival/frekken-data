package io.danito.tekken7.backend.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A entity object, like in any other Java application. In a typical real world
 * application this could for example be a JPA entity.
 */
@SuppressWarnings("serial")
public class AdditionalNotes implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("weak_side")
    private String weakSide;
    @JsonProperty("sidestep_notes")
    private String sidestepNotes;

    public String getWeakSide() {
        return weakSide;
    }

    public void setWeakSide(String weakSide) {
        this.weakSide = weakSide;
    }

    public String getSidestepNotes() {
        return sidestepNotes;
    }

    public void setSidestepNotes(String sidestepNotes) {
        this.sidestepNotes = sidestepNotes;
    }

    public AdditionalNotes() {
        this.weakSide = "";
        this.sidestepNotes = "";
    }

    @Override
    public String toString() {
        return "AdditionalNotes{" +
                "weakSide='" + weakSide + '\'' +
                ", sidestepNotes='" + sidestepNotes + '\'' +
                '}';
    }
}