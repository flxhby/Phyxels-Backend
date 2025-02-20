package com.phyxels.phyxels.controller;

import com.google.gson.Gson;
import com.phyxels.phyxels.model.SaveState;
import com.phyxels.phyxels.repository.SaveStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/v0/save", produces = "application/json")
public class SaveStateController {

    private static final Logger logger = LoggerFactory.getLogger(SaveStateController.class);

    @Autowired
    SaveStateRepository saveStateRepository;

    @PostMapping(consumes = "text/plain")
    public ResponseEntity<SaveState> saveSaveState(@RequestBody String saveStatePayload) {
        logger.info("Entering POST /api/v0/save with raw payload: {}", saveStatePayload);
        try {
            SaveState saveState = new Gson().fromJson(saveStatePayload, SaveState.class);
            SaveState saved = saveStateRepository.save(saveState);
            logger.info("Exiting POST /api/v0/save with saved entity: {}", saved);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.error("Error occurred in POST /api/v0/save", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping(value = "/{id}", consumes = "text/plain")
    public ResponseEntity<SaveState> updateSaveState(@PathVariable String id, @RequestBody String saveStatePayload) {
        logger.info("Entering POST /api/v0/save/{} with raw payload: {}", id, saveStatePayload);
        try {
            SaveState saveState = new Gson().fromJson(saveStatePayload, SaveState.class);
            saveState.setId(id);

            Optional<SaveState> existingSave = saveStateRepository.findById(id);
            existingSave.ifPresent(state -> saveState.setRev(state.getRev()));

            SaveState saved = saveStateRepository.save(saveState);
            logger.info("Exiting POST /api/v0/save/{} with updated entity: {}", id, saved);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            logger.error("Error occurred in POST /api/v0/save/{}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<SaveState>> getAllSaveStates() {
        logger.info("Entering GET /api/v0/save");
        try {
            List<SaveState> saveStates = saveStateRepository.findAll();
            logger.info("Exiting GET /api/v0/save with retrieved save states: {}", saveStates);
            return ResponseEntity.ok(saveStates);
        } catch (Exception e) {
            logger.error("Error occurred in GET /api/v0/save", e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaveState> getSaveStateById(@PathVariable String id) {
        logger.info("Entering GET /api/v0/save/{} with ID: {}", id, id);
        try {
            Optional<SaveState> saveState = saveStateRepository.findById(id);
            if (saveState.isPresent()) {
                logger.info("Exiting GET /api/v0/save/{} with save state: {}", id, saveState.get());
                return ResponseEntity.ok(saveState.get());
            } else {
                logger.warn("Exiting GET /api/v0/save/{} - save state not found", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error occurred in GET /api/v0/save/{}", id, e);
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/mock")
    public ResponseEntity<SaveState> getMockSaveState() {
        logger.info("Entering GET /api/v0/save/mock to return a mock SaveState object");

        SaveState mockSaveState = new SaveState();
        mockSaveState.setId("mock-id");
        mockSaveState.setRev("1-mockrev");
        mockSaveState.setSave("""
                {
                  "save": "[{\\"type\\":0,\\"x\\":395,\\"y\\":274.55000000000655},{\\"type\\":0,\\"x\\":390,\\"y\\":274.55000000000604},{\\"type\\":0,\\"x\\":389,\\"y\\":274.5500000000082},{\\"type\\":0,\\"x\\":388,\\"y\\":274.550000000005},{\\"type\\":0,\\"x\\":393,\\"y\\":274.5540833333346},{\\"type\\":0,\\"x\\":404,\\"y\\":274.5500000000016},{\\"type\\":0,\\"x\\":410,\\"y\\":274.55000000000126},{\\"type\\":0,\\"x\\":408,\\"y\\":274.5500000000167},{\\"type\\":0,\\"x\\":400,\\"y\\":274.55000000000575},{\\"type\\":0,\\"x\\":393,\\"y\\":273.66225000000793},{\\"type\\":0,\\"x\\":392,\\"y\\":274.5500000000024}]",
                  "name": "Test",
                  "date": "2025-01-23T08:32:55.518Z"
                }""");
        mockSaveState.setName("Mock Save");
        mockSaveState.setDate("2024-11-23T10:00:00");

        logger.info("Exiting GET /api/v0/save/mock with mock SaveState: {}", mockSaveState);
        return ResponseEntity.ok(mockSaveState);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaveState(@PathVariable String id) {
        logger.info("Entering DELETE /api/v0/save/{} with ID: {}", id, id);
        try {
            saveStateRepository.deleteById(id);
            logger.info("Exiting DELETE /api/v0/save/{} - delete successful", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error occurred in DELETE /api/v0/save/{}", id, e);
            return ResponseEntity.status(500).build();
        }
    }
}