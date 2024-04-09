package com.example.restApi.domain.member.dto;

import com.example.restApi.domain.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberDto {

    private Long id;

    private String username;
    private String email;

    private LocalDateTime createDate;
    private LocalDateTime modifiedDate;

    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
        this.email = member.getEmail();
        this.createDate = member.getCreateDate();
        this.modifiedDate = member.getModifiedDate();
    }

}
