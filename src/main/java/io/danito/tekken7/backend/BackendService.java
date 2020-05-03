package io.danito.tekken7.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.danito.tekken7.backend.dao.*;
import org.springframework.stereotype.Service;

@Service
public class BackendService {

    private static BackendService instance;
    private static final Logger LOGGER = Logger.getLogger(BackendService.class.getName());

    private final HashMap<String, List<Move>> moveListHashMap = new HashMap<>();
    private final HashMap<String, List<PunishmentMove>> punishmentMoveListHashMap = new HashMap<>();
    private final HashMap<String, List<AntiCharacter>> antiCharacterListHashMap = new HashMap<>();
    private final HashMap<String, List<Move>> commandThrowListHashMap = new HashMap<>();
    private final HashMap<String, AdditionalNotes> additionalNotesHashMap = new HashMap<>();
    private List<String> characterList = new ArrayList<>();
    private Config config;

    public static BackendService getInstance() {
        if (instance == null) {
            instance = new BackendService();
        }
        return instance;
    }

    private String getBlockPunishment(Integer onBlock, List<PunishmentMove> punishmentMoveList) {
        String blockPunishment = "";
        for (PunishmentMove punishmentMove : punishmentMoveList) {
            if (punishmentMove.getFrames() <= onBlock * -1 && punishmentMove.isMain()) {
                blockPunishment = punishmentMove.getMove();
            }
        }
        return blockPunishment;
    }

    private String checkBlockPunishment(Move move, List<PunishmentMove> punishmentMoveList) {
        String punishment = "";
        for (PunishmentMove punishmentMove : punishmentMoveList) {
            if (punishmentMove.getFrames() <= move.getOnBlockValue() * -1 &&
                    punishmentMove.isMain() && move.isEndsWithLow() == punishmentMove.isInCrouchingState()) {
                punishment = punishmentMove.getMove();
            }
        }
        return punishment;
    }

    public String findMainCharacter() {
        if (config != null)
            return config.getMainCharacterName();

        // create object mapper instance
        ObjectMapper mapper = new ObjectMapper();
        // convert JSON array to list of characters
        File file = new File("./config/config1.json");
        if (! file.exists()) {
            LOGGER.info("No configuration file found.");
            return null;
        }
        try {
            config = mapper.readValue(file, Config.class);
            return config.getMainCharacterName();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to read configuration file! " +  e.getMessage());
        }
        return null;
    }

    public void saveMainCharacter(String character) {
        // create object mapper instance
        ObjectMapper mapper = new ObjectMapper();
        config = new Config(character);
        // convert JSON array to list of characters
        File file = new File("./config/config1.json");
        try {
            // Serialize Java object info JSON file.
            mapper.writeValue(file, config);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to write to configuration file! " + e.getMessage());
        }
    }

    private void createCharacterFolderStructure(List<String> characterList) {
        try {
            for (String character : characterList) {
                File directory = new File("./config/" + character);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Unable to create folder structure! " + ex.getMessage());
        }
    }

    public boolean saveAntiCharacter(String character, String mainCharacter) {

        AntiCharacter antiCharacter;
        List<AntiCharacter> antiCharacterList = new ArrayList<>();
        for (Move move : moveListHashMap.get(character)) {
            if (move.isAdditionalNote()) {
                antiCharacter = new AntiCharacter();
                antiCharacter.setMove(move.getMove());
                antiCharacter.setInformation(move.getPunishment());
                antiCharacterList.add(antiCharacterList.size(), antiCharacter);
            }
        }

        antiCharacterListHashMap.put(character, antiCharacterList);

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            File file = new File("./config/" + mainCharacter + "/anti_" + character + ".json");
            // Serialize Java object info JSON file.
            mapper.writeValue(file, antiCharacterList);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Unable to save anti character information! " +  e.getMessage());
        }
        return true;
    }

    public List<Move> findMoves(String character, String yourCharacter) {

        List<Move> moveListCharacter = findFrameData(character);

        List<PunishmentMove> punishmentMoveListMain = findPunishment(yourCharacter);
        List<AntiCharacter> antiCharacterList = findAntiInformation(character, yourCharacter);

        getAdditionalNotes(moveListCharacter, punishmentMoveListMain, antiCharacterList);
        for (Move move : findCommandThrow(character))
            moveListCharacter.add(moveListCharacter.size(),move);

        return moveListCharacter;

    }

