import java.io.*;
import java.util.*;

public class Compress {
	
	static Scanner k = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		String operation;
		do{
			System.out.println("Enter the command : Compress(C) | Decompress(D) | Quit(Q)");
			operation = k.next();
			
			if (operation.equals("C"))
				compress();
			
			else if(operation.equals("D"))
			Decompress();
						
			else if(!operation.equals("Q"))
				System.out.println("Incorrect input.");
		}while(!operation.equals("Q"));
		
	}

	
	
	
	public static void compress() {
		
		System.out.print("Enter the file to compress : ");
		String file = k.next();
		
		ArrayList<Character> arr = openFile(file);
		String text = readFile(file);
	    String[] binary = charToBinary(arr);
	    String newText = binarytext(arr,binary,text); 	
	    writeBinary(file+".S",newText,arr,text);
	    System.out.println(file+".S created.");
	}
	
	public static ArrayList<Character> openFile(String file) {
    	
    	ArrayList<Character> arr = new ArrayList<Character>();
	    Scanner input = null;
	    try{
	      input = new Scanner(new File(file));
	      int count = 0;
	      while(input.hasNextLine()){
	    	  if(count != 0)
	    		  putArr(arr,"\n");
	    	  String line = input.nextLine();
	    	  putArr(arr,line);
	    	  count++;
	      }
	 
	    }catch(FileNotFoundException e){
	      System.out.println("File not found! ");
	      System.exit(0);
	    }
	    input.close();
	    
	    return arr;
	    
    }
	
	public static String readFile(String file) {
    	
		String text = "";
	    Scanner input = null;
	    try{
	      input = new Scanner(new File(file));
	      int count = 0;
	      while(input.hasNextLine()){
	    	if(count !=0)
	    		text+= "\n";
	        String line = input.nextLine();
	        text += line;
	        count++;
	      }
	 
	    }catch(FileNotFoundException e){
	      System.out.println("File not found! ");
	      System.exit(0);
	    }
	    input.close();
	    return text;
	    
    }
	
	public static void putArr(ArrayList<Character> arr, String line) {
		for(int i=0; i<line.length(); i++)
			if(! (arr.contains(line.charAt(i)) ))
				arr.add(line.charAt(i));
	}
	
	public static String[] charToBinary(ArrayList<Character> arr) {
		
		String[] binary = new String[arr.size()];
	    for(int i=0; i<binary.length; i++)
	    	binary[i] = Integer.toBinaryString(i);
	    int len = binary[binary.length-1].length();
	    
	    for(int i=0; i<binary.length; i++)
	    	while(binary[i].length() < len)
	    		binary[i] = "0"+binary[i];
	    
		return binary;
		
	}
	
	public static String binarytext(ArrayList<Character> arr,String[] binary,String text) {
		String newText = "";
	    for(int i=0; i<text.length(); i++) {
	    	int j;
	    	for(j=0; j<arr.size(); j++) {
	    		if( text.charAt(i) == arr.get(j) )
	    			break;
	    	}
	    	newText += binary[j];
	    }
	    return newText;
	}
		
	public static void writeBinary(String name,String write,ArrayList<Character> arr,String text) {
		
		byte numOfChar = (byte)arr.size();
		int textLen = text.length();
		  ObjectOutputStream out = null;
		  try {

		    out = new ObjectOutputStream(new FileOutputStream(name));
		    
		    out.writeByte(numOfChar); 
		    out.writeInt(textLen);  
		    for(int i=0; i<numOfChar; i++) 
		    	out.writeChar(arr.get(i)); 
		    	    
		    int add = 8 - (write.length() % 8);
		    for(int i=0; i<add; i++)
		    	write += "0";
		    String[] newTextByte = new String[write.length()/8];
		    for(int i=0; i<newTextByte.length; i++) {
		    	newTextByte[i] = write.substring(0,8);
		    	write = write.substring(8);
		    }
		    int[] convert = new int[newTextByte.length];
		    for(int i=0; i<convert.length; i++)
		    	convert[i] = Integer.parseInt(newTextByte[i],2);
		    byte[] lastConv = new byte[convert.length];
		    
		    for(int i=0; i<lastConv.length;i++)
		    	lastConv[i] = (byte)convert[i];
		    
		    for(int i=0; i<lastConv.length; i++)
		    	out.writeByte(lastConv[i]);

		    out.close();
		  } catch(FileNotFoundException e) {
		    System.out.println("File does not exist");
		  } catch (IOException e) {
		    System.out.println("io error");
		  }
	}

	
	public static void Decompress() {
		System.out.print("Enter the file to decompress : ");
		String file = k.next();
		
		String binarySentence = readBinary(file);
		String sentence = convertSentence(binarySentence);
		textwrite(sentence,file);
		System.out.println(file.substring(0, file.length()-2) + " created.");
	}
	
	public static String readBinary(String file) {
		
		String s = "";
	    ObjectInputStream inpStream = null;
	    try{

	      inpStream = new ObjectInputStream(new FileInputStream(file));
	      byte numOfChar;
	      numOfChar = inpStream.readByte();
	      int howManyBit = Integer.toBinaryString(numOfChar-1).length();
      	  s += numOfChar;
      	  s += " ";
      	  int textLen;
      	  textLen = inpStream.readInt();
      	  int totalBitToRead = (textLen*howManyBit)/8;
      	  if(textLen%8 > 0)
      		  totalBitToRead++;
      	  
	      for(int i=0; i<numOfChar; i++) {
	    		char c = inpStream.readChar();
	    		s += c;
	      }
	      byte b = 0;
	      int oldLength = s.length();
	      for(int i=0; i<totalBitToRead; i++) {
	  		  b = inpStream.readByte();
	  		  s += String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
	      }
	      inpStream.close();
	      s = s.substring(0, oldLength+ textLen*howManyBit);
	      
	    }catch(FileNotFoundException e){
	      System.out.println("file does not exist");
	      System.exit(0);
	    }catch(IOException e){
	      System.out.println("io error");
	      System.exit(0);
	    }
	    
	return s;
		
	}

	public static String convertSentence(String binary) {
		String sentence = "";
		
		int howManyChar = Integer.parseInt(binary.substring(0,binary.indexOf(" ")));
		int howManyBit = Integer.toBinaryString(howManyChar-1).length();
		binary = binary.substring(binary.indexOf(" ")+1);
		String chars = binary.substring(0,howManyChar);
		binary = binary.substring(howManyChar);
		ArrayList<Character> charlar = new ArrayList<Character>();
		for(int i=0; i<chars.length(); i++)
			charlar.add(chars.charAt(i));
		String[] charToStr = charToBinary(charlar);
		ArrayList<String> arr = new ArrayList<String>();
		do{
			arr.add(binary.substring(0,howManyBit));
			binary = binary.substring(howManyBit);
		}while(!binary.equals(""));
		for(int i=0;i<arr.size(); i++) {
			int j;
			for(j=0; j<charToStr.length; j++)
				if(arr.get(i).equals(charToStr[j]))
					sentence += charlar.get(j);
		}
			
		return sentence;
	}

	public static void textwrite(String sentence,String fileName) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(fileName.substring(0,fileName.length()-2));
			writer.println(sentence);
		}
		catch(FileNotFoundException e) {
			System.out.println("file does not exist.");
			System.exit(0);
		}
		writer.close();
	}
	
	
}
