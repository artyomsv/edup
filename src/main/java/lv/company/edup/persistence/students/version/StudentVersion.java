package lv.company.edup.persistence.students.version;

import lv.company.edup.persistence.students.Student;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NamedStoredProcedureQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedStoredProcedureQuery(name = StudentVersion.Procedure.GENERATE_USER_ID, procedureName = "getStudentId")

@NamedQueries({
        @NamedQuery(
                name = StudentVersion.Query.FIND_VERSIONS,
                query = " select sv from StudentVersion as sv where sv.id = :pId order by sv.created desc"
        )
})

@Entity
@Table(name = "STUDENTS")
@SequenceGenerator(name = "sStudent", sequenceName = "STUDENT_VERSION_SEQUENCE", allocationSize = 1)
public class StudentVersion extends Student {

    public interface Query {
        String FIND_VERSIONS = "StudentVersion:FindVersions";
    }

    public interface Procedure {
        String GENERATE_USER_ID = "StudentVersion:GenerateStudentId";
    }

    @Id
    @Column(name = "STUDENT_VERSION_ID")
    @GeneratedValue(generator = "sStudent", strategy = GenerationType.SEQUENCE)
    private Long versionId;

    @Column(name = "STUDENT_ID")
    private Long id;

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
