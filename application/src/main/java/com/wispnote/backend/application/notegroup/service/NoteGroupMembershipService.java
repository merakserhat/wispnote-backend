package com.wispnote.backend.application.notegroup.service;

import com.wispnote.backend.application.note.exception.NoteNotFoundBusinessException;
import com.wispnote.backend.application.note.port.NotePort;
import com.wispnote.backend.application.notegroup.exception.NoteGroupNotFoundBusinessException;
import com.wispnote.backend.application.notegroup.port.NoteGroupPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteGroupMembershipService {

    private final NoteGroupPort noteGroupPort;
    private final NotePort notePort;

    @Transactional
    public void addNoteToGroup(UUID memberId, UUID noteGroupId, UUID noteId) {
        validateMemberOwnsGroupAndNote(memberId, noteGroupId, noteId);
        noteGroupPort.addNoteToGroup(noteGroupId, noteId);
    }

    @Transactional
    public void removeNoteFromGroup(UUID memberId, UUID noteGroupId, UUID noteId) {
        validateMemberOwnsGroupAndNote(memberId, noteGroupId, noteId);
        noteGroupPort.removeNoteFromGroup(noteGroupId, noteId);
    }

    private void validateMemberOwnsGroupAndNote(UUID memberId, UUID noteGroupId, UUID noteId) {
        if (!noteGroupPort.existsByIdAndMemberId(noteGroupId, memberId)) {
            throw new NoteGroupNotFoundBusinessException();
        }

        if (!notePort.existsByIdAndMemberId(noteId, memberId)) {
            throw new NoteNotFoundBusinessException();
        }
    }
}
