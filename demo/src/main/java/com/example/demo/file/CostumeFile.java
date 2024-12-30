package com.example.demo.file;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;


@Data


public class CostumeFile {
    public static int counter=0;
    private String path;
    private  int id;
    private String extension;
    public CostumeFile(){

        CostumeFile.counter++;
        this.id=CostumeFile.counter;


    }

    public void setPath(String path) {
        this.path = path+ File.separator+String.valueOf(this.id);;
    }

    public String getPath(){
        return this.path+"."+this.extension;
    }


}
