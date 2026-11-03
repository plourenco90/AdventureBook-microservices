package com.pictet.adventureservice.domain.consequence;

import com.pictet.adventureservice.domain.model.Player;
import com.pictet.adventureservice.infrastructure.client.book.dto.ConsequenceType;
import com.pictet.adventureservice.infrastructure.client.book.dto.PlayableConsequence;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConsequenceProcessorTest {
    ConsequenceProcessor processor = new ConsequenceProcessor(List.of(new LoseHealthHandler()));

    @Test
    void nullConsequenceIsIgnored() {
        Player p = new Player("A");
        processor.apply(null, p);
        assertEquals(10, p.getHealth());
    }

    @Test
    void damageIsApplied() {
        Player p = new Player("A");
        processor.apply(new PlayableConsequence(ConsequenceType.LOSE_HEALTH, 4, "Ouch"), p);
        assertEquals(6, p.getHealth());
    }

    @Test
    void invalidDamageIsRejected() {
        Player p = new Player("A");
        assertThrows(IllegalArgumentException.class,
                () -> processor.apply(new PlayableConsequence(ConsequenceType.LOSE_HEALTH, -1, ""), p));
    }
}
