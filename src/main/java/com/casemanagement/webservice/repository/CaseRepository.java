package com.casemanagement.webservice.repository;

import com.casemanagement.webservice.model.Case;

import java.util.List;
import java.util.Optional;

public interface CaseRepository {
    Case save(Case caseRecord);
    Optional<Case> findById(String caseId);
    List<Case> findAll();
    void deleteById(String caseId);
    boolean existsById(String caseId);
}
