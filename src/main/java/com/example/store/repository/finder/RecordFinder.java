package com.example.store.repository.finder;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.store.exception.NotFoundException;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class RecordFinder <Entity, Repository extends JpaRepository<Entity, Long>> {
    
    private final Class<Entity> type;
    private final Repository repository;

    public Entity byId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(type.getClass(), id));
    }
}
