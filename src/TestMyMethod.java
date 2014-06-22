import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TestMyMethod {
	
	public static void main(String [] s) throws ParseException, IOException{
		
		String[] words = loadFile();
		
		long usedMemory;
		int useType = 2;
		
		usedMemory = (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
		System.out.println("total"+usedMemory+" B , "+usedMemory/1024/1024+"MB");
		
		if(useType == 1)
			testSearchBySequence(words);
		else{
			List<List<Integer>> [] indexData = buildIndexData(words);
//			String xmlData = 
			testSearchByMyAlgorithm(indexData);
		}
		
//		System.out.println("free"+);
		
	}

	private static void testSearchByMyAlgorithm(List<List<Integer>>[] indexData)
			throws IOException {
		Timestamp begin;
		Timestamp end;
		while(true){
			
			System.out.println("please input key char");
			
			InputStreamReader isr = 
	            new InputStreamReader(System.in);
		    BufferedReader br = new BufferedReader(isr);
		    String key = br.readLine();
		    
		    System.out.println(" you input key is :"+key);
		    System.out.println(" search key in line:");
		    begin = new Timestamp(System.currentTimeMillis());
		    List<Integer> lines = search(indexData,key);
		    end = new Timestamp(System.currentTimeMillis());
		    for(Integer line : lines){
		    	
		    	System.out.println(line);
		    }
		    System.out.println("use time:"+(end.getTime()-begin.getTime())+" ms");
		}
	}

	private static void testSearchBySequence(String[] words) {
		int index = 1;
		
		Timestamp begin;
		
		 begin = new Timestamp(System.currentTimeMillis());
		
		for(String word:words){
			
			if(word.indexOf("ab")>=0){
				//System.out.println("line is ："+index);
			}
			
			index++;
			
		}
		Timestamp end = new Timestamp(System.currentTimeMillis());
		
		System.out.println(end+":"+begin);
		System.out.println(end.getTime()-begin.getTime());
	}

	private static String[] loadFile() throws IOException {
		URL url = TestMyMethod.class.getClassLoader().getResource("a.txt");
		
		File f = new File(url.getPath());
		
		List<String> wordList = org.apache.commons.io.FileUtils.readLines(f);
		
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
//		wordList.addAll(org.apache.commons.io.FileUtils.readLines(f));
		
		String[] words = wordList.toArray(new String[0]);
		
		System.out.println(" word size :"+words.length);
		
		for(int i =0;i<words.length;i++){
			
			words[i] = words[i].substring(0, words[i].indexOf(" "));
			
		}
		return words;
	}
	
	private static List<Integer> search(List<List<Integer>> [] indexData, String key) {
		List<Integer> result = new ArrayList<Integer>();
		
		int i=0,j=1;
		
		char[] ca = key.toCharArray();
		
		List<Integer>[] queue = new ArrayList[ca.length-1];
		
		int queueIndex = 0;
		
		while(j<ca.length){
			
			char firstChar = ca[i];
			char secondChar = ca[j];
			
			int firstCharCode = encodeChar2Code(firstChar)-97;
			int secondCharCode = encodeChar2Code(secondChar)-97;
			
			queue[queueIndex] = searchLine(indexData,firstCharCode,secondCharCode);
			
			i=j;
			j++;
			
			queueIndex++;
			
		}
		
		
		
		if(queue.length==1)
			result = queue[0];
		else{
			
			int loopObjectIndex =  0;
			
			int loopMinLength = Integer.MAX_VALUE;
			
			for(int indexTemp = 0;indexTemp<queue.length;indexTemp++){
				
				if(queue[indexTemp].size()<loopMinLength){
					loopObjectIndex = indexTemp;
					loopMinLength = queue[indexTemp].size();
				}
			}
			
			
			for(int indexTemp = 2;indexTemp<queue[loopObjectIndex].size();indexTemp++){
				
				Integer queueFirstIndex = queue[loopObjectIndex].get(indexTemp);
				
				boolean flag = true;
				for(i=0;i<queue.length;i++){
					if(i==loopObjectIndex)
						continue;
					
					if(!queue[i].contains(queueFirstIndex)){
						flag = false;
						break;
					}
				}
				if(flag==true){
					
					result.add(queueFirstIndex);
				}
				
			}
		
		}
		return result;
	}

	private static List<Integer> searchLine(List<List<Integer>>[] indexData,
			int firstCharCode, int secondCharCode) {
		
		List<List<Integer>> secondDim = indexData[firstCharCode];
		
		for(List<Integer> thirdDim : secondDim){
			
			if(thirdDim.size()==0)
				continue;
			
			if(thirdDim.get(0)==secondCharCode)
				return thirdDim;
			
		}
		
		
		return null;
	}

	public static List<List<Integer>> [] buildIndexData(String[] words){
		
		
		List<List<Integer>> [] resultTemp = new ArrayList[7000];
		
		
		for(int z=0;z<words.length;z++){
			
			char[] ca = words[z].toCharArray();
			
			if(ca.length<=1)
				continue;
			
			int i=0,j=1;
			
			while(j<ca.length){
				
				char firstChar = ca[i];
				char secondChar = ca[j];
				
				int firstCharCode = encodeChar2Code(firstChar)-97;
				int secondCharCode = encodeChar2Code(secondChar)-97;
				if(firstCharCode>=0&&secondCharCode>=0)
					resultTemp = putIndexData(resultTemp,firstCharCode,secondCharCode,z+1,ca.length==2);
				
				i=j;
				j++;
				
			}
			
		}
		
		return resultTemp;
	}

	private static List<List<Integer>>[] putIndexData(List<List<Integer>>[] result, int firstCharCode, int secondCharCode,
			int lineCode,boolean isMatched) {
		
		List<List<Integer>> secondDim = result[firstCharCode];
		List<Integer> thirdDim = null;
		
		if(secondDim==null){
			secondDim =new  ArrayList<List<Integer>>();
			
		}else{
			thirdDim = getThirdDim(secondCharCode,secondDim);
			
		}
	
		if(thirdDim==null){
				thirdDim = new ArrayList<Integer>();
				thirdDim.add(0, secondCharCode);
				int insertIndex = getInsertIndex(secondDim,0,0,secondCharCode);
				secondDim.add(insertIndex==-1?0:insertIndex,thirdDim);
		}
		
		if(isMatched)
			thirdDim.add(1,lineCode);
		else
			thirdDim.add(lineCode);
		
		result[firstCharCode] = secondDim;
		
		return result;
		
	}

	private static List<Integer> getThirdDim(int secondCharCode, List<List<Integer>> secondDim) {
	
		List<Integer> result = null;
		
		
		int index = binSearch(secondDim, 0, secondDim.size() - 1, secondCharCode);
		
		if(index!=-1)
			result = secondDim.get(index);

		return result;
		
	}
	
	 public static int binSearch(List<List<Integer>> secondDim, int start, int end, int key) {
//	        int mid = (end - start) / 2 + start;   
//	        if (((Integer)secondDim.get(mid).get(0)).intValue() == key) {
//	            return mid;   
//	        }   
//	        if (start >= end) {   
//	            return -1;   
//	        } else if (key > ((Integer)secondDim.get(mid).get(0)).intValue()) {
//	            return binSearch(secondDim, mid + 1, end, key);   
//	        } else if (key < ((Integer)secondDim.get(mid).get(0)).intValue()) {   
//	            return binSearch(secondDim, start, mid - 1, key);   
//	        }   
//	        return -1;
		 
		 int result = -1;
		 
		 for(int i=0;i<secondDim.size();i++){
			 
			 int thirdDimCode = secondDim.get(i).get(0);
			 
			 if(thirdDimCode==key)
				 return i;
			 
		 }
		 
		 return result;
	    } 

	private static int encodeChar2Code(char data) {

		int result = Integer.MIN_VALUE;
		
		result = (int)data;
		
		return result;
	}
	
	private static int getInsertIndex(List<List<Integer>> secondDim,int start,int end,int key){
		
		if(secondDim==null||secondDim.size()<=0)
			return 0;
		
		for(int i =0;i<secondDim.size();i++){
			
			List<Integer> thirdDim = secondDim.get(i);
			
			if(((Integer)thirdDim.get(0)).intValue()>=key)
				return i;
			
		}
		return -1;
		
	}

}
