package lv.company.edup.persistence.students;

import lv.company.edup.infrastructure.utils.AppCollectionUtils;
import lv.company.edup.persistence.students.properties.PropertyName;
import lv.company.edup.persistence.students.properties.StudentProperty;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@MappedSuperclass
public abstract class Student implements Serializable {

    public static final String PATTERN = "dd-MM-yyyy";

    public interface Parameters {
        String ID = "pId";
    }

    @Column(name = "NAME")
    protected String name;

    @Column(name = "LAST_NAME")
    protected String lastName;

    @Column(name = "CREATED")
    protected Date created;

    @Transient
    private String rootUrl;
    @Transient
    private boolean indexed = false;
    @Transient
    private List<StudentProperty> properties;
    @Transient
    private Map<PropertyName, Collection<StudentProperty>> map = new HashMap<>();


    public List<StudentProperty> getProperties() {
        if (properties == null) {
            properties = new ArrayList<StudentProperty>();
        }
        return properties;
    }

    public StudentProperty getSinglePropertyByName(PropertyName propertyName) {
        indexProperties();
        if (MapUtils.isNotEmpty(map) && map.containsKey(propertyName)) {
            Collection<StudentProperty> collection = map.get(propertyName);
            if (CollectionUtils.isNotEmpty(collection))
                return collection.iterator().next();
        }
        return null;
    }

    public Boolean getBooleanProperty(PropertyName propertyName) {
        StudentProperty property = getSinglePropertyByName(propertyName);
        if (property != null && StringUtils.isNotEmpty(property.getValue())) {
            return Boolean.valueOf(property.getValue());
        }
        return null;
    }

    public Long getLongProperty(PropertyName propertyName) {
        StudentProperty property = getSinglePropertyByName(propertyName);
        if (property != null && StringUtils.isNumeric(property.getValue())) {
            return Long.valueOf(property.getValue());
        }
        return null;
    }

    public String getStringProperty(PropertyName propertyName) {
        StudentProperty property = getSinglePropertyByName(propertyName);
        if (property != null) {
            return property.getValue();
        }
        return null;
    }

    public Date getDateProperty(PropertyName propertyName) {
        StudentProperty property = getSinglePropertyByName(propertyName);
        if (property != null) {
            try {
                return DateUtils.parseDate(property.getValue(), PATTERN);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addProperty(StudentProperty property) {
        List<StudentProperty> list = getProperties();
        list.add(property);
    }

    public void addProperty(PropertyName propertyName, Boolean value) {
        if (value != null) {
            StudentProperty property = prepareProperty(propertyName);
            property.setValue(String.valueOf(value));
            addProperty(property);
        }
    }

    public void addProperty(PropertyName propertyName, Number value) {
        if (value != null) {
            StudentProperty property = prepareProperty(propertyName);
            property.setValue(String.valueOf(value));
            addProperty(property);
        }
    }

    public void addProperty(PropertyName propertyName, String value) {
        if (StringUtils.isNotEmpty(value)) {
            StudentProperty property = prepareProperty(propertyName);
            property.setValue(value);
            addProperty(property);
        }
    }

    public void addProperty(PropertyName propertyName, Date date) {
        if (date != null) {
            StudentProperty property = prepareProperty(propertyName);
            property.setValue(DateFormatUtils.format(date, PATTERN));
            addProperty(property);
        }
    }

    private StudentProperty prepareProperty(PropertyName propertyName) {
        StudentProperty property = new StudentProperty();
        property.setName(propertyName);
        return property;
    }

    public Collection<StudentProperty> getPropertiesByName(PropertyName propertyName) {
        indexProperties();
        if (MapUtils.isNotEmpty(map) && map.containsKey(propertyName)) {
            return map.get(propertyName);
        } else {
            return Collections.emptyList();
        }
    }

    protected void indexProperties() {
        if (!indexed && CollectionUtils.isNotEmpty(properties)) {
            map = AppCollectionUtils.mapToCollection(properties, new AppCollectionUtils.KeyTransformer<StudentProperty, PropertyName>() {
                @Override
                public PropertyName transform(StudentProperty studentProperty) {
                    return studentProperty.getName();
                }
            });
        }
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

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getRootUrl() {
        return rootUrl;
    }

    public void setRootUrl(String rootUrl) {
        this.rootUrl = rootUrl;
    }

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract Long getVersionId();

    public abstract void setVersionId(Long versionId);

    public abstract Date getUpdated();
}
