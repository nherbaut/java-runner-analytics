package fr.pantheonsorbonne.ufr27.miage.service.impl;

public class ForbiddenClassException extends RuntimeException {
    public ForbiddenClassException(String str){
        super(str);
    }
}
