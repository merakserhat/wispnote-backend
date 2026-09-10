package com.wispnote.backend.adapter.notegroup.jpa.repository;

import com.wispnote.backend.adapter.notegroup.jpa.entity.NoteGroupNoteEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface NoteGroupNoteRepository extends JpaRepository<NoteGroupNoteEntity, UUID> {

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE note_group_note
            SET deleted = TRUE,
                updated_at = CURRENT_TIMESTAMP
            WHERE note_group_id = :noteGroupId
              AND note_id = :noteId
              AND deleted = FALSE
            """, nativeQuery = true)
    void softDeleteByNoteGroupIdAndNoteId(@Param("noteGroupId") UUID noteGroupId,
                                          @Param("noteId") UUID noteId);

    @Query("""
            SELECT ngn.noteGroupId AS noteGroupId, COUNT(ngn.id) AS noteCount
            FROM NoteGroupNoteEntity ngn
            JOIN NoteEntity n ON n.id = ngn.noteId
            WHERE ngn.noteGroupId IN :noteGroupIds
              AND ngn.deleted = false
              AND n.deleted = false
            GROUP BY ngn.noteGroupId
            """)
    List<NoteGroupNoteCountProjection> countActiveNotesByNoteGroupIds(
            @Param("noteGroupIds") Collection<UUID> noteGroupIds);

    @Query("""
            SELECT COUNT(ngn.id)
            FROM NoteGroupNoteEntity ngn
            JOIN NoteEntity n ON n.id = ngn.noteId
            WHERE ngn.noteGroupId = :noteGroupId
              AND ngn.deleted = false
              AND n.deleted = false
            """)
    long countActiveNotesByNoteGroupId(@Param("noteGroupId") UUID noteGroupId);
}
