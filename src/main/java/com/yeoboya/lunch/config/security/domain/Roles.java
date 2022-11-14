package com.yeoboya.lunch.config.security.domain;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.config.security.constants.Authority;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLES_ID", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Authority role;

    @Builder
    public Roles(Authority role) {
        this.role = role;
    }
}