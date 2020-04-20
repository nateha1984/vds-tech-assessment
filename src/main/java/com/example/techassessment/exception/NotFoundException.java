package com.example.techassessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.RequiredArgsConstructor;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
}