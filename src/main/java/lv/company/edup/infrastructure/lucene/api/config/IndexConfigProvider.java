package lv.company.edup.infrastructure.lucene.api.config;

import lv.company.edup.infrastructure.exceptions.InternalException;
import lv.company.edup.infrastructure.lucene.api.indexer.IndexAttribute;
import lv.company.edup.infrastructure.lucene.impl.LuceneDocumentUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.BIRTH_DATE;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.LAST_NAME;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.MAIL;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.MOBILE;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.NAME;
import static lv.company.edup.infrastructure.lucene.impl.indexer.StudentIndexAttribute.PERSON_ID;

@ApplicationScoped
public class IndexConfigProvider {

    private static final Logger LOGGER = Logger.getLogger(IndexConfigProvider.class.getSimpleName());

    @Inject @IndexAnalyzer Analyzer analyzer;

    private Map<IndexType, Directory> directoryMap = new ConcurrentHashMap<IndexType, Directory>();
    private Map<IndexType, DirectoryReader> directoryReaderMap = new ConcurrentHashMap<IndexType, DirectoryReader>();
    private Map<IndexType, Collection<? extends IndexAttribute>> fullTextSearchAttributes;
    private Map<IndexType, IndexSearcher> searcherMap = new ConcurrentHashMap<IndexType, IndexSearcher>();

    @PostConstruct
    public void init() {
        fullTextSearchAttributes = new ConcurrentHashMap<IndexType, Collection<? extends IndexAttribute>>();
        fullTextSearchAttributes.put(IndexType.STUDENT, Arrays.asList(NAME, LAST_NAME, PERSON_ID, MAIL, MOBILE, BIRTH_DATE));
    }

    public IndexWriter buildWriter(IndexType type) throws IOException {
        IndexWriterConfig config = get();

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

        spellChecker.indexDictionary(dictionary, get(), false);
        return indexSearcher;
    }

    public Collection<? extends IndexAttribute> getFullTextSearchAttributes(IndexType type) {
        return fullTextSearchAttributes.get(type);
    }

    public Map<IndexAttribute, Float> getCustomBoosts(IndexType type) {
        return null;
    }

    private IndexWriterConfig get() {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
//        config.setMergedSegmentWarmer(new SimpleMergedSegmentWarmer(new PrintStreamInfoStream(System.out)));
//        config.setMergeScheduler(new ConcurrentMergeScheduler());
        config.setRAMBufferSizeMB(256);
        return config;
    }
}
