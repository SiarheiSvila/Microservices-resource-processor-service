package com.epam.resourceprocessor.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
@Slf4j
public class Mp3FileProcessor {
    public Metadata getSongMetadata(byte[] bytes) {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();

        ParseContext parseContext = new ParseContext();
        Mp3Parser mp3Parser = new Mp3Parser();
        try {
            mp3Parser.parse(new ByteArrayInputStream(bytes), handler, metadata, parseContext);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException(e);
        }
        return metadata;
    }
}
