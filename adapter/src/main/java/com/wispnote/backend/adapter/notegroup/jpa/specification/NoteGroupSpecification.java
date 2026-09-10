package com.wispnote.backend.adapter.notegroup.jpa.specification;

import com.wispnote.backend.adapter.common.jpa.util.SpecificationUtil;
import com.wispnote.backend.adapter.notegroup.jpa.entity.NoteGroupEntity;
import com.wispnote.backend.application.notegroup.model.NoteGroupFilter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class NoteGroupSpecification {

    public static Specification<NoteGroupEntity> of(UUID memberId, NoteGroupFilter filter) {
        return (root, query, builder) -> builder.and(
                builder.equal(root.get("memberId"), memberId),
                builder.isFalse(root.get("deleted")),
                builder.or(
                        SpecificationUtil.like(builder, root.get("title"), filter.search()),
                        SpecificationUtil.like(builder, root.get("description"), filter.search())));
    }
}
