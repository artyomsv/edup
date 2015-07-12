package lv.company.edup.persistence.subjects.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "SUBJECTS")
@SequenceGenerator(name = Subject.SEQUENCE, sequenceName = "subjects_sequence", allocationSize = 1)
public class Subject {

    public static final String SEQUENCE = "sSubjects";

    @Id
    @Column(name = "subject_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    private Long subjectId;

    @Column(name = "subject_name")
    private String subjectName;

    @Column(name = "created")
    private Date created;

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
