package com.skilland.game.demo.model.gameroom.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageUserData {
    private Long memberId;
    private String fullName;
    private Integer numberOfPoints;
    private Boolean isFinished;
}
