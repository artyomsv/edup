package lv.company.edup.persistence.students;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;
import java.util.List;

@MappedSuperclass
public abstract class Student {

    @Column(name = "NAME")
    private String name;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "CREATED")
    private Date created;

    public abstract List<StudentProperty> getProperties();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract Long getVersionId();

    public abstract void setVersionId(Long versionId);
}
