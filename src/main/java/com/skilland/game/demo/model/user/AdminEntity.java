package com.skilland.game.demo.model.user;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("METH")
public class AdminEntity extends GameUserDAO{
}
