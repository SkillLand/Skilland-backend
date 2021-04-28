package com.skilland.game.demo.model.user.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SaveUserRequest {

    @Email
    @NotNull
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String authority;

    public SaveUserRequest() {
    }

    public SaveUserRequest(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.firstName = nickname;
    }

}
