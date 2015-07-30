package lv.company.edup.infrastructure.lucene.api.config;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute;
import lv.company.edup.infrastructure.lucene.impl.indexer.SubjectsIndexAttribute;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexDeletionPolicy;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@ApplicationScoped
public class IndexConfigProvider {

    private static final Logger LOGGER = Logger.getLogger(IndexConfigProvider.class.getSimpleName());

    private Map<IndexType, Directory> directoryMap = new ConcurrentHashMap<IndexType, Directory>();
    private Map<IndexType, DirectoryReader> directoryReaderMap = new ConcurrentHashMap<IndexType, DirectoryReader>();
    private Map<IndexType, Collection<? extends IndexAttribute>> fullTextSearchAttributes;
    private Map<IndexType, IndexSearcher> searcherMap = new ConcurrentHashMap<IndexType, IndexSearcher>();

    private Map<IndexType, SearcherManager> managerMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        fullTextSearchAttributes = new ConcurrentHashMap<IndexType, Collection<? extends IndexAttribute>>();

        fullTextSearchAttributes.put(IndexType.STUDENT, Arrays.asList(
                StudentIndexAttribute.NAME,
                StudentIndexAttribute.LAST_NAME,
                StudentIndexAttribute.PERSON_ID,
                StudentIndexAttribute.MAIL,
                StudentIndexAttribute.MOBILE,
                StudentIndexAttribute.BIRTH_DATE));

        fullTextSearchAttributes.put(IndexType.SUBJECT, Collections.singletonList(SubjectsIndexAttribute.NAME));


        BooleanQuery.setMaxClauseCount(1024 * 100);
        IndexWriterConfig indexWriterConfig = buildIndexWriterConfig();
    }

    public IndexWriter buildWriter(IndexType type) throws IOException {
        IndexWriterConfig config = buildIndexWriterConfig();

        if (!directoryMap.containsKey(type)) {
            prepareDirectory(type);
        }

        return new IndexWriter(directoryMap.get(type), config);
    }

    public IndexSearcher getSearcher(IndexType type) {
        try {

            if (!directoryReaderMap.containsKey(type)) {
                if (!directoryMap.containsKey(type)) {
                    prepareDirectory(type);
                }
                directoryReaderMap.put(type, DirectoryReader.open(directoryMap.get(type)));

            }
            DirectoryReader oldReader = directoryReaderMap.get(type);
            DirectoryReader reader = DirectoryReader.openIfChanged(oldReader);
            if (reader == null) {
                if (!searcherMap.containsKey(type)) {
                    searcherMap.put(type, buildSearcher(type));
                }
            } else {
                IOUtils.closeQuietly(oldReader);
                directoryReaderMap.put(type, reader);
                searcherMap.put(type, buildSearcher(type));
            }
        } catch (IOException e) {
            throw new InternalException(e);
        }

        return searcherMap.get(type);
    }

    private void prepareDirectory(IndexType type) throws IOException {
        Path path = FileSystems.getDefault().getPath("index", StringUtils.lowerCase(type.name()));
        File indexDirectory = path.toFile();
        if (!indexDirectory.exists()) {
            FileUtils.forceMkdir(indexDirectory);
        }
        MMapDirectory directory = new MMapDirectory(path);
        directoryMap.put(type, directory);
    }

    private IndexSearcher buildSearcher(IndexType type) throws IOException {
        IndexSearcher indexSearcher = new IndexSearcher(directoryReaderMap.get(type), Executors.newCachedThreadPool());
        indexSearcher.setSimilarity(new DefaultSimilarity());

        LuceneDictionary dictionary = new LuceneDictionary(directoryReaderMap.get(type), LuceneDocumentUtils.ALL);
        SpellChecker spellChecker = new SpellChecker(directoryMap.get(type));

        spellChecker.indexDictionary(dictionary, buildIndexWriterConfig(), false);
        return indexSearcher;
    }

    public Collection<? extends IndexAttribute> getFullTextSearchAttributes(IndexType type) {
        return fullTextSearchAttributes.get(type);
    }

    public Map<IndexAttribute, Float> getCustomBoosts(IndexType type) {
        return null;
    }

    private IndexWriterConfig buildIndexWriterConfig() {
        IndexWriterConfig config = new IndexWriterConfig(new AppAnalyzer());
        config.setWriteLockTimeout(TimeUnit.SECONDS.toMillis(60));
        config.setIndexDeletionPolicy(new IndexDeletionPolicy() {
            @Override
            public void onInit(List<? extends IndexCommit> commits) throws IOException {
                int size = commits.size();
                for (int i = 0; i < size - 1; i++) {
                    commits.get(i).delete();
                }
            }

            @Override
            public void onCommit(List<? extends IndexCommit> commits) throws IOException {

            }
        });
        return config;
    }
}
