package lv.company.edup.persistence.students;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "V_STUDENTS")
public class StudentCurrent extends Student {

    @Id
    @Column(name = "STUDENT_ID")
    private Long id;

    @Column(name = "STUDENT_VERSION_ID")
    private Long versionId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "STUDENT_VERSION_FK", referencedColumnName = "STUDENT_VERSION_ID")
    private List<StudentProperty> properties;

    @Override
    public List<StudentProperty> getProperties() {
        return properties;
    }

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
}
