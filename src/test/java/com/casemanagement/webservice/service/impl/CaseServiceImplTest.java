package com.casemanagement.webservice.service.impl;

import com.casemanagement.webservice.exception.CaseNotFoundException;
import com.casemanagement.webservice.model.Case;
import com.casemanagement.webservice.repository.CaseRepository;
import com.casemanagement.webservice.service.CaseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CaseServiceImplTest {

    @Mock
    private CaseRepository caseRepository;
    private CaseService caseService;
    AutoCloseable autoCloseable;
    Case caseRecord;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        caseService = new CaseServiceImpl(caseRepository);
        caseRecord = new Case("1", "Laptop not booting", "Customer laptop fails to power on",
                "OPEN", "Amazon", LocalDate.now());
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testCreateCase() {
        when(caseRepository.save(any(Case.class))).thenReturn(caseRecord);
        assertThat(caseService.createCase(caseRecord)).isEqualTo("Success");
    }

    @Test
    void testCreateCase_DefaultsFlaggedToFalse() {
        Case unflagged = new Case("2", "Printer offline", "Office printer not responding",
                "OPEN", "IBM", LocalDate.now());
        when(caseRepository.save(any(Case.class))).thenReturn(unflagged);
        caseService.createCase(unflagged);
        assertThat(unflagged.isFlagged()).isFalse();
    }

    @Test
    void testUpdateCase_CanSetFlagged() {
        caseRecord.setFlagged(true);
        when(caseRepository.existsById("1")).thenReturn(true);
        when(caseRepository.save(caseRecord)).thenReturn(caseRecord);
        caseService.updateCase(caseRecord);
        assertThat(caseRecord.isFlagged()).isTrue();
    }

    @Test
    void testUpdateCase_Success() {
        when(caseRepository.existsById("1")).thenReturn(true);
        when(caseRepository.save(caseRecord)).thenReturn(caseRecord);
        assertThat(caseService.updateCase(caseRecord)).isEqualTo("Update Success");
    }

    @Test
    void testUpdateCase_NotFound() {
        when(caseRepository.existsById("1")).thenReturn(false);
        assertThatThrownBy(() -> caseService.updateCase(caseRecord))
                .isInstanceOf(CaseNotFoundException.class);
    }

    @Test
    void testDeleteCase_Success() {
        when(caseRepository.existsById("1")).thenReturn(true);
        assertThat(caseService.deleteCase("1")).isEqualTo("Deleted Success");
    }

    @Test
    void testDeleteCase_NotFound() {
        when(caseRepository.existsById("1")).thenReturn(false);
        assertThatThrownBy(() -> caseService.deleteCase("1"))
                .isInstanceOf(CaseNotFoundException.class);
    }

    @Test
    void testGetCase_Found() {
        when(caseRepository.findById("1")).thenReturn(Optional.of(caseRecord));
        assertThat(caseService.getCase("1")).isEqualTo(caseRecord);
    }

    @Test
    void testGetCase_NotFound() {
        when(caseRepository.findById("1")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> caseService.getCase("1"))
                .isInstanceOf(CaseNotFoundException.class);
    }

    @Test
    void testGetAllCases() {
        when(caseRepository.findAll()).thenReturn(List.of(caseRecord));
        assertThat(caseService.getAllCases()).hasSize(1);
    }
}
