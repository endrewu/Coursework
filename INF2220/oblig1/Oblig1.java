import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

//Node-class for use in the tree
class Node {
    Node left;
    Node right;

    String word;
    public Node(String word) {
        this.word = word;
    }
}

class BinaryTree {
    Node root;
    int[] nodeAtDepth = new int[30]; //Array to store how many nodes are at each depth, a cheap solution for simplisity

    int nmbNodes = 0;   //number of nodes in the tree
    //int depthCnt = 0; //counts the depth while traversing the tree
    int depthTot = 0;   //counter adds together each nodes depth to later calculate average depth
    int avgDepth = 0;   //average depth of the tree
    int tempDepth = 0;  //counter keeps tally on the depth at which nodes are inserted
    int maxDepth = 0;   //depth of the tree
    final char[] alphabet = {'a', 'b', 'c', 'd','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','æ','ø','å'};

    public BinaryTree() {
        try {
            Scanner s = new Scanner(new File("ordbok1_utf.txt"));
            String tmp = s.next();
            root = new Node(tmp);         //sets the first word of the dictionary as root
            nmbNodes++; nodeAtDepth[0]++; //update counters
            while (s.hasNext()) {         //inserts all subsequent words from the dictionary
                tmp = s.next();
                insert(root, tmp);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        System.out.println("Binary Tree is created"); //if no errors were encountered the tree is successfully created
    }

    public void insert(Node n, String word) {
        if (word.compareTo(n.word) == 0) return; //if the word already exists, return
        if (word.compareTo(n.word) < 0) {        //go left
            tempDepth++;                         //update counter
            if (n.left != null) {                //if left node exists, insert recursively
                insert(n.left, word);
            } else {                             //if no left node exists, create new node
                n.left = new Node(word);
                nmbNodes++;
                depthTot += tempDepth;
                nodeAtDepth[tempDepth]++;
            }
        } else {                                 //go right
            tempDepth++;
            if (n.right != null) {               //if right node exists, insert recursively
                insert(n.right, word);
            } else {                             //if no right node exists, create new node
                n.right = new Node(word);
                nmbNodes++;
                depthTot += tempDepth;
                nodeAtDepth[tempDepth]++;
            }
        }
        if (tempDepth > maxDepth) maxDepth = tempDepth; //update depth of the tree if applicable
        tempDepth = 0; //reset counter
    }

    //lookup words in the dictionary
    public String lookup(Node n, String lookup) {
        String ret = null;                   //String to be returned at the end of method
        if (lookup.compareTo(n.word) == 0) { //if word is found return the word
            return n.word;
        }
        if (lookup.compareTo(n.word) < 0) {  //go left
            if (n.left == null) {            //if left node doesn't exist return null
                return null;
            }
            ret = lookup(n.left, lookup);    //if left node exists, lookup recursively
        }
        if (lookup.compareTo(n.word) > 0) {  //go right
            if (n.right == null) {           //if right node doesn't exist return null
                return null;
            }
            ret = lookup(n.right, lookup);   //if right node exists, lookup recursively
        }
        return ret;
    }

    public void remove(Node n, String word) {
        if(n == null) return;                        //empty tree
        if (word.compareTo(n.word) == 0) {           //Word to remove is found
            Node parent = getParent (root, word);    //get pointer to parent

            if (n.left == null && n.right == null) { //No children
                n = null;
                nmbNodes--;                          //decrease number of nodes
                return;
            }

            if ((n.left == null && n.right != null) || (n.left != null && n.right == null)) { //One child
                if (parent.left == n) {              //if left node of parent
                    if (n.left == null) parent.left = n.right;  //if right node exists, replace
                    if (n.right == null) parent.left = n.left;  //if left ndoe exists, replace
                }
                if (parent.right == n) {             //if right node of parent
                    if (n.left == null) parent.right = n.right; //if right node exists, replace
                    if (n.right == null) parent.right = n.left; //if left node exissts, replace
                }
                nmbNodes--;                          //decrease number of nodes
                return;
            }

            if (n.left != null && n.right != null) { //Two children
                Node min = getMin(n.right); //Get the smallest node from the right subtree
                remove (root, min.word);    //Remove the smallest word to avoid duplicates, also reduces the number of nodes

                if (parent.left == n) parent.left = min;   //if left child of parent, replace with min
                if (parent.right == n) parent.right = min; //if right child of parent, replace with min
                min.left = n.left;  min.right = n.right;   //update min's pointers to it's new children
                return;
            }
        }
        if (word.compareTo(n.word) < 0) {   //if word to remove is not found, try to find it
            remove(n.left, word);
        }
        if (word.compareTo(n.word) > 0) {   //if word to remove is not found, try to find it
            remove(n.right, word);
        }
    }

    //method copied from assignment
    public String[] similarOne(String word){
        char[] word_array = word.toCharArray();
        char[] tmp;
        String[] words = new String[word_array.length-1];
        
        for(int i = 0; i < word_array.length - 1; i++){
            tmp = word_array.clone();
            words[i] = swap(i, i+1, tmp);
        }

        return words;
    }
    
    //method copied from assignment
    public String swap(int a, int b, char[] word){
        char tmp = word[a];
        word[a] = word[b];
        word[b] = tmp;

        return new String(word);
    }

    public String[] similarTwo(String word) {
        char[] word_array = word.toCharArray();  //make a character array from word
        char[] tmp;
        String[] words = new String[word_array.length*29-(word_array.length)];  //array to store words generated

        int index = 0;  //index for words
        for (int i = 0; i < word_array.length; i++) { //for every single character in the word
            for (int j = 0; j < 29; j++) {            //replace it with every letter from a to å
                tmp = word_array.clone();
                tmp[i] = alphabet[j];                 //replace a letter with every character from char[] alphabet
                String newWord = new String(tmp);     //make new String
                if (!newWord.equals(word)) {          //store in words as long as it isn't the original word
                    words[index] = new String(tmp);
                    index ++;
                }
            }
        }
        return words;   //return words
    }

    //assume one letter missing from word found in dictionary
    public String[] similarThree(String word) {
        char[] word_array = word.toCharArray();
        char[] tmp = new char[word_array.length+1];
        String[] words = new String[(word_array.length+1)*29];

        int index = 0;                              //index for words
        for (int i = 0; i < tmp.length; i++) {      //for every position in target word, i denotes position where alphabet will be put in
            for (int j = 0; j < tmp.length; j++) {  //create an open space for possible lost chars when i == j, j fills in all letters
                if (j < i) {                        //as long as j < i copy from word_array to the same space in tmp
                    tmp[j] = word_array[j];         
                } else if (j > i) {                 //if j > i copy char from word_array and put it into tmp, one space to the right
                    tmp[j] = word_array[j-1];
                }
            }
            for (int k = 0; k < 29; k++) {          //try every character of the alphabet in position i
                tmp[i] = alphabet[k];
                words[index] = new String(tmp);
                index++;
            }
        }

        return words;
    }

    //assume one char is unwanted
    public String[] similarFour(String word) {
        char[] word_array = word.toCharArray();
        char[] tmp = new char[word_array.length-1];
        String[] words = new String[word_array.length];

        int index = 0;  //index for words

        for (int i = 0; i < word_array.length; i++) {       //for every char in the word, i denotes the one to be dropped
            for (int j = 0; j < word_array.length; j++) {   //j moves chars from word_array to tmp
                if (j < i) {                                //if j < i move char to the same position in tmp
                    tmp[j] = word_array[j];
                } else if (j > i) {                         //if j > i move chars one space to the left
                    tmp[j-1] = word_array[j];
                }
            }
            words[index] = new String(tmp);                 //insert word into words
            index++;
        }

        return words;
    }

    //Goes left until the end and returns the node containing the lowest word in a subtree
    public Node getMin(Node n) {
        if (n.left == null) return n;
        return getMin(n.left);
    }

    //Goes right until the end and returns the node containing the biggest word in a subtree
    public Node getMax(Node n) {
        if (n.right == null) return n;
        return getMax(n.right);
    }

    public Node getParent(Node n, String word) {
        if (word.compareTo(n.word) == 0 && n == root) {
            //Found root special case
            //compareTo should not otherwise return 0
            return null;
        }
        if (word.compareTo(n.word) < 0) {
            if(word.compareTo(n.left.word) == 0) {
                return n;  //If left node is target then return n as parent
            } else {
                return getParent(n.left, word);  //recursive step
            }
        }
        if (word.compareTo(n.word) > 0) {
            if (word.compareTo(n.right.word) == 0) {
                return n;  //If right node is target then return n as parent
            } else {
                return getParent(n.right, word);  //recursive step
            }
        }

        return null;
    }

    //prints statistics
    public void printStatistics() {
        System.out.println("Depth of the tree is " + maxDepth);
        System.out.println("Number of nodes is " + nmbNodes);
        for (int i = 0; i < maxDepth+1; i++) {
            System.out.println("There are " + nodeAtDepth[i] + " nodes at the depth of " + i);
        }
        System.out.println("The average depth of the tree is " + depthTot/nmbNodes);
        System.out.println("The first and last word of the dictionary is " + getMin(root).word + " and " + getMax(root).word);
    }
}

class Menu {
    Menu(BinaryTree bt) {
        //infinite loop
        while(true) {
            System.out.println("\nType in a norwegian word to compare to the dictionary\n Type in the letter q to end dictionary search\n Type in the letter s to print statistics");
            System.out.print("> ");
            Scanner s = new Scanner(System.in);
            String lookup = s.next().toLowerCase();
            if(lookup.equals("q")) return;              //'q' terminates loop
            if(lookup.equals("s")) bt.printStatistics();//'s' prints statistics
            else {                                      //all else initiates searching
                long startTime;             //Starttime, used to calculate time to retrieve similar words
                long stopTime;              //Stoptime, used to calculate time to retrieve similar words
                int nmbSimilarWords = 0;    //counter of similar words generated

                String word = bt.lookup(bt.root, lookup);   //looks up word given
                if (word != null) {                         //if successful prints notice
                    System.out.println("\nFound " + word + " in the dictionary, the word appears to be correct");
                } else if (word == null) {                  //if not successful generates all possible similar words
                    startTime = System.nanoTime();          //initiate timer
                    String[] one = bt.similarOne(lookup);
                    String[] two = bt.similarTwo(lookup);
                    String[] three = bt.similarThree(lookup);
                    String[] four = bt.similarFour(lookup);

                    ArrayList<String> similarWords = new ArrayList<String>(); //arraylist to store all similar words with hits

                    for (int i = 0; i < one.length; i++) {      //iterate similar words method one
                        word = bt.lookup(bt.root, one[i]);
                        if (word != null && !similarWords.contains(word)) { //if unique, store in similarWords
                            similarWords.add(word);
                            nmbSimilarWords++;
                        }
                    }

                    for (int i = 0; i < two.length; i++) {      //iterate similar words method two
                        word = bt.lookup(bt.root, two[i]);
                        if (word != null && !similarWords.contains(word)) {  //if unique, store in similarWords
                            similarWords.add(word);
                            nmbSimilarWords++;
                        }
                    }
                    for (int i = 0; i < three.length; i++) {    //iterate similar words method three
                        word = bt.lookup(bt.root, three[i]);
                        if (word != null && !similarWords.contains(word)) {  //if unique, store in similarWords
                            similarWords.add(word);
                            nmbSimilarWords++;
                        }
                    }
                    for (int i = 0; i < four.length; i++) {     //iterate similar words method four
                        word = bt.lookup(bt.root, four[i]);
                        if (word != null && !similarWords.contains(word)) {  //if unique, store in similarWords
                            similarWords.add(word);
                            nmbSimilarWords++;
                        }
                    }
                    stopTime = System.nanoTime();           //stop timer
                    if (similarWords.size() > 0) {          //if similar words are found:
                        System.out.println("\n" + lookup + " was not found, did you mean:");  //print a message
                        while (!similarWords.isEmpty()) {   //while more words exists
                            System.out.println(similarWords.remove(0));  //print them to terminal

                        }
                        //print number of words found and time taken to find them
                        System.out.println("\t found " + nmbSimilarWords + " possible words in " + ((stopTime-startTime)/1000000) + " milliseconds");
                    } else {
                        //if no words were found, print message
                        System.out.println("Could not find " + lookup + " or similar words in the dictionary");
                    }
                }
            }
        }
    }
}

//Initialize BinaryTree
public class Oblig1 {
    public static void main (String[] args) {
        System.out.println("Initializing");
        BinaryTree bt = new BinaryTree();

        //quiet and hard coded removal and insertion of the word "familie", could easily have been put in the menu to allow users to remove words
        bt.remove(bt.root, "familie");
        bt.insert(bt.root, "familie");

        //initializes menu
        Menu m = new Menu(bt);
    }
}