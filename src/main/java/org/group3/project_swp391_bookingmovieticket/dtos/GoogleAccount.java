package org.group3.project_swp391_bookingmovieticket.dtos;

import lombok.Data;

@Data
public class GoogleAccount {

    private String id, email, name, first_name, given_name, family_name, picture;

    private boolean verified_email;

    public GoogleAccount() {
    }

    public GoogleAccount(String id, String email, String name, String first_name, String given_name, String family_name, String picture, boolean verified_email) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.first_name = first_name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.verified_email = verified_email;
    }
}
