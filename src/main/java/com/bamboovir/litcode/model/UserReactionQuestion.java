package com.zonglinpeng.litcode.model;


import lombok.Data;

@Data
public class UserReactionQuestion {
    public int user_id;
    public int question_id;
    public String reaction_type;
}
