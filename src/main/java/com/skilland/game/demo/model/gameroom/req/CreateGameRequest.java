package com.skilland.game.demo.model.gameroom.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGameRequest {

    private String gameName;
    private String subjectName;
    private String courseName;
    private String [] topicNames;

    @Min(1)
    private int minTaskLevel;
    private int maxTaskLevel;
    @Min(1)
    private int GameDurabilityMinutes;

    @Pattern(regexp = "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")
    private String startDate;

}
