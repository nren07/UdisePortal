package com.udise.portal.entity;

import com.udise.portal.enums.FileType;
import com.udise.portal.enums.Format;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "file_validation")
@Setter
public class FileValidation extends AbstractEntity {

    public static final String RECON = "recon";

    public static final String Type = "type";

    private FileType fileType;

    private Format type;

    private Integer discardIfExistsDataMTN;

    private Integer discardIfInvalidDataMTN;

    private Integer discardIfDuplicateRecordsMTN;


    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public Format getType() {
        return type;
    }

    @Column(name = "exists_mtn", nullable = false)
    public Integer getDiscardIfExistsDataMTN() {
        return discardIfExistsDataMTN;
    }

    @Column(name = "invalid_mtn", nullable = false)
    public Integer getDiscardIfInvalidDataMTN() {
        return discardIfInvalidDataMTN;
    }

    @Column(name = "duplicate_mtn", nullable = false)
    public Integer getDiscardIfDuplicateRecordsMTN() {
        return discardIfDuplicateRecordsMTN;
    }

    @Column(name = "file_type", nullable = true)
    @Enumerated(EnumType.STRING)
    public FileType getFileType() {
        return fileType;
    }

}

