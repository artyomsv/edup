package lv.company.edup.persistence.files;

import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@NamedQueries({
        @NamedQuery(name = FileEntity.Query.FIND_BY_CHECKSUM, query = " select file from FileEntity as file where file.checkSum = :pChecksum ")
})

@Entity
@Table(name = "files")
@SequenceGenerator(sequenceName = "files_sequence", name = FileEntity.Sequence.SEQUENCE, allocationSize = 1)
public class FileEntity {

    public interface Query {
        String FIND_BY_CHECKSUM = "FileEntity:FindFilesByChecksum";
    }

    public interface Parameters {
        String CHECKSUM = "pChecksum";
    }

    interface Sequence {
        String SEQUENCE = "sFile";
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = Sequence.SEQUENCE)
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CONTENT_TYPE")
    @Enumerated(EnumType.STRING)
    private FileType contentType;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "CREATED")
    private Date date;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    @Column(name = "DATA")
    @Basic(fetch = FetchType.EAGER)
    private byte[] data;

    @Column(name = "CHECKSUM")
    private Long checkSum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileType getContentType() {
        return contentType;
    }

    public void setContentType(FileType contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getCheckSum() {
        return checkSum;
    }

    public void setCheckSum(Long checkSum) {
        this.checkSum = checkSum;
    }
}
