package io.springbatch.springbatch.controller;

import io.springbatch.springbatch.dto.SignUpRequest;
import io.springbatch.springbatch.service.SignUpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignController {

    private final SignUpService signUpService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest request) {
        signUpService.signUp(request.getName(), request.getEmail());

        return ResponseEntity.ok("생성완료");
    }

}
