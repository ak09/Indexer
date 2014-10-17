package edu.nyu.cs.cs2580;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import edu.nyu.cs.cs2580.SearchEngine.Options;
import gnu.trove.list.array.TByteArrayList;
import gnu.trove.map.TIntByteMap;
import gnu.trove.map.hash.TIntByteHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @CS2580: Implement this class for HW2.
 */
public class IndexerInvertedCompressed extends Indexer {
	private class SkipPointer{
		private int docId;
		private int byteNumber;
		
		SkipPointer(int id,int size){
			docId = id;
			byteNumber = size;
		}
		
		public int getDocId() {
			return docId;
		}
		
		public int getByteNumber() {
			return byteNumber;
		}
		
	}
	
	public IndexerInvertedCompressed(Options options) {
		super(options);
		System.out.println("Using Indexer: " + this.getClass().getSimpleName());
	}
	// word occurances in different docs by saving int as key which represents the word in the dictionary
	// docid, no. of occurances and positions in the document 
	private static TIntObjectHashMap<TByteArrayList> wordMap = new TIntObjectHashMap<TByteArrayList>();
	//private static Map<String,List<Byte>> wordMap1 = new HashMap<String,List<Byte>>();
	//private static Map<Integer,TByteArrayList> wordMap2 = new HashMap<Integer,TByteArrayList>();
	
	
	// word to integer mapping
	private static Map<String,Integer> dictionary = new HashMap<String,Integer>();
	// integer to word mapping
	private static Vector<String> corpusVocabulary = new Vector<>();
	
	/*// words in the doc in terms of bytes to save space
  	private static Map<Integer,List<Byte>> docMap = new HashMap<Integer,List<Byte>>();
	 */
	private Map<Integer, Integer> _termDocFrequency =
			new HashMap<Integer, Integer>();
	
	// Term frequency, key is the integer representation of the term and value is
	// the number of times the term appears in the corpus.
	private Map<Integer, Integer> _termCorpusFrequency =
			new HashMap<Integer, Integer>();

	// Stores all Document in memory.(it will be DocumentIndexed)
	private Vector<Document> _documents = new Vector<Document>();
	// contains list of skip pointers for every word will have skip pointer for every 25 documents
	private static Map<Integer,List<SkipPointer>> skipPointers = new HashMap<Integer,List<SkipPointer>>();
	// converts url to docId 
	private static Map<String,Integer> urlMap = new HashMap<String,Integer>();
	
	@Override
	public void constructIndex() throws IOException {
		List<File> fileList = listFilesForFolder(new File("data/wiki"));
		for(File f: fileList){
			String docContent= null;// aashu's class will be called and it will return that in String
			processDocument(docContent);
		}
	}

	private void processDocument(String content) {
		
	}
	
	@Override
	public void loadIndex() throws IOException, ClassNotFoundException {
	}

	@Override
	public Document getDoc(int docid) {
		return null;
	}

	/**
	 * In HW2, you should be using {@link DocumentIndexed}
	 */
	@Override
	public Document nextDoc(Query query, int docid) {
		return null;
	}

	@Override
	public int corpusDocFrequencyByTerm(String term) {
		return 0;
	}

	@Override
	public int corpusTermFrequency(String term) {
		return 0;
	}

	/**
	 * @CS2580: Implement this for bonus points.
	 */
	@Override
	public int documentTermFrequency(String term, String url) {
		return 0;
	}

	private int byteToInts(TByteArrayList byteList,int index){
		boolean nextValue = Boolean.TRUE;
		int i = index;
		int number = 0;
		while(nextValue){
			if(byteList.get(i) >> 7 == 1){
				nextValue = Boolean.FALSE;
			}
			number = number | ((byteList.get(i) & 0x0000007F) << (i*7));
			i++;
		}
		return number;
	}

	private void intToBytes(TByteArrayList byteList, int number){
		TByteArrayList bytes = new TByteArrayList();
		boolean foundAll = Boolean.FALSE;
		for(int i=0;i<4 && !foundAll;i++){
    		byte b = (byte)((number >> i*7) & 0x0000007F);
    		if(b==0){
    			bytes.set(i-1, (byte)(bytes.get(i-1) & 0xFF));
    			foundAll = Boolean.TRUE;
    		}
    		else{
    			bytes.add(b);
        	}
    	}
	}

	private void listToDocList(){

	}
    
    public static int numberOfBytesMoved(int n){
		int bits = log2(n);
		if(bits < 7){
			return 1;
		}
		else if(bits < 14){
			return 2;
		}
		else if(bits < 21){
			return 3;
		}
		else
			return 4;
	}

	public static int log2(int n){
		if(n == 0){
			return 0;
		}
		return 31 - Integer.numberOfLeadingZeros(n);
	}
	
	public List<File> listFilesForFolder(final File folder) {
		List<File> fileList = new ArrayList<File>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (!fileEntry.isDirectory()) {
	        	fileList.add(fileEntry);
	        }
	    }
	    return fileList;
	}
}
