package com.br.hobbie.shared.core.errors;

public class DomainException extends RuntimeException{
    public DomainException(String msg){
        super(msg);
    }
}
