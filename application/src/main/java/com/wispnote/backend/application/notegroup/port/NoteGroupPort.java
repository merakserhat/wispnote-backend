package com.wispnote.backend.application.notegroup.port;

import com.wispnote.backend.application.notegroup.model.NoteGroup;
import com.wispnote.backend.application.notegroup.model.NoteGroupFilter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteGroupPort {
    NoteGroup create(NoteGroup note);

    Optional<NoteGroup> findByIdAndMemberId(UUID id, UUID memberId);

    List<NoteGroup> findAllByMemberId(UUID memberId, NoteGroupFilter filter);

    void deleteByIdAndMemberId(UUID id, UUID memberId);

}
