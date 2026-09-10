package com.wispnote.backend.application.notegroup;

import com.wispnote.backend.application.notegroup.exception.NoteGroupNotFoundBusinessException;
import com.wispnote.backend.application.notegroup.model.CreateNoteGroupCommand;
import com.wispnote.backend.application.notegroup.model.NoteGroup;
import com.wispnote.backend.application.notegroup.model.NoteGroupFilter;
import com.wispnote.backend.application.notegroup.port.NoteGroupPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteGroupFacade {

    private final NoteGroupPort noteGroupPort;

    public NoteGroup create(UUID memberId, CreateNoteGroupCommand createNoteGroupCommand) {
        log.info("Creating note group for {} with {}, {}",
                kv("memberId", memberId),
                kv("title", createNoteGroupCommand.title()),
                kv("description", createNoteGroupCommand.description()));

        return noteGroupPort.create(createNoteGroupCommand.toModel(memberId));
    }

    public List<NoteGroup> list(UUID memberId, NoteGroupFilter filter) {
        log.info("Retrieving note group for {} with {}",
                kv("memberId", memberId),
                kv("search", filter.search()));

        return noteGroupPort.findAllByMemberId(memberId, filter);
    }

    public NoteGroup retrieve(UUID memberId, UUID noteGroupId) {
        log.info("Retrieving not group for {} with {}",
                kv("memberId", memberId),
                kv("noteGroupId", noteGroupId));

        return noteGroupPort.findByIdAndMemberId(noteGroupId, memberId).
                orElseThrow(NoteGroupNotFoundBusinessException::new);
    }

    public void delete(UUID memberId, UUID noteGroupId) {
        log.info("Deleting note group for {} with {}",
                kv("memberId", memberId),
                kv("noteGroupId", noteGroupId));

        noteGroupPort.deleteByIdAndMemberId(noteGroupId, memberId);
    }
}
