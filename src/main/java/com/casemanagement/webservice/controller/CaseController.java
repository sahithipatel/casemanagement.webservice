package com.casemanagement.webservice.controller;

import com.casemanagement.webservice.model.Case;
import com.casemanagement.webservice.response.ResponseHandler;
import com.casemanagement.webservice.service.CaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    // Read specific case details
    @GetMapping("{caseId}")
    public ResponseEntity<Object> getCaseDetails(@PathVariable("caseId") String caseId) {
        return ResponseHandler.responseBuilder("Requested case details are given here",
                HttpStatus.OK, caseService.getCase(caseId));
    }

    // Read all cases
    @GetMapping
    public List<Case> getAllCaseDetails() {
        return caseService.getAllCases();
    }

    @PostMapping
    public String createCaseDetails(@RequestBody Case caseRecord) {
        caseService.createCase(caseRecord);
        return "Case Created Successfully";
    }

    @PutMapping
    public String updateCaseDetails(@RequestBody Case caseRecord) {
        caseService.updateCase(caseRecord);
        return "Case Updated Successfully";
    }

    @DeleteMapping("{caseId}")
    public String deleteCaseDetails(@PathVariable("caseId") String caseId) {
        caseService.deleteCase(caseId);
        return "Case Deleted Successfully";
    }
}
