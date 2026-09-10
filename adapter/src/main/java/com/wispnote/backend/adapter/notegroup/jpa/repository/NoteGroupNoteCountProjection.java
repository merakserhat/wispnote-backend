package com.wispnote.backend.adapter.notegroup.jpa.repository;

import java.util.UUID;

public interface NoteGroupNoteCountProjection {
    UUID getNoteGroupId();

    Long getNoteCount();
}
