package com.skilland.game.demo.model.gameroom.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HelloMessage {
    private Long memberId;
    private Long gameId;
}
