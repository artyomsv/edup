package lv.company.edup.persistence.students.properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "STUDENT_PROPERTIES")
@SequenceGenerator(name = StudentProperty.SEQUENCE, sequenceName = "STUDENT_PROPERTY_SEQUENCE", allocationSize = 1)
public class StudentProperty {

    public static final String SEQUENCE = "sPartnerProperty";

    @Id
    @Column(name = "PROPERTY_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    private Long id;

    @Column(name = "STUDENT_VERSION_FK")
    private Long versionId;

    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    private PropertyName name;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "ORDER_NUMBER")
    private Long order;

    @Column(name = "REFERENCE_ID")
    private Long referenceId;

    public StudentProperty() {
    }

    public StudentProperty(PropertyName name, String value) {
        this.name = name;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public PropertyName getName() {
        return name;
    }

    public void setName(PropertyName name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StudentProperty{");
        sb.append("name=").append(name);
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
