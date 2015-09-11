package lv.company.edup.services.subjects.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SubjectDto {

    private Long subjectId;
    @NotBlank(message = "Subject name may not be null")
    private String subjectName;
    private Date created;

    public SubjectDto() {
    }

    public SubjectDto(Long subjectId, String subjectName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SubjectDto{");
        sb.append("subjectId=").append(subjectId);
        sb.append(", subjectName='").append(subjectName).append('\'');
        sb.append(", created=").append(created);
        sb.append('}');
        return sb.toString();
    }
}
