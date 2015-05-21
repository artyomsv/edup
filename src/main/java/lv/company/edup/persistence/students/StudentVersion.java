package lv.company.edup.persistence.students;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "STUDENTS")
@SequenceGenerator(name = "sStudent", sequenceName = "STUDENT_VERSION_SEQUENCE", allocationSize = 1)
public class StudentVersion extends Student {

    @Id
    @Column(name = "STUDENT_VERSION_ID")
    @GeneratedValue(generator = "sStudent", strategy = GenerationType.SEQUENCE)
    private Long versionId;

    @Column(name = "STUDENT_ID")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "STUDENT_VERSION_FK", referencedColumnName = "STUDENT_VERSION_ID")
    private List<StudentProperty> properties;

    @Override
    public List<StudentProperty> getProperties() {
        return properties;
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
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
