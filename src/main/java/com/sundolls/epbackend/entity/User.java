package com.sundolls.epbackend.entity;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "user_tb")
@NoArgsConstructor
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @Column(name = "UID")
    private String uid;

    @Column(name = "NICKNAME")
    private String nickName;

    @Column(name="SCHOOL_NAME")
    private String schoolName;

    @Column(name = "EMAIL")
    private String email;

    @Builder
    public User(String uid, String email,String nickname, String school){
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return nickName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
