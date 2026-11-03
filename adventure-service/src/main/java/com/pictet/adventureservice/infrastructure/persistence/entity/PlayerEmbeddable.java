package com.pictet.adventureservice.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlayerEmbeddable {

    private String name;

    private int health;
}
