package com.casemanagement.webservice.e2e;

import com.casemanagement.webservice.model.Case;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Boots the real app on a random port and drives the actual HTTP endpoints,
 * verifying the full request/response cycle through controller, service and
 * the in-memory repository together.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CaseControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + "/cases" + path;
    }

    @Test
    void fullCrudLifecycle() {
        String caseId = UUID.randomUUID().toString();
        Case newCase = new Case(caseId, "Server down", "Prod server unreachable",
                "OPEN", "NOC Team", LocalDate.now(), false);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(url(""), newCase, String.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(createResponse.getBody()).isEqualTo("Case Created Successfully");

        Map<String, Object> fetched = getCaseData(caseId);
        assertThat(fetched.get("caseId")).isEqualTo(caseId);
        assertThat(fetched.get("status")).isEqualTo("OPEN");
        assertThat(fetched.get("flagged")).isEqualTo(false);

        ResponseEntity<Case[]> allResponse = restTemplate.getForEntity(url(""), Case[].class);
        assertThat(allResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(List.of(allResponse.getBody())).extracting(Case::getCaseId).contains(caseId);

        newCase.setStatus("IN_PROGRESS");
        newCase.setFlagged(true);
        ResponseEntity<String> updateResponse = restTemplate.exchange(url(""), HttpMethod.PUT,
                new HttpEntity<>(newCase), String.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isEqualTo("Case Updated Successfully");

        Map<String, Object> updated = getCaseData(caseId);
        assertThat(updated.get("status")).isEqualTo("IN_PROGRESS");
        assertThat(updated.get("flagged")).isEqualTo(true);

        restTemplate.delete(url("/" + caseId));

        ResponseEntity<Map> afterDelete = restTemplate.getForEntity(url("/" + caseId), Map.class);
        assertThat(afterDelete.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getUnknownCase_returns404() {
        ResponseEntity<Map> response = restTemplate.getForEntity(url("/does-not-exist"), Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUnknownCase_returns404() {
        Case ghost = new Case(UUID.randomUUID().toString(), "Ghost case", "Never created",
                "OPEN", "Nobody", LocalDate.now(), false);
        ResponseEntity<Map> response = restTemplate.exchange(url(""), HttpMethod.PUT,
                new HttpEntity<>(ghost), Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getCaseData(String caseId) {
        ResponseEntity<Map> response = restTemplate.getForEntity(url("/" + caseId), Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return (Map<String, Object>) response.getBody().get("data");
    }
}
