package lv.company.edup.persistence.files;

import lv.company.edup.persistence.EdupRepository;

import javax.ejb.Singleton;
import java.util.List;

@Singleton
public class FileRepository extends EdupRepository<FileEntity> {

    @Override
    public Class<FileEntity> getEntityClass() {
        return FileEntity.class;
    }

    public List<FileEntity> findByCheckSum(Long value) {
        return em.createNamedQuery(FileEntity.Query.FIND_BY_CHECKSUM, FileEntity.class)
                .setParameter(FileEntity.Parameters.CHECKSUM, value)
                .getResultList();
    }
}
