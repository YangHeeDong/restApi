package com.example.restApi.domain.article.entity;

import com.example.restApi.domain.member.entity.Member;
import com.example.restApi.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Article extends BaseEntity {
    @ManyToOne
    private Member author;
    private String title;
    private String content;

}
