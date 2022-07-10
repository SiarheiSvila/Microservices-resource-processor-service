package com.epam.resourceprocessor.services;

import com.epam.resourceprocessor.entities.Song;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.metadata.XMPDM;
import org.springframework.stereotype.Component;

@Component
public class SongBuilder {
    public Song buildFromMp3File(Metadata metadata, Long resourceId) {
        return Song.builder()
                .name(metadata.get(TikaCoreProperties.TITLE))
                .album(metadata.get(XMPDM.ALBUM))
                .artist(metadata.get(XMPDM.ARTIST))
                .length((long) Double.parseDouble(metadata.get(XMPDM.DURATION)))
                .resourceId(resourceId)
                .year(metadata.get(XMPDM.RELEASE_DATE))
                .build();
    }
}
