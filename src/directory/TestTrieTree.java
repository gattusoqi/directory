package directory;

import java.util.*;

/**
 * Tries数据结构（字典树） 这里只是最基本的实现，可判断某个单词是否出现过，单词出现频数等，根据需要可做其它扩展
 * 
 * @author
 * 
 */
public class TestTrieTree {

	private TrieNode root;
	private char[] characterTable = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k',
			'l', // 遍历的时候使用
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z' };

	public TestTrieTree() {
		root = new TrieNode();
	}

	/**
	 * 插入字符串
	 * 
	 * @param word
	 */
	public void insert(String word) {
		TrieNode node = root;
		word = word.trim();
		for (int i = 0; i < word.length(); i++) {
			if (!(node.children.containsKey(word.charAt(i)))) {
				node.children.put(word.charAt(i), new TrieNode());
			}
			node = node.children.get(word.charAt(i));
		}
		node.terminable = true;
		node.count++;
	}

	/**
	 * 查找某个字符串
	 * 
	 * @param word
	 * @return
	 */
	public boolean find(String word) {
		TrieNode node = root;
		for (int i = 0; i < word.length(); i++) {
			if (!(node.children.containsKey(word.charAt(i)))) {
				return false;
			} else {
				node = node.children.get(word.charAt(i));
			}
		}
		return node.terminable; // 即便该字符串在Trie路径中，也不能说明该单词已存在，因为它有可能是某个子串
	}

	/**
	 * 删除某个字符串（必须是个单词，而不是前缀）
	 * 
	 * @param word
	 */
	public void delete(String word) {
		if (!find(word)) {
			System.out.println("no this word.");
			return;
		}
		TrieNode node = root;
		deleteStr(node, word);
	}

	public boolean deleteStr(TrieNode node, String word) {
		if (word.length() == 0) {
			node.terminable = false; // 不要忘了这里信息的更新
			return node.children.isEmpty();
		}
		if (deleteStr(node.children.get(word.charAt(0)), word.substring(1))) {
			node.children.remove(word.charAt(0));
			if (node.children.isEmpty() && node.terminable == false) { // 注意第二个条件，假如有"abcd"与"abc",删除abcd时，要判断中间路径上是不是另一个子串的结束
				return true;
			}
		}
		return false;
	}

	/**
	 * 以字典序输出Tire中所有出现的单词及频数
	 */
	public void traverse() {
		StringBuffer word = new StringBuffer("");
		TrieNode node = root;
		traverseTrie(node, word);
	}

	public void traverseTrie(TrieNode node, StringBuffer word) {
		if (node.terminable) {
			System.out.println(word + "------" + node.count);
			if (node.children.isEmpty())
				return;
		}
		for (int i = 0; i < characterTable.length; i++) {
			if (!(node.children.containsKey(characterTable[i])))
				continue;
			traverseTrie(node.children.get(characterTable[i]),
					word.append(characterTable[i]));
			word.deleteCharAt(word.length() - 1);
		}
	}
}

/**
 * Trie结点类
 * 
 * @author
 * @param <T>
 */
class TrieNode {
	public boolean terminable; // 是不是单词结尾（即叶子节点）
	public int count; // 单词出现频数
	public Map<Character, TrieNode> children = null;

	public TrieNode() {
		terminable = false;
		count = 0;
		children = new HashMap<Character, TrieNode>();
	}
}