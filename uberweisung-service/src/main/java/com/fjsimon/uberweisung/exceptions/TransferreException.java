package com.fjsimon.uberweisung.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferreException extends Exception{

    private int errorCode;

}