package com.casemanagement.webservice.repository.impl;

import com.casemanagement.webservice.model.Case;
import com.casemanagement.webservice.repository.CaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stand-in for a real database. Holds everything in memory, so data resets
 * every time the app restarts. Swap this out for a JPA-backed repository
 * once a real database is available.
 */
@Repository
public class CaseRepositoryImpl implements CaseRepository {

    private final Map<String, Case> store = new ConcurrentHashMap<>();

    @Override
    public Case save(Case caseRecord) {
        store.put(caseRecord.getCaseId(), caseRecord);
        return caseRecord;
    }

    @Override
    public Optional<Case> findById(String caseId) {
        return Optional.ofNullable(store.get(caseId));
    }

    @Override
    public List<Case> findAll() {
        return List.copyOf(store.values());
    }

    @Override
    public void deleteById(String caseId) {
        store.remove(caseId);
    }

    @Override
    public boolean existsById(String caseId) {
        return store.containsKey(caseId);
    }
}
