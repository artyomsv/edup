package lv.company.edup.persistence.documents;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "V_STUDENT_DOCUMENTS")
public class StudentDocumentDetails {

    @Id
    @Column(name = "DOCUMENT_ID")
    private Long id;

    @Column(name = "FILE_FK")
    private Long fileId;

    @Column(name = "STUDENT_FK")
    private Long studentId;

    @Column(name = "CREATED")
    private Date created;

    @Column(name = "NAME")
    private String fileName;

    @Column(name = "SIZE")
    private Long size;

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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
