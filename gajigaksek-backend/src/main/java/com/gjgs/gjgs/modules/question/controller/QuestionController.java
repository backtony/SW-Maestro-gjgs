package com.gjgs.gjgs.modules.question.controller;

import com.gjgs.gjgs.modules.question.dto.AnswerRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionDetailResponse;
import com.gjgs.gjgs.modules.question.dto.QuestionRequest;
import com.gjgs.gjgs.modules.question.dto.QuestionResponse;
import com.gjgs.gjgs.modules.question.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/questions")
public class QuestionController {

    private final QuestionService questionService;

    @PreAuthorize("hasAnyRole('USER,DIRECTOR')")
    @PostMapping("")
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody @Validated QuestionRequest request) {
        return new ResponseEntity<>(questionService.createQuestion(request), HttpStatus.OK);
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionDetailResponse> getQuestion(@PathVariable Long questionId) {
        return new ResponseEntity<>(questionService.getQuestion(questionId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('DIRECTOR')")
    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> putAnswer(@PathVariable Long questionId,
                                                      @RequestBody @Validated AnswerRequest request) {
        return new ResponseEntity<>(questionService.putAnswer(questionId, request), HttpStatus.OK);
    }
}
