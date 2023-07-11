package com.bamboovir.litcode.model;

import lombok.Data;

@Data
public class GoogleUserInfo {
    public String id;
    public String email;
    public boolean verified_email;
    public String name;
    public String given_name;
    public String family_name;
    public String picture;
    public String locale;

    public UserProfile toUserProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.id = id;
        userProfile.avatar = picture;
        userProfile.email = email;
        userProfile.name = name;
        return userProfile;
    }
}
