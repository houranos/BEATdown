package com.houranos.beatdown;

/**
 * Created by rei on 3/12/18.
 */

public class Song {
    private String name;
    private String path;
    public Song(String name, String path){
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
