package com.skilland.game.demo.config.security.properties;

import com.skilland.game.demo.config.security.properties.AdminProperties;
import com.skilland.game.demo.config.security.properties.JWTProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Validated
@ConfigurationProperties(prefix = "card-checking.security")
public class SecurityProperties {

    @Valid
    @NestedConfigurationProperty
    private JWTProperties jwt;

    public JWTProperties getJwt() {
        return jwt;
    }

    public void setJwt(JWTProperties jwt) {
        this.jwt = jwt;
    }

    private Map<@NotBlank String, @Valid AdminProperties> admins;

    public void setAdmins(Map<String, AdminProperties> admins) {
        this.admins = admins;
    }

    public Map<String, AdminProperties> getAdmins() {
        return admins;
    }



}

