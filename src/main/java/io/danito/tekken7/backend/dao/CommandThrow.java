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
public class CommandThrow implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String command;
    @JsonProperty("break")
    private String commandBreak;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandBreak() {
        return commandBreak;
    }

    public void setCommandBreak(String commandBreak) {
        this.commandBreak = commandBreak;
    }

    @Override
    public String toString() {
        return "Throws{" +
                "command='" + command + '\'' +
                ", commandBreak='" + commandBreak + '\'' +
                '}';
    }
}