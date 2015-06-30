package lv.company.edup.services.students;

import lv.company.edup.persistence.students.current.CurrentStudentVersion;
import lv.company.edup.persistence.students.current.CurrentStudentVersion_;
import lv.company.odata.impl.jpa.AbstractODataJPAMapping;

import javax.persistence.metamodel.SingularAttribute;

public class StudentODataMapping extends AbstractODataJPAMapping<CurrentStudentVersion> {

    public StudentODataMapping() {
        mapAttribute("Id").to(CurrentStudentVersion_.id);
        mapAttribute("VersionId").to(CurrentStudentVersion_.versionId);
        mapAttribute("Name").to(CurrentStudentVersion_.name);
        mapAttribute("LastName").to(CurrentStudentVersion_.lastName);
        mapAttribute("Created").to(CurrentStudentVersion_.created);
    }

    @Override
    public Class<CurrentStudentVersion> getEntityType() {
        return CurrentStudentVersion.class;
    }

    @Override
    public SingularAttribute<CurrentStudentVersion, ?> getPrimaryKeyAttribute() {
        return CurrentStudentVersion_.id;
    }

}
