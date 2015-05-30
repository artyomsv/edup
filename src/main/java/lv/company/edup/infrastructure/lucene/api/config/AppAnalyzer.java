package lv.company.edup.infrastructure.lucene.api.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;

public class AppAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String s) {
        Tokenizer tokenizer = new WhitespaceTokenizer();
//        Tokenizer tokenizer1 = new StandardTokenizer();
        TokenStream stream = new LowerCaseFilter(tokenizer);
        stream = new ASCIIFoldingFilter(stream);

        return new TokenStreamComponents(tokenizer, stream);
    }


}
