package lv.company.edup.persistence.documents;

import lv.company.edup.services.documents.dto.DocumentStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@NamedStoredProcedureQuery(name = StudentDocument.Procedure.GENERATE_FAKTURA_ID, procedureName = "getfakturaid")

@Entity
@Table(name = "STUDENT_DOCUMENTS")
@SequenceGenerator(name = StudentDocument.SEQUENCE, sequenceName = "STUDENT_DOCUMENTS_SEQUENCE", allocationSize = 1)
public class StudentDocument {

    public static final String SEQUENCE = "sStudentDocument";

    public interface Procedure {
        String GENERATE_FAKTURA_ID = "w";
    }

    @Id
    @Column(name = "DOCUMENT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    private Long id;

    @Column(name = "FILE_FK")
    private Long fileId;

    @Column(name = "STUDENT_FK")
    private Long studentId;

    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "UPDATED")
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
