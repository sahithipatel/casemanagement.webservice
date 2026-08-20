package com.casemanagement.webservice.service.impl;

import com.casemanagement.webservice.exception.CaseNotFoundException;
import com.casemanagement.webservice.model.Case;
import com.casemanagement.webservice.repository.CaseRepository;
import com.casemanagement.webservice.service.CaseService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CaseServiceImpl implements CaseService {

    private final CaseRepository caseRepository;

    public CaseServiceImpl(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    @Override
    public String createCase(Case caseRecord) {
        if (caseRecord.getCaseId() == null || caseRecord.getCaseId().isBlank()) {
            caseRecord.setCaseId(UUID.randomUUID().toString());
        }
        if (caseRecord.getCreatedDate() == null) {
            caseRecord.setCreatedDate(LocalDate.now());
        }
        caseRepository.save(caseRecord);
        return "Success";
    }

    @Override
    public String updateCase(Case caseRecord) {
        if (!caseRepository.existsById(caseRecord.getCaseId())) {
            throw new CaseNotFoundException("Requested case does not exist");
        }
        caseRepository.save(caseRecord);
        return "Update Success";
    }

    @Override
    public String deleteCase(String caseId) {
        if (!caseRepository.existsById(caseId)) {
            throw new CaseNotFoundException("Requested case does not exist");
        }
        caseRepository.deleteById(caseId);
        return "Deleted Success";
    }

    @Override
    public Case getCase(String caseId) {
        return caseRepository.findById(caseId)
                .orElseThrow(() -> new CaseNotFoundException("Requested case does not exist"));
    }

    @Override
    public List<Case> getAllCases() {
        return caseRepository.findAll();
    }
}
