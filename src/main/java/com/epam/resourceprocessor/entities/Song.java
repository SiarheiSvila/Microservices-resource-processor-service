package com.epam.resourceprocessor.entities;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Song implements Serializable {
    private String name;
    private String artist;
    private String album;
    private long length;
    private long resourceId;
    private String year;
}
