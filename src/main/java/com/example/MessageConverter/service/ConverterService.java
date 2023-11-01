package com.example.MessageConverter.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.w3c.dom.Document;

import java.util.*;

@Service
public class ConverterService {
    @Autowired
    private SpringTemplateEngine engine;
    private final XmlDocumentService xmlDocumentService;

    public ConverterService(
            XmlDocumentService xmlDocumentService
    ) {
        this.xmlDocumentService = xmlDocumentService;
    }

    @SneakyThrows
    public String convertMessage(String xmlMessage) {
        Document document = xmlDocumentService.createDocument(xmlMessage);
        Map<String, Object> answerLetterMapOfParams = xmlDocumentService.convertXmlToLetterToAliensParams(document);

        Context context = new Context();
        context.setVariable("letter", answerLetterMapOfParams);

        String content = engine.process("formatted_letter", context);

        return content;
    }

}
