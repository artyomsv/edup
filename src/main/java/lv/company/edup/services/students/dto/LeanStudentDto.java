package lv.company.edup.services.students.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lv.company.edup.services.students.validation.StudentUpdateCheck;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LeanStudentDto {

    private Long id;
    @NotNull(groups = StudentUpdateCheck.class, message = "Student version may not be null")
    private Long versionId;
    @NotBlank(message = "Name is missing")
    @Size(max = 32, message = "Name have to be {max} letters length")
    private String name;
    @NotBlank(message = "Last name missing")
    @Size(max = 32, message = "Last name have to be {max} letters length")
    private String lastName;
    @Pattern(regexp = "(\\d{6}+-\\d{5}+)", message = "Person number does not match pattern")
    @Size(min = 0, max = 12, message = "Person number have to {max} letters length")
    private String personId;
    @Size(min = 0, max = 24, message = "Phone have to be max {max} length")
    private String mobile;
    private Long age;
    @Past(message = "Birth date cannot be in future")
    private Date birthDate;
    private Date created;
    private Date updated;

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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("id", id)
                .append("name", name)
                .append("lastName", lastName)
                .append("created", created)
                .toString();
    }
}
