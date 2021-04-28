package com.skilland.game.demo.model.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ANDM")
public class AdminEntity extends GameUserEntity {
}
