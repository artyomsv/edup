package lv.company.edup.persistence;

public class EntityPayload {

    private Long id;
    private Long versionId;

    public EntityPayload(Long id, Long versionId) {
        this.id = id;
        this.versionId = versionId;
    }

    public Long getId() {
        return id;
    }

    public Long getVersionId() {
        return versionId;
    }

}
