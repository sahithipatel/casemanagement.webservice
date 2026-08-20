package com.casemanagement.webservice.service;

import com.casemanagement.webservice.model.Case;

import java.util.List;

public interface CaseService {
    String createCase(Case caseRecord);
    String updateCase(Case caseRecord);
    String deleteCase(String caseId);
    Case getCase(String caseId);
    List<Case> getAllCases();
}
