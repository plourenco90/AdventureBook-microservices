package com.pictet.adventureservice.domain.consequence;


import com.pictet.adventureservice.infrastructure.client.book.dto.ConsequenceType;
import com.pictet.adventureservice.infrastructure.client.book.dto.PlayableConsequence;
import com.pictet.adventureservice.domain.exception.UnsupportedConsequenceException;
import com.pictet.adventureservice.domain.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ConsequenceProcessor {

    private final Map<ConsequenceType, ConsequenceHandler> handlers;

    public ConsequenceProcessor(List<ConsequenceHandler> consequenceHandlers) {
        this.handlers = consequenceHandlers.stream()
                .collect(Collectors.toMap(ConsequenceHandler::supportedType, Function.identity()));
    }

    public void apply(PlayableConsequence consequence, Player player) {
        if (consequence == null) {
            return;
        }
        ConsequenceHandler handler = handlers.get(consequence.type());
        if (handler == null) {
            throw new UnsupportedConsequenceException(consequence.type());
        }
        handler.apply(consequence, player);
    }
}
