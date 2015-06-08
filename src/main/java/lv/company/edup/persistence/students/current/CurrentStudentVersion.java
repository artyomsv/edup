package lv.company.edup.persistence.students.current;

import lv.company.edup.persistence.students.Student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "V_STUDENTS")
public class CurrentStudentVersion extends Student {

    @Id
    @Column(name = "STUDENT_ID")
    private Long id;

    @Column(name = "STUDENT_VERSION_ID")
    private Long versionId;

    @Column(name = "UPDATED")
    private Date updated;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getVersionId() {
        return versionId;
    }

    @Override
    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}
