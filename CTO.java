import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * @author Anton Belev 1103816b
 * Cipher Text Only Attack
 */
public class CTO {
	private static final String BIGRAMS_TXT = "bigrams.txt";
	private static final String C2_TXT = "c2.txt";
	private static final String WORDS_TXT = "words.txt";
	public static Set<String> englishWords = new HashSet<String>();
	public static Map<String, Double> bigramFrequencyTable = new HashMap<String, Double>();
	private static ArrayList<String> cipherBlocks = new ArrayList<String>();
	
	public static void main(String[] args) throws FileNotFoundException{
		setUpEnglishWordsSet();
		setUpBigramFrequencyTable();
		cipherBlocks = Utils.getCipherBlocks(C2_TXT);
		String actualKey = ctoCheckForEnglishWords(cipherBlocks, false);
		findMinimumNumberOfBlocksNeededToDecrypt(actualKey);
	}
	
	private static void findMinimumNumberOfBlocksNeededToDecrypt(
			String actualKey) throws FileNotFoundException {
		System.out.println("Start of experiment...");
		String experimentOutput = "Results...\n";
		for (int blocksUsed = 1; blocksUsed <= cipherBlocks.size(); blocksUsed++){
			int countCorret = 0;
			for(int last = blocksUsed; last <= cipherBlocks.size(); last += 1){
				ArrayList<String> sublist = new ArrayList<String>();
				sublist = new ArrayList<String>(cipherBlocks.subList(last - blocksUsed, last));
				String newKey = ctoCheckForEnglishWords(sublist, true);
				if (newKey.equals(actualKey))
					countCorret += 1;
			}
			experimentOutput += "Ciphertext blocks used: " + blocksUsed + ". In total " 
								+ countCorret + "/" + (cipherBlocks.size() - blocksUsed + 1) + 
								" attacks have found the correct key.\n";
		}
		System.out.println(experimentOutput);
	}

	private static void setUpBigramFrequencyTable() throws FileNotFoundException {
		Scanner s = new Scanner(new File(BIGRAMS_TXT));
		while (s.hasNextLine()){
			String[] bigramFreq = s.nextLine().split(" ");
			double score = Double.parseDouble(bigramFreq[1]);
			bigramFrequencyTable.put(bigramFreq[0].toLowerCase(), score);
		}
		s.close();
	}

	private static void setUpEnglishWordsSet() throws FileNotFoundException {
		Scanner s = new Scanner(new File(WORDS_TXT));
		while (s.hasNextLine()){
			englishWords.add(s.nextLine().toLowerCase());
		}
		s.close();
	}

	private static String ctoCheckForEnglishWords(ArrayList<String> cipherBlocks, boolean experiment) throws FileNotFoundException{
		ArrayList<KeyScorePair> keyScores = new ArrayList<KeyScorePair>();
		
		for (int key = 0; key < Math.pow(2, 16); key++){
			String keyHexString = Utils.intToHexString(key);
			ArrayList<String> decryptedBlocks = Utils.decrypt(cipherBlocks, keyHexString);
			String text = Utils.block2Text(decryptedBlocks);
			int validEnglishScore = scoreValidEnglishWords(text);
			double bigramScore = scoreBigramFrequency(text);
			keyScores.add(new KeyScorePair(keyHexString, validEnglishScore + bigramScore));
		}
		KeyScorePair maxScore = new KeyScorePair("-1", Double.MIN_VALUE);
		for (KeyScorePair kc : keyScores){
			if (kc.score > maxScore.score){
				maxScore = kc;
			}
		}
		
		if (!experiment){
			System.out.println("FINAL RESULTS");
			System.out.println("The most likely key based on the score is - " + maxScore.hexKeyString + " Score for key: " + maxScore.score);
			ArrayList<String> dblocks = Utils.decrypt(cipherBlocks, maxScore.hexKeyString);
			String text = Utils.block2Text(dblocks);
			System.out.println("Decrypted text:\n" + text + "\n");
		}
		return maxScore.hexKeyString;
		
	}
	
	private static int scoreValidEnglishWords(String text){
		String[] words = text.split(" ");
		int score = 0;
		for (String word : words){
			if (englishWords.contains(word.toLowerCase()))
				score += 100*word.length();
			else
				score -= 10*word.length();
		}
		return score;
	}
	
	private static double scoreBigramFrequency(String text){
		text = text.replaceAll("[^a-zA-Z ]", " ").toLowerCase();
		double score = 0.0;
		for (int i = 0; i < text.length()-1; i++){
			String bigram = text.substring(i, i+2);
			if (bigramFrequencyTable.containsKey(bigram))
				score += bigramFrequencyTable.get(bigram);
		}
		return score;
	}
	
	private static class KeyScorePair{
		public String hexKeyString;
		public double score;
		public KeyScorePair(String hexKeyString, double score) {
			this.hexKeyString = hexKeyString;
			this.score = score;
		}
	}
}
