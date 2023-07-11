package com.bamboovir.litcode.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question {
    public String id;
    public String title;
    public String description;
    public String code_content;
    public String hint;
    public String difficulty;
    public int likes;
    public int dislikes;
}
