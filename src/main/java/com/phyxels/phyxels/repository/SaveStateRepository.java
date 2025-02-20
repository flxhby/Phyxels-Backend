package com.phyxels.phyxels.repository;

import com.phyxels.phyxels.model.SaveState;

import java.util.List;
import java.util.Optional;

public interface SaveStateRepository {
    SaveState save(SaveState saveState);
    Optional<SaveState> findById(String id);
    List<SaveState> findAll();
    void deleteById(String id);
}
