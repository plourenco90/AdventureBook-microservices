package com.pictet.bookservice.domain.unreachable;

import com.pictet.bookservice.domain.model.Book;
import com.pictet.bookservice.domain.model.Section;

import java.util.*;

public class UnreachableSectionsAnalyzer {

    public List<Integer> findUnreachableSections(Book book) {
        Map<Integer, Section> sectionsById = new HashMap<>();
        book.sections().forEach(section -> sectionsById.put(section.id(), section));
        Section beginningSection = book.findBeginningSection()
                .orElseThrow(() -> new IllegalArgumentException("Book has no beginning section"));
        Set<Integer> visited = new HashSet<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(beginningSection.id());
        while (!stack.isEmpty()) {
            Integer currentSectionId = stack.pop();
            if (!visited.add(currentSectionId)) {
                continue;
            }
            Section currentSection = sectionsById.get(currentSectionId);
            currentSection.options().forEach(option -> {
                Integer targetSectionId = option.targetSectionId();
                if (!visited.contains(targetSectionId)) {
                    stack.push(targetSectionId);
                }
            });
        }
        return book.sections().stream().map(Section::id).filter(sectionId -> !visited.contains(sectionId)).toList();
    }
}
