package com.pictet.adventureservice.application.service;

import com.pictet.adventureservice.application.dto.AdventureStateResponse;
import com.pictet.adventureservice.application.dto.MakeChoiceRequest;
import com.pictet.adventureservice.application.dto.StartAdventureRequest;
import com.pictet.adventureservice.application.mapper.AdventureResponseMapper;
import com.pictet.adventureservice.domain.consequence.ConsequenceProcessor;
import com.pictet.adventureservice.domain.consequence.LoseHealthHandler;
import com.pictet.adventureservice.domain.exception.AdventureAlreadyFinishedException;
import com.pictet.adventureservice.domain.exception.AdventureNotFoundException;
import com.pictet.adventureservice.domain.exception.InvalidOptionException;
import com.pictet.adventureservice.domain.exception.SectionNotFoundException;
import com.pictet.adventureservice.domain.model.Adventure;
import com.pictet.adventureservice.domain.model.AdventureStatus;
import com.pictet.adventureservice.domain.model.Player;
import com.pictet.adventureservice.infrastructure.client.book.BookClient;
import com.pictet.adventureservice.infrastructure.client.book.dto.BookDetailsResponse;
import com.pictet.adventureservice.infrastructure.client.book.dto.BookPlayableResponse;
import com.pictet.adventureservice.infrastructure.client.book.exception.InvalidBookException;
import com.pictet.adventureservice.infrastructure.client.book.mapper.PlayableBookMapper;
import com.pictet.adventureservice.infrastructure.persistence.entity.AdventureEntity;
import com.pictet.adventureservice.infrastructure.persistence.mapper.AdventurePersistenceMapper;
import com.pictet.adventureservice.infrastructure.persistence.repository.AdventureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdventureServiceTest {
    @Mock
    BookClient bookClient;
    @Mock
    AdventureRepository repository;
    AdventureService service;
    AdventurePersistenceMapper persistence = Mappers.getMapper(AdventurePersistenceMapper.class);

    @BeforeEach
    void setup() {
        service = new AdventureService(bookClient, Mappers.getMapper(PlayableBookMapper.class), repository,
                new ConsequenceProcessor(List.of(new LoseHealthHandler())),
                Mappers.getMapper(AdventureResponseMapper.class), persistence);
    }

    private BookPlayableResponse book(boolean valid) {
        return new BookPlayableResponse(valid, new BookDetailsResponse(1L, "Demo", "Author", List.of(
                new BookDetailsResponse.SectionResponse(1, "Start", BookDetailsResponse.SectionType.BEGIN, List.of(
                        new BookDetailsResponse.OptionResponse("Safe", 0, 2, null),
                        new BookDetailsResponse.OptionResponse("Fatal", 1, 2,
                                new BookDetailsResponse.ConsequenceResponse(BookDetailsResponse.ConsequenceType.LOSE_HEALTH,
                                        10,
                                        "Fatal damage")))),
                new BookDetailsResponse.SectionResponse(2, "Finish", BookDetailsResponse.SectionType.END, List.of()))));
    }

    private AdventureEntity stored() {
        AdventureEntity e = persistence.toEntity(new Adventure(7L, 1L, new Player("Ana"), 1));
        e.setId(7L);
        return e;
    }

    @Test
    void startCreatesActiveAdventure() {
        when(bookClient.getPlayableBook(1L)).thenReturn(book(true));
        when(repository.save(any())).thenAnswer(inv -> {
            AdventureEntity e = inv.getArgument(0);
            e.setId(7L);
            return e;
        });
        AdventureStateResponse result = service.start(new StartAdventureRequest(1L, "Ana"));
        assertEquals(7L, result.id());
        assertEquals(10, result.health());
        assertEquals(AdventureStatus.ACTIVE, result.status());
        assertEquals(1, result.currentSection().id());
        verify(repository).save(any());
    }

    @Test
    void invalidBookCannotStart() {
        when(bookClient.getPlayableBook(1L)).thenReturn(book(false));
        assertThrows(InvalidBookException.class, () -> service.start(new StartAdventureRequest(1L, "Ana")));
        verify(repository, never()).save(any());
    }

    @Test
    void getStateReturnsStoredProgress() {
        when(repository.findById(7L)).thenReturn(Optional.of(stored()));
        when(bookClient.getPlayableBook(1L)).thenReturn(book(true));
        assertEquals(1, service.getState(7L).currentSection().id());
    }

    @Test
    void missingAdventureThrows() {
        when(repository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(AdventureNotFoundException.class, () -> service.getState(7L));
    }

    @Test
    void choiceCompletesAdventureAndUpdatesEntity() {
        AdventureEntity entity = stored();
        when(repository.findById(7L)).thenReturn(Optional.of(entity));
        when(bookClient.getPlayableBook(1L)).thenReturn(book(true));
        AdventureStateResponse result = service.makeChoice(7L, new MakeChoiceRequest(0));
        assertEquals(AdventureStatus.COMPLETED, result.status());
        assertEquals(2, result.currentSection().id());
        assertEquals(AdventureStatus.COMPLETED, entity.getStatus());
        assertEquals(2, entity.getCurrentSectionId());
    }

    @Test
    void fatalChoiceKillsPlayerWithoutMoving() {
        AdventureEntity entity = stored();
        when(repository.findById(7L)).thenReturn(Optional.of(entity));
        when(bookClient.getPlayableBook(1L)).thenReturn(book(true));
        AdventureStateResponse result = service.makeChoice(7L, new MakeChoiceRequest(1));
        assertEquals(AdventureStatus.DEAD, result.status());
        assertEquals(0, result.health());
        assertEquals(1, result.currentSection().id());
        assertEquals("Fatal damage", result.consequenceMessage());
        assertEquals(0, entity.getPlayer().getHealth());
    }

    @Test
    void invalidOptionDoesNotChangeState() {
        AdventureEntity entity = stored();
        when(repository.findById(7L)).thenReturn(Optional.of(entity));
        when(bookClient.getPlayableBook(1L)).thenReturn(book(true));
        assertThrows(InvalidOptionException.class, () -> service.makeChoice(7L, new MakeChoiceRequest(5)));
        assertEquals(AdventureStatus.ACTIVE, entity.getStatus());
    }

    @Test
    void finishedAdventureCannotChoose() {
        AdventureEntity entity = stored();
        entity.setStatus(AdventureStatus.COMPLETED);
        when(repository.findById(7L)).thenReturn(Optional.of(entity));
        assertThrows(AdventureAlreadyFinishedException.class, () -> service.makeChoice(7L, new MakeChoiceRequest(0)));
        verifyNoInteractions(bookClient);
    }

    @Test
    void missingCurrentSectionThrows() {
        AdventureEntity entity = stored();
        entity.setCurrentSectionId(99);
        when(repository.findById(7L)).thenReturn(Optional.of(entity));
        when(bookClient.getPlayableBook(1L)).thenReturn(book(true));
        assertThrows(SectionNotFoundException.class, () -> service.getState(7L));
    }
}
