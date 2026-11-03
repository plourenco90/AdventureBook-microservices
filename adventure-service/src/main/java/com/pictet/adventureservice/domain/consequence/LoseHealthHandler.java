package com.pictet.adventureservice.domain.consequence;

import com.pictet.adventureservice.infrastructure.client.book.dto.ConsequenceType;
import com.pictet.adventureservice.infrastructure.client.book.dto.PlayableConsequence;
import com.pictet.adventureservice.domain.model.Player;

import org.springframework.stereotype.Component;

@Component
public class LoseHealthHandler implements ConsequenceHandler {

    @Override
    public ConsequenceType supportedType() {
        return ConsequenceType.LOSE_HEALTH;
    }

    @Override
    public void apply(PlayableConsequence consequence, Player player) {
        player.loseHealth(consequence.value());
    }
}
