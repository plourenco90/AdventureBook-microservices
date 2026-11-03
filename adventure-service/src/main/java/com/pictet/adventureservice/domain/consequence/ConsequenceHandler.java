package com.pictet.adventureservice.domain.consequence;

import com.pictet.adventureservice.infrastructure.client.book.dto.ConsequenceType;
import com.pictet.adventureservice.infrastructure.client.book.dto.PlayableConsequence;
import com.pictet.adventureservice.domain.model.Player;


public interface ConsequenceHandler {

    ConsequenceType supportedType();

    void apply(PlayableConsequence consequence, Player player);
}
