package com.skilland.game.demo.model.gameroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TopicLevel {
    private String topicName;
    private String topicLevel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopicLevel that = (TopicLevel) o;
        return topicName.equals(that.topicName) && topicLevel.equals(that.topicLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(topicName, topicLevel);
    }
}
