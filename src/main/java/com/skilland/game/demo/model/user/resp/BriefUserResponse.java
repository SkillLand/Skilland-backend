package com.skilland.game.demo.model.user.resp;

import com.skilland.game.demo.model.user.GameUserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BriefUserResponse {

    private String email;
    private String userName;

    public static BriefUserResponse fromUserEntity(GameUserEntity userEntity){
        return new BriefUserResponse(userEntity.getEmail(), userEntity.getFirstName());
    }
}
