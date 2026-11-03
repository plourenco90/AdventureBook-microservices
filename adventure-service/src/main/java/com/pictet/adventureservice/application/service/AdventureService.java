package com.pictet.adventureservice.application.service;

import com.pictet.adventureservice.application.dto.AdventureStateResponse;
import com.pictet.adventureservice.application.dto.MakeChoiceRequest;
import com.pictet.adventureservice.application.dto.StartAdventureRequest;
import com.pictet.adventureservice.application.mapper.AdventureResponseMapper;
import com.pictet.adventureservice.domain.consequence.ConsequenceProcessor;
import com.pictet.adventureservice.domain.exception.AdventureNotFoundException;
import com.pictet.adventureservice.domain.exception.InvalidOptionException;
import com.pictet.adventureservice.domain.exception.SectionNotFoundException;
import com.pictet.adventureservice.domain.model.Adventure;
import com.pictet.adventureservice.domain.model.Player;
import com.pictet.adventureservice.infrastructure.client.book.BookClient;
import com.pictet.adventureservice.infrastructure.client.book.dto.*;
import com.pictet.adventureservice.infrastructure.client.book.exception.InvalidBookException;
import com.pictet.adventureservice.infrastructure.client.book.mapper.PlayableBookMapper;
import com.pictet.adventureservice.infrastructure.persistence.entity.AdventureEntity;
import com.pictet.adventureservice.infrastructure.persistence.mapper.AdventurePersistenceMapper;
import com.pictet.adventureservice.infrastructure.persistence.repository.AdventureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdventureService {


    private final BookClient bookClient;
    private final PlayableBookMapper playableBookMapper;
    private final AdventureRepository adventureRepository;
    private final ConsequenceProcessor consequenceProcessor;
    private final AdventureResponseMapper adventureResponseMapper;
    private final AdventurePersistenceMapper adventurePersistenceMapper;

    @Transactional
    public AdventureStateResponse start(StartAdventureRequest request) {
        PlayableBook book = findBook(request.bookId());
        PlayableSection beginning = book.sections()
                .stream()
                .filter(section -> section.type() == SectionType.BEGIN)
                .findFirst()
                .orElseThrow(() -> new InvalidBookException(book.id()));
        Player player = new Player(request.playerName());
        AdventureEntity savedAdventure = adventureRepository.save(adventurePersistenceMapper.toEntity(new Adventure(null,
                book.id(),
                player,
                beginning.id())));
        log.info("Adventure started: adventureId={}, bookId={}", savedAdventure.getId(), book.id());
        return adventureResponseMapper.toResponse(adventurePersistenceMapper.toDomain(savedAdventure), beginning, null);
    }

    @Transactional(readOnly = true)
    public AdventureStateResponse getState(Long adventureId) {
        Adventure adventure = findAdventure(adventureId);
        PlayableBook book = findBook(adventure.getBookId());
        return adventureResponseMapper.toResponse(adventure, findSection(book, adventure.getCurrentSectionId()), null);
    }

    @Transactional
    public AdventureStateResponse makeChoice(Long adventureId, MakeChoiceRequest request) {
        AdventureEntity entity = adventureRepository.findById(adventureId)
                .orElseThrow(() -> new AdventureNotFoundException(adventureId));

        Adventure adventure = adventurePersistenceMapper.toDomain(entity);
        adventure.ensureActive();
        PlayableBook book = findBook(adventure.getBookId());
        PlayableSection currentSection = findSection(book, adventure.getCurrentSectionId());
        PlayableOption selectedOption = getSelectedOption(currentSection, request.optionIndex());
        consequenceProcessor.apply(selectedOption.consequence(), adventure.getPlayer());
        String consequenceMessage = selectedOption.consequence() == null ? null : selectedOption.consequence().text();
        log.debug("Adventure choice: adventureId={}, optionIndex={}, sectionId={}",
                adventureId,
                request.optionIndex(),
                adventure.getCurrentSectionId());
        if (adventure.getPlayer().isDead()) {
            log.info("Adventure ended in death: adventureId={}", adventureId);
            adventure.die();
        } else {
            PlayableSection nextSection = findSection(book, selectedOption.targetSectionId());
            adventure.moveTo(nextSection.id());
            if (nextSection.type() == SectionType.END) {
                log.info("Adventure completed: adventureId={}", adventureId);
                adventure.complete();
            }
        }
        adventurePersistenceMapper.update(adventure, entity);
        PlayableSection current = findSection(book, adventure.getCurrentSectionId());
        return adventureResponseMapper.toResponse(adventure, current, consequenceMessage);
    }

    private PlayableBook findBook(Long bookId) {
        BookPlayableResponse response = bookClient.getPlayableBook(bookId);
        log.debug("Playable book retrieved: bookId={}", bookId);
        if (!response.valid()) {
            throw new InvalidBookException(bookId);
        }
        return playableBookMapper.toDomain(response.book());
    }

    private Adventure findAdventure(Long id) {
        return adventureRepository.findById(id)
                .map(adventurePersistenceMapper::toDomain)
                .orElseThrow(() -> new AdventureNotFoundException(id));
    }

    private PlayableSection findSection(PlayableBook book, Integer sectionId) {
        return book.sections()
                .stream()
                .filter(section -> section.id().equals(sectionId))
                .findFirst()
                .orElseThrow(() -> new SectionNotFoundException(sectionId));
    }

    private PlayableOption getSelectedOption(PlayableSection section, Integer optionIndex) {
        if (optionIndex < 0 || optionIndex >= section.options().size()) {
            throw new InvalidOptionException(optionIndex);
        }
        return section.options().get(optionIndex);
    }
}