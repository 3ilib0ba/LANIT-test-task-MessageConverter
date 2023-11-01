package com.example.MessageConverter.service;

import com.example.MessageConverter.entity.AuthorPost;
import com.example.MessageConverter.entity.LetterAuthor;
import com.example.MessageConverter.entity.Race;
import com.example.MessageConverter.exception.InputFieldErrorException;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class XmlDocumentService {
    private final DocumentBuilderFactory factory;
    private final DocumentBuilder builder;

    private final RaceService raceService;
    private final LetterAuthorService letterAuthorService;
    private final AuthorPostService authorPostService;


    @SneakyThrows
    public XmlDocumentService(
            RaceService raceService,
            LetterAuthorService letterAuthorService,
            AuthorPostService authorPostService
    ) {
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();

        this.authorPostService = authorPostService;
        this.letterAuthorService = letterAuthorService;
        this.raceService = raceService;
    }

    public Document createDocument(String message) {
        try {
            // return Document which parsed from message(structure like DOM)
            return builder.parse(new InputSource(new StringReader(message)));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> convertXmlToLetterToAliensParams(Document document) {
        Map<String, Object> paramsInTemplate = new HashMap<>();

        Map<String, Object> title = convertTitle(document);
        paramsInTemplate.put("title", title);

        String alienCode = (String) title.get("alien_code");
        paramsInTemplate.put("message", extractMessage(document, alienCode));

        paramsInTemplate.put("contacts", extractAndGenerateContacts(document));

        paramsInTemplate.put("sentTime", getCurrentDateWithFormat());

        return paramsInTemplate;
    }

    private Map<String, Object> convertTitle(Document document) {
        Map<String, Object> titleParams = new HashMap<>();

        String raceValue = extractRaceValue(document);
        Race race = raceService.getRaceByValueTag(raceValue);

        titleParams.put("description", race.getToDescription());
        titleParams.put("theme_text", race.getToTitle());
        titleParams.put("alien_code", race.getValueTag());

        String createdDateTime = extractCreatedDateTime(document);
        String changedCreatedTime = updateDate(createdDateTime);
        titleParams.put("created", changedCreatedTime);

        titleParams.put("id", extractTitleId(document));

        titleParams.put("authors", extractAuthors(document));

        return titleParams;
    }

    private String extractRaceValue(Document document) {
        NodeList raceElements = document.getElementsByTagName("код_расы");
        if (raceElements.getLength() != 1) {
            throw new InputFieldErrorException("<код_расы>...</код_расы> должен быть представлен единожды в письме");
        }

        Element raceElement = (Element) raceElements.item(0);

        if (raceElement.getElementsByTagName("value").getLength() != 1) {
            throw new InputFieldErrorException("<код_расы>...</код_расы> должен иметь единственный тег <value>...</value>");
        }

        return raceElement.getElementsByTagName("value").item(0).getTextContent();
    }

    private String extractCreatedDateTime(Document document) {
        NodeList isCreated = document.getElementsByTagName("created");
        if (isCreated.getLength() != 1) {
            throw new InputFieldErrorException("<created>...</created> должен быть представлен единожды в письме");
        }

        Element created = (Element) isCreated.item(0);

        if (!created.hasAttribute("date_time")) {
            throw new InputFieldErrorException("<created ... /> должен содержать единственный атрибут date_time");
        }
        return created.getAttribute("date_time");
    }

    private String updateDate(String createdDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = sdf.parse(createdDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DAY_OF_MONTH, 1);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm");
            Date result = calendar.getTime();
            return outputFormat.format(result);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> extractTitleId(Document document) {
        Map<String, Object> titleIdParams = new HashMap<>();

        titleIdParams.put("description", "Идентификатор письма");
        titleIdParams.put("value", extractUidCodeValue(document));

        return titleIdParams;
    }

    private String extractUidCodeValue(Document document) {
        NodeList isUid = document.getElementsByTagName("uid");
        if (isUid.getLength() != 1) {
            throw new InputFieldErrorException("<uid>...</uid> должен быть представлен единожды в письме");
        }
        Element titleId = (Element) isUid.item(0);

        NodeList isCode = titleId.getElementsByTagName("code");
        if (isCode.getLength() != 1) {
            throw new InputFieldErrorException("<uid><code>...</code></uid> должен быть представлен единожды в письме");
        }
        Element code = (Element) isCode.item(0);

        NodeList isValue = code.getElementsByTagName("value");
        if (isValue.getLength() != 1) {
            throw new InputFieldErrorException("<uid><code><value>...</value></code></uid> должен быть представлен единожды в письме");
        }
        Element value = (Element) isValue.item(0);

        return value.getTextContent();
    }

    private List<Object> extractAuthors(Document document) {
        List<Object> authorsWithParams = new ArrayList<>();

        NodeList authors = document.getElementsByTagName("author");
        if (authors.getLength() < 1) {
            throw new InputFieldErrorException("<author>...</author> должен быть хотя бы один в письме");
        }
        for (int i = 0; i < authors.getLength(); i++) {
            authorsWithParams.add(extractOneAuthorByItNode(authors.item(i)));
        }

        return authorsWithParams;
    }

    private Map<String, Object> extractOneAuthorByItNode(Node authorNode) {
        Map<String, Object> authorWithParams = new HashMap<>();

        NodeList isId = ((Element) authorNode).getElementsByTagName("id");
        if (isId.getLength() != 1) {
            throw new InputFieldErrorException("<author><id>...</id></author> должен быть представлен единожды для каждого автора");
        }
        Element id = (Element) isId.item(0);

        NodeList isValue = id.getElementsByTagName("value");
        if (isValue.getLength() != 1) {
            throw new InputFieldErrorException("<author><id><value>...<value></id></author> должен быть представлен единожды для каждого автора");
        }
        Element value = (Element) isValue.item(0);
        String authorId = value.getTextContent();

        LetterAuthor author = letterAuthorService.getAuthorByAuthorId(authorId);
        authorWithParams.put("name", author.getName());
        authorWithParams.put("fatherName", author.getFathername());
        authorWithParams.put("lastName", author.getLastname());

        AuthorPost position = authorPostService.getPositionByAuthorId(author.getPost());
        authorWithParams.put("position", position.getDescription());

        return authorWithParams;
    }

    private List<Object> extractMessage(Document document, String alienCode) {
        List<Object> messageWithParams = new ArrayList<>();

        String allText = extractAllMessageText(document);
        String[] paragraphs = allText.split("\n");
        for (String paragraph : paragraphs) {
            if (paragraph.equals("")) {
                // skip blank paragraph getting at start of letter
                continue;
            }
            messageWithParams.add(extractForOneParagraph(paragraph, alienCode));
        }
        String externalParagraph = "Надеюсь, это поможет Вам. Если у Вас" +
                " есть какие-либо дополнительные" +
                " вопросы, пожалуйста, не стесняйтесь" +
                " спрашивать. С уважением, Земляне!";
        messageWithParams.add(extractForOneParagraph(externalParagraph, alienCode));

        return messageWithParams;
    }

    private String extractAllMessageText(Document document) {
        NodeList documentTags = document.getElementsByTagName("document");
        if (documentTags.getLength() != 1) {
            throw new InputFieldErrorException("<document>...</document> должен быть представлен единожды в письме");
        }

        Element documentTag = (Element) documentTags.item(0);
        if (documentTag.getElementsByTagName("text").getLength() != 1) {
            throw new InputFieldErrorException("<document><text>...</text></document> должен иметь единственный тег <value>...</value>");
        }

        return documentTag.getElementsByTagName("text").item(0).getTextContent();
    }

    private Map<String, Object> extractForOneParagraph(String paragraphText, String alienCode) {
        Map<String, Object> paragraph = new HashMap<>();

        switch (alienCode) {
            case "A1" -> paragraphText = paragraphText.replaceAll("Здравствуйте", "こんにちは");
            case "A2" -> paragraphText = paragraphText.replaceAll("Здравствуйте", "Dif-tor heh smusma");
            case "A3" -> paragraphText = paragraphText.replaceAll("Здравствуйте", "안녕하세요");
        }
        paragraph.put("text", paragraphText);

        return paragraph;
    }

    private Map<String, Object> extractAndGenerateContacts(Document document) {
        Map<String, Object> contactsWithParams = new HashMap<>();

        NodeList documentTags = document.getElementsByTagName("document");
        if (documentTags.getLength() != 1) {
            throw new InputFieldErrorException("<document>...</document> должен быть представлен единожды в письме");
        }
        Element documentTag = (Element) documentTags.item(0);

        NodeList isTel = documentTag.getElementsByTagName("tel");
        if (isTel.getLength() > 1) {
            throw new InputFieldErrorException("<document><tel>...</tel></document> должен быть представлен <= 1 раза в письме");
        }
        Element tel = (Element) isTel.item(0);
        if (tel != null) {
            NodeList telValue = tel.getElementsByTagName("value");
            if (telValue.getLength() != 1) {
                throw new InputFieldErrorException("<document><tel><value>...</value></tel></document> должен быть представлен 1 раз в письме");
            }

            String telephone = telValue.item(0).getTextContent();
            contactsWithParams.put("is_tel_number", true);
            contactsWithParams.put("tel_number", telephone.replaceAll("[^0-9]", ""));
        } else {
            contactsWithParams.put("is_tel_number", false);
        }

        NodeList isAddress = documentTag.getElementsByTagName("address");
        if (isAddress.getLength() > 1) {
            throw new InputFieldErrorException("<document><address>...</address></document> должен быть представлен <= 1 раза в письме");
        }
        Element address = (Element) isAddress.item(0);
        if (address != null) {
            NodeList addressValue = address.getElementsByTagName("value");
            if (addressValue.getLength() != 1) {
                throw new InputFieldErrorException("<document><address><value>...</value></address></document> должен быть представлен 1 раз в письме");
            }

            String addressDescription = addressValue.item(0).getTextContent();
            contactsWithParams.put("is_address_description", true);
            contactsWithParams.put("address_description", addressDescription);
        } else {
            contactsWithParams.put("is_address_description", false);
        }

        return contactsWithParams;
    }

    private String getCurrentDateWithFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date());
    }
}
