package com.epam.resourceprocessor.processors;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@Slf4j
public class Mp3FileProcessor {
    public void getSong(byte[] bytes) {
        Mp3File mp3File;
        try {
            File file = new File("song.mp3");
            FileUtils.writeByteArrayToFile(file, bytes);
            mp3File = new Mp3File(file);
        } catch (InvalidDataException | UnsupportedTagException | IOException e) {
            throw new RuntimeException(e);
        }
        log.debug(mp3File.getId3v1Tag().getTitle());
    }
}
