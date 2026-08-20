package com.casemanagement.webservice.repository.impl;

import com.casemanagement.webservice.model.Case;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CaseRepositoryImplTest {

    private CaseRepositoryImpl caseRepository;
    private Case caseRecord;

    @BeforeEach
    void setUp() {
        caseRepository = new CaseRepositoryImpl();
        caseRecord = new Case("1", "Laptop not booting", "Customer laptop fails to power on",
                "OPEN", "Amazon", LocalDate.now());
        caseRepository.save(caseRecord);
    }

    @Test
    void testFindById_Found() {
        assertThat(caseRepository.findById("1")).isPresent();
        assertThat(caseRepository.findById("1").get().getTitle()).isEqualTo(caseRecord.getTitle());
    }

    @Test
    void testFindById_NotFound() {
        assertThat(caseRepository.findById("does-not-exist")).isEmpty();
    }

    @Test
    void testFindAll() {
        List<Case> cases = caseRepository.findAll();
        assertThat(cases).hasSize(1);
    }

    @Test
    void testDeleteById() {
        caseRepository.deleteById("1");
        assertThat(caseRepository.existsById("1")).isFalse();
    }
}
