package com.edu.fud.projectfootbalpitch.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.edu.fud.projectfootbalpitch.dto.UserDto;
import com.edu.fud.projectfootbalpitch.service.UserService;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class GoogleUtils {
    @Autowired
    private Environment env;
    @Autowired
    private UserService userService;
    public String getToken(final String code,String type) throws ClientProtocolException, IOException {
        String link = env.getProperty("google.link.get.token");
        String response = Request.Post(link)
                .bodyForm(Form.form().add("client_id", env.getProperty("google.app.id"))
                        .add("scope",env.getProperty("google.link.get.scope")).add("type",type)
                        .add("client_secret", env.getProperty("google.app.secret"))
                        .add("redirect_uri", env.getProperty("google.redirect.uri")).add("code", code)
                        .add("grant_type", "authorization_code").build())
                .execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(response).get("access_token");
        return node.textValue();
    }
    public GooglePojo getUserInfo(final String accessToken) throws ClientProtocolException, IOException {
        String link = env.getProperty("google.link.get.user_info") + accessToken;
        System.out.println("accesstoken"+accessToken);
        String response = Request.Get(link).execute().returnContent().asString();
        ObjectMapper mapper = new ObjectMapper();
        GooglePojo googlePojo = mapper.readValue(response, GooglePojo.class);
        System.out.println("GivenName:"+googlePojo.getGiven_name()+"FamilyName:"+googlePojo.getFamily_name());
        System.out.println(googlePojo.toString());
        return googlePojo;
    }
    public UserDetails buildUser(GooglePojo googlePojo) {
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        if (!userService.getUserByUsername(googlePojo.getEmail()).isPresent()){
            UserDto userDto=new UserDto();
            System.out.println("GivenName"+googlePojo.getGiven_name());
            String fullName=googlePojo.getGiven_name();
//            String address="12456151651";
//            String phone="1156156156165";
            userDto.setUserName(googlePojo.getEmail());
            userDto.setGmail(googlePojo.getEmail());
            userDto.setAddress("");
            userDto.setPassword("123456");
            userDto.setPhone("");
            userDto.setImage("default.png");
            userDto.setFullName(fullName);
            userService.save(userDto);
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetail = new User(googlePojo.getEmail(),
                "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);

        return userDetail;
    }
}
