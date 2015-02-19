import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @since 16/02/2015
 * @author antonbelev
 * Cipher Text Only Attack
 */
public class CTO {
	public static Set<String> englishWords = new HashSet<String>();
	public static void main(String[] args) throws FileNotFoundException{
		setUpEnglishWordsSet();
		ctoCheckForEnglishWords();
	}
	
	private static void setUpEnglishWordsSet() throws FileNotFoundException {
		Scanner s = new Scanner(new File("words"));
		while (s.hasNextLine()){
			englishWords.add(s.nextLine().toLowerCase());
		}
		s.close();
	}

	private static void ctoCheckForEnglishWords() throws FileNotFoundException{
		ArrayList<KeyScorePair> keyScores = new ArrayList<KeyScorePair>();
		String fileIn = "c2.txt";
		ArrayList<String> cipherBlocks = DecryptAllBlocks.getCipherBlocks(fileIn);
		for (int key = 0; key < Math.pow(2, 16); key++){
			String keyHexString = intToHexString(key);
			ArrayList<String> decryptedBlocks = DecryptAllBlocks.decrypt(cipherBlocks, keyHexString);
			String text = Block2Text.block2Text(decryptedBlocks);
			int score = scoreTextBasedOnPresenceOfEnglishWords(text);
			keyScores.add(new KeyScorePair(keyHexString, score));
		}
		KeyScorePair maxScore = new KeyScorePair("-1", -1);
		for (KeyScorePair kc : keyScores){
			if (kc.score > maxScore.score){
				maxScore = kc;
			}
		}
		System.out.println("Potential key decimal - " + maxScore.hexKeyString + " Score for key: " + maxScore.score);
		ArrayList<String> dblocks = DecryptAllBlocks.decrypt(cipherBlocks, maxScore.hexKeyString);
		String text = Block2Text.block2Text(dblocks);
		System.out.println("Decrypted text:\n" + text);
	}
	
	private static void ctoCheckEnglishLetters() throws IOException{
		for (int key = 0; key < Math.pow(2, 16); key++){
			String fileIn = "c2.txt";
			String fileOut = "c2_decrypted.txt";	
			String keyHexString = intToHexString(key);
			if(key%1000 == 0)
				System.out.println(keyHexString);
			String[] arguments = {fileIn, fileOut, keyHexString};
			DecryptAllBlocks.main(arguments);
			String[] arguments2 = {"c2_decrypted"};
			//System.out.println(keyHexString);
			Block2Text.main(arguments2);
			if (checkDecryptedTextForEnglishChars("c2_decrypted_t.txt")){
				System.out.println("Potential key decimal - " + key 
						+ " hex - " + keyHexString);
				DecryptCipher.decrypt("c2", keyHexString);
			}
		}
	}
	
	private static boolean checkDecryptedTextForEnglishChars(String fileName) throws IOException {
		File f = new File(fileName);
	    InputStream targetStream = new FileInputStream(f);
	    int nextByte = targetStream.read();
		while (nextByte != -1 ){
			if (!isLatinLetter(nextByte)){
				targetStream.close();
				return false;
			}
			nextByte = targetStream.read();
		}
		targetStream.close();
		return true;
	}
	
	private static int scoreTextBasedOnPresenceOfEnglishWords(String text){
		String[] words = text.split(" ");
		int score = 0;
		for (String word : words){
			if (englishWords.contains(word.toLowerCase())){
				score += 100;
			}
		}
		return score;
	}
	
	public static boolean isLatinLetter(int c) {
	    return (c >= (int)'A' && c <= (int)'Z') || (c >= (int)'a' && c <= (int)'z')
	    		|| c == (int)' ' || c == (int)'.' || c == (int)',' || c == (int)'\'' || c == (int)'!' || c == (int)'?';
	}

	private static String intToHexString(int i){
		return String.format("0x%04x", i);
	}
	
	private static class KeyScorePair{
		public String hexKeyString;
		public int score;
		public KeyScorePair(String hexKeyString, int score) {
			this.hexKeyString = hexKeyString;
			this.score = score;
		}
	}
}
