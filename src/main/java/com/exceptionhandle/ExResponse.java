package com.exceptionhandle;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class ExResponse {

    private LocalDate timestamp;
    private String message;

}