    /**
     * Retrieve all character names
     * @return
     */
    public synchronized List<String> findCharacters() {
        if (characterList.isEmpty()) {
            try {
                // create object mapper instance
                ObjectMapper mapper = new ObjectMapper();

                try (Stream<String> lines = Files.lines(Paths.get("./config/characters.conf"))) {
                    characterList = lines.collect(Collectors.toList());
                    createCharacterFolderStructure(characterList);
                }

            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Unable to load character list! " +  ex.getMessage());
            }

        }
        return characterList;
    }


    private List<Move> findFrameData(String character) {

        if (moveListHashMap.containsKey(character))
            return moveListHashMap.get(character);

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON array to list of characters
            File file = new File("./config/" + character + "/frame_data.json");
            if (file.exists()) {
                moveListHashMap.put(character, new ArrayList<>(Arrays.asList(mapper.readValue(file, Move[].class))));
                return moveListHashMap.get(character);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to map frame data information! " +  e.getMessage());
        }

        return new ArrayList<>();

    }

    private List<PunishmentMove> findPunishment(String character) {
        if (punishmentMoveListHashMap.containsKey(character))
            return punishmentMoveListHashMap.get(character);

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON array to list of characters
            File file = new File("./config/" + character + "/" + "punishment.json");
            if (file.exists()) {
                punishmentMoveListHashMap.put(character, Arrays.asList(mapper.readValue(file, PunishmentMove[].class)));
                return punishmentMoveListHashMap.get(character);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to map punishment data information! " +  e.getMessage());
        }
        return new ArrayList<>();
    }

    private List<AntiCharacter> findAntiInformation(String character, String yourCharacter) {
        if (antiCharacterListHashMap.containsKey(yourCharacter + "_" + character))
            return antiCharacterListHashMap.get(yourCharacter + "_" + character);

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON array to list of characters
            File file = new File("./config/" + yourCharacter + "/anti_" + character + ".json");
            if (file.exists()) {
                antiCharacterListHashMap.put(yourCharacter + "_" + character, Arrays.asList(mapper.readValue(file, AntiCharacter[].class)));
                return antiCharacterListHashMap.get(yourCharacter + "_" + character);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to map anti data information! " +  e.getMessage());
        }
        return new ArrayList<>();
    }

    public AdditionalNotes findAdditionalNote(String character) {
        if (additionalNotesHashMap.containsKey(character))
            return additionalNotesHashMap.get(character);

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON array to list of characters
            File file = new File("./config/" + character + "/additional_notes.json");
            if (file.exists()) {
                additionalNotesHashMap.put(character, mapper.readValue(file, AdditionalNotes.class));
                return additionalNotesHashMap.get(character);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to map additional notes information! " +  e.getMessage());
        }
        return new AdditionalNotes();
    }

    private void getAdditionalNotes(List<Move> moveList, List<PunishmentMove> punishmentMoveList, List<AntiCharacter> antiCharacterList) {

        for (Move move : moveList) {
                move.setPunishment("");
                move.setAdditionalNote(false);
                for (AntiCharacter antiCharacter : antiCharacterList) {
                    if (antiCharacter.getMove().equals(move.getMove())) {
                        move.setPunishment(antiCharacter.getInformation());
                        move.setAdditionalNote(true);
                        continue;
                    }
                }
                if (!move.isAdditionalNote()) {
                    move.setPunishment(checkBlockPunishment(move, punishmentMoveList));
                }
        }
    }

    private List<Move> findCommandThrow(String character) {

        if (commandThrowListHashMap.containsKey(character))
            return commandThrowListHashMap.get(character);

        try {
            // create object mapper instance
            ObjectMapper mapper = new ObjectMapper();
            // convert JSON array to list of characters
            File file = new File("./config/" + character + "/command_throw.json");
            if (file.exists()) {
                commandThrowListHashMap.put(character, Arrays.asList(mapper.readValue(file, Move[].class)));
                return commandThrowListHashMap.get(character);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to map command throw information! " +  e.getMessage());
        }

        return new ArrayList<>();

    }

}
