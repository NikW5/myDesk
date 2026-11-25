package com.psyduck.myDesk.persistenz;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageService {

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

//    public String get(String key) {
//        return messageSource.getMessage(
//                key,
//                null,
//                key,
//                Locale.GERMAN
//        );
//    }

    public String get(String key) {

        System.out.println("!!! MESSAGESERVICE WIRD AUFGERUFEN !!!");
        System.out.println("Key: " + key);

        String ergebnis = messageSource.getMessage(
                key,
                null,
                key,
                Locale.GERMAN
        );

        System.out.println("Ergebnis: " + ergebnis);

        return ergebnis;
    }
    
    public String get(String key, Object... args) {
        return messageSource.getMessage(
                key,
                args,
                key,
                Locale.GERMAN
        );
    }
}