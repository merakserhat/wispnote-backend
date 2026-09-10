package com.wispnote.backend.adapter.notegroup.jpa;

import com.wispnote.backend.adapter.common.exception.EntityNotFoundException;
import com.wispnote.backend.application.notegroup.model.NoteGroup;
import com.wispnote.backend.application.notegroup.model.NoteGroupFilter;
import com.wispnote.backend.application.notegroup.port.NoteGroupPort;
import com.wispnote.backend.adapter.notegroup.jpa.entity.NoteGroupEntity;
import com.wispnote.backend.adapter.notegroup.jpa.repository.NoteGroupRepository;
import com.wispnote.backend.adapter.notegroup.jpa.specification.NoteGroupSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
@RequiredArgsConstructor
public class NoteGroupJpaAdapter implements NoteGroupPort {

    private final NoteGroupRepository noteGroupRepository;

    @Override
    public NoteGroup create(NoteGroup noteGroup) {
        var entity = new NoteGroupEntity();
        entity.setMemberId(noteGroup.memberId());
        entity.setTitle(noteGroup.title());
        entity.setDescription(noteGroup.description());

        return noteGroupRepository.save(entity).toModel();
    }

    @Override
    public Optional<NoteGroup> findByIdAndMemberId(UUID id, UUID memberId) {
        return noteGroupRepository.findByIdAndMemberIdAndDeletedFalse(id, memberId)
                .map(NoteGroupEntity::toModel);
    }

    @Override
    public List<NoteGroup> findAllByMemberId(UUID memberId, NoteGroupFilter filter) {
        return noteGroupRepository.findAll(
                        NoteGroupSpecification.of(memberId, filter),
                        Sort.by(DESC, "createdAt"))
                .stream()
                .map(NoteGroupEntity::toModel)
                .toList();
    }

    @Override
    public void deleteByIdAndMemberId(UUID id, UUID memberId) {
        var entity = noteGroupRepository.findByIdAndMemberIdAndDeletedFalse(id, memberId)
                .orElseThrow(EntityNotFoundException::new);
        entity.setDeleted(true);
        noteGroupRepository.save(entity);
    }
}
