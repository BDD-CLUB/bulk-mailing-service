package io.springbatch.springbatch.bdd.email.controller;

import io.springbatch.springbatch.bdd.email.dto.request.MonthRepostRequest;
import io.springbatch.springbatch.bdd.email.service.MonthMailingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailingController {

    private final MonthMailingService mailingService;

    @PostMapping("/month-report")
    public ResponseEntity<Void> reportMonthSummary(@RequestBody @Valid MonthRepostRequest request) throws Exception {
        mailingService.runMailingBatch(request.getJobName(), request.getSubject(),
                request.getReportMessage(), request.getPassword());
        return ResponseEntity.noContent().build();
    }
}
