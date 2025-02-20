package com.phyxels.phyxels.repository;

import com.phyxels.phyxels.model.SaveState;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SaveStateRepositoryImpl implements SaveStateRepository {

    private static final Logger logger = LoggerFactory.getLogger(SaveStateRepositoryImpl.class);

    @Autowired
    CouchDbClient couchDbClient;

    @Override
    public SaveState save(SaveState saveState) {
        logger.info("Attempting to save entity: {}", saveState);
        try {
            if (saveState.getId() != null && !saveState.getId().isEmpty()) {
                Optional<SaveState> existingSave = findById(saveState.getId());
                if (existingSave.isPresent()) {
                    logger.info("Entity with ID {} found. Updating existing entity.", saveState.getId());
                    saveState.setRev(existingSave.get().getRev());
                    org.lightcouch.Response response = couchDbClient.update(saveState);
                    saveState.setRev(response.getRev());
                    logger.info("Entity with ID {} updated successfully.", saveState.getId());
                    return saveState;
                }
            }
            org.lightcouch.Response response = couchDbClient.save(saveState);
            saveState.setId(response.getId());
            saveState.setRev(response.getRev());
            logger.info("New entity saved successfully with ID: {}", saveState.getId());
            return saveState;
        } catch (Exception e) {
            logger.error("Error occurred while saving entity: {}", saveState, e);
            throw e;
        }
    }

    @Override
    public Optional<SaveState> findById(String id) {
        logger.info("Attempting to find entity with ID: {}", id);
        try {
            Optional<SaveState> saveState = Optional.ofNullable(couchDbClient.find(SaveState.class, id));
            if (saveState.isPresent()) {
                logger.info("Entity with ID {} found: {}", id, saveState.get());
            } else {
                logger.warn("Entity with ID {} not found.", id);
            }
            return saveState;
        } catch (Exception e) {
            logger.error("Error occurred while finding entity with ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<SaveState> findAll() {
        logger.info("Attempting to retrieve all entities.");
        try {
            List<SaveState> saveStates = new ArrayList<>(couchDbClient.view("_all_docs")
                    .includeDocs(true)
                    .query(SaveState.class));
            logger.info("Retrieved {} entities.", saveStates.size());
            return saveStates;
        } catch (Exception e) {
            logger.error("Error occurred while retrieving all entities.", e);
            throw e;
        }
    }

    @Override
    public void deleteById(String id) {
        logger.info("Attempting to delete entity with ID: {}", id);
        try {
            Optional<SaveState> existingSave = findById(id);
            if (existingSave.isPresent()) {
                couchDbClient.remove(existingSave.get());
                logger.info("Entity with ID {} deleted successfully.", id);
            } else {
                logger.warn("Entity with ID {} not found for deletion.", id);
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting entity with ID: {}", id, e);
            throw e;
        }
    }
}
