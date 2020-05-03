package io.danito.tekken7.backend.dao;

public class Config {
    private static final long serialVersionUID = 1L;

    private String mainCharacterName;

    public String getMainCharacterName() {
        return mainCharacterName;
    }

    public void setMainCharacterName(String mainCharacterName) {
        this.mainCharacterName = mainCharacterName;
    }

    public Config() {
    }

    public Config(String mainCharacterName) {
        this.mainCharacterName = mainCharacterName;
    }

    @Override
    public String toString() {
        return "Config{" +
                "mainCharacterName='" + mainCharacterName + '\'' +
                '}';
    }
}
