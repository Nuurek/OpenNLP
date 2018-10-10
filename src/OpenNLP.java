import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class OpenNLP {

    private static String LANG_DETECT_MODEL = "resources/models/langdetect-183.bin";
    private static String ENGLISH_TOKENIZER_MODEL = "resources/models/en-token.bin";
    private static String GERMAN_TOKENIZER_MODEL = "resources/models/de-token.bin";
    private static String SENTENCE_MODEL = "resources/models/en-sent.bin";
    private static String PART_OF_SPEECH_MODEL = "resources/models/en-pos-maxent.bin";
    private static String CHUNKER_MODEL = "resources/models/en-chunker.bin";
    private static String LEMMATIZER_DICT = "resources/models/en-lemmatizer.dict";
    private static String NAME_MODEL = "resources/models/en-ner-person.bin";
    private static String ENTITY_XXX_MODEL = "resources/models/en-ner-xyz.bin";

	public static void main(String[] args) throws IOException {
		OpenNLP openNLP = new OpenNLP();
		openNLP.run();
	}

	private void run() throws IOException {

		languageDetection();
        tokenization();
        sentenceDetection();
        partOfSpeechTagging();
        lemmatization();
        stemming();
		// chunking();
		// nameFinding();
	}

	private void languageDetection() throws IOException {
        System.out.println("Language detection");

		File modelFile = new File(LANG_DETECT_MODEL);
		LanguageDetectorModel model = new LanguageDetectorModel(modelFile);
        LanguageDetectorME detector = new LanguageDetectorME(model);

		String[] texts = {
            "cats",
            "Many cats like milk because in some ways it reminds them of their mother's milk.",
            "The two things are not really related. Many cats like milk because in "
            + "some ways it reminds them of their mother's milk.",
            "The two things are not really related. Many cats like milk because in some ways it reminds them of their mother's milk. "
            + "It is rich in fat and protein. They like the taste. They like the consistency . "
            + "The issue as far as it being bad for them is the fact that cats often have difficulty digesting milk and so it may give them "
            + "digestive upset like diarrhea, bloating and gas. After all, cow's milk is meant for baby calves, not cats. "
            + "It is a fortunate quirk of nature that human digestive systems can also digest cow's milk. But humans and cats are not cows.",
            "Many cats like milk because in some ways it reminds them of their "
            + "mother's milk. Le lait n'est pas forc�ment mauvais pour les chats",
            "Many cats like milk because in some ways it reminds them of their "
            + "mother's milk. Le lait n'est pas forc�ment mauvais pour les chats. "
            + "Der Normalfall ist allerdings der, dass Salonl�wen Milch weder brauchen "
            + "noch gut verdauen k�nnen."
        };
        for (String text: texts) {
            System.out.println(text);

            Language language = detector.predictLanguage(text);
            System.out.printf("Language: %s, %.2f\n", language.getLang(), language.getConfidence());
        }
	}

	private void tokenization() throws IOException {
	    tokenizeByLanguage(ENGLISH_TOKENIZER_MODEL);
	    tokenizeByLanguage(GERMAN_TOKENIZER_MODEL);
	}

	private void tokenizeByLanguage(String modelFilePath) throws IOException {
        System.out.printf("Tokenization %s\n", modelFilePath);

        File modelFile = new File(modelFilePath);
        TokenizerModel tokenizerModel = new TokenizerModel(modelFile);
        TokenizerME tokenizer = new TokenizerME(tokenizerModel);

        String[] texts = {
                "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
                        + "but there may have been instances of domestication as early as the Neolithic from around 9500 years ago (7500 BC).",
                "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
                        + "but there may have been instances of domestication as early as the Neolithic from around 9,500 years ago (7,500 BC).",
                "Since cats were venerated in ancient Egypt, they were commonly believed to have been domesticated there, "
                        + "but there may have been instances of domestication as early as the Neolithic from around 9 500 years ago ( 7 500 BC)."
        };

        for (String text: texts) {
            System.out.println(text);

            tokenizer.tokenize(text);
            double[] tokenProbabilities = tokenizer.getTokenProbabilities();
            System.out.println(Arrays.toString(tokenProbabilities));
        }
    }

	private void sentenceDetection() throws IOException {
        System.out.println("Sentence detection");

        File modelFile = new File(SENTENCE_MODEL);
        SentenceModel sentenceModel = new SentenceModel(modelFile);
        SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);

        String[] texts = {
            "Hi. How are you? Welcome to OpenNLP. "
            + "We provide multiple built-in methods for Natural Language Processing.",
            "Hi. How are you? Welcome to OpenNLP.?? "
            + "We provide multiple . built-in methods for Natural Language Processing.",
            "The interrobang, also known as the interabang (often represented by ?! or !?), "
            + "is a nonstandard punctuation mark used in various written languages. "
            + "It is intended to combine the functions of the question mark (?), or interrogative point, "
            + "and the exclamation mark (!), or exclamation point, known in the jargon of printers and programmers as a \"bang\". "
        };

        for (String text: texts) {
            System.out.println(text);

            sentenceDetector.sentDetect(text);
            double[] sentenceProbabilities = sentenceDetector.getSentenceProbabilities();
            System.out.println(Arrays.toString(sentenceProbabilities));
        }
	}

	private void partOfSpeechTagging() throws IOException {
        System.out.println("Part of speech tagging");

	    String[][] sentences = {
            { "Cats", "like", "milk" },
            { "Cat", "is", "white", "like", "milk" },
            { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
                "built-in", "methods", "for", "Natural", "Language", "Processing" },
            { "She", "put", "the", "big", "knives", "on", "the", "table" }
        };

        for (String[] sentence: sentences) {
            System.out.println(Arrays.toString(sentence));

            String[] partOfSpeechTags = getPartOfSpeechTags(sentence);
            System.out.println(Arrays.toString(partOfSpeechTags));
        }
	}

	private void lemmatization() throws IOException {
        System.out.println("Lemmatizer");

        File modelFile = new File(LEMMATIZER_DICT);
        DictionaryLemmatizer dictionaryLemmatizer = new DictionaryLemmatizer(modelFile);

        String[] sentence = { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
                                "built-in", "methods", "for", "Natural", "Language", "Processing" };
        String[] tags = getPartOfSpeechTags(sentence);

        String[] terms = dictionaryLemmatizer.lemmatize(sentence, tags);
        System.out.println(Arrays.toString(sentence));
        System.out.println(Arrays.toString(terms));
	}

	private void stemming() {
        System.out.println("Stemmer");

        PorterStemmer stemmer = new PorterStemmer();

        String[] sentence = { "Hi", "How", "are", "you", "Welcome", "to", "OpenNLP", "We", "provide", "multiple",
                "built-in", "methods", "for", "Natural", "Language", "Processing" };

        ArrayList<String> stemmedWords = new ArrayList<>();
        for (String word: sentence) {
            stemmedWords.add(stemmer.stem(word));
        }
        System.out.println(Arrays.toString(sentence));
        System.out.println(Arrays.toString(stemmedWords.toArray()));
	}
	
	private void chunking() throws IOException
    {
		String[] sentence = new String[0];
		sentence = new String[] { "She", "put", "the", "big", "knives", "on", "the", "table" };

		String[] tags = new String[0];
		tags = new String[] { "PRP", "VBD", "DT", "JJ", "NNS", "IN", "DT", "NN" };

	}

	private void nameFinding() throws IOException
    {
		String text = "he idea of using computers to search for relevant pieces of information was popularized in the article "
				+ "As We May Think by Vannevar Bush in 1945. It would appear that Bush was inspired by patents "
				+ "for a 'statistical machine' - filed by Emanuel Goldberg in the 1920s and '30s - that searched for documents stored on film. "
				+ "The first description of a computer searching for information was described by Holmstrom in 1948, "
				+ "detailing an early mention of the Univac computer. Automated information retrieval systems were introduced in the 1950s: "
				+ "one even featured in the 1957 romantic comedy, Desk Set. In the 1960s, the first large information retrieval research group "
				+ "was formed by Gerard Salton at Cornell. By the 1970s several different retrieval techniques had been shown to perform "
				+ "well on small text corpora such as the Cranfield collection (several thousand documents). Large-scale retrieval systems, "
				+ "such as the Lockheed Dialog system, came into use early in the 1970s.";

	}

    private String[] getPartOfSpeechTags(String[] sentence) throws IOException {
        File modelFile = new File(PART_OF_SPEECH_MODEL);
        POSModel model = new POSModel(modelFile);
        POSTaggerME partOfSpeechTagger = new POSTaggerME(model);

        return partOfSpeechTagger.tag(sentence);
    }
}
