import java.io.*;
import java.util.*;
/////////////////////////////////////////////
class Node {
    public char charData;  // character
    public int freq;       // frequency of character
    public Node leftChild;
    public Node rightChild;

    public void displayNode() {
        System.out.print('{');
        System.out.print(charData);
        System.out.print("} ");
    }
}// end class Node
////////////////////////////////////////////
class Tree {
    public Node root;                                               // first node of tree
    public String path;
    //--------------------------------------------
    public Tree() {                                                  // constructor
        root = null;                                                 // no nodes in tree yet
    }

    public Tree(Tree obj) {
        root = null;
    }

    public Tree(char cVal, int fVal) {
        root = new Node();
        root.charData = cVal;
        root.freq = fVal;
    }

    public Tree(int sumFreq, Tree left, Tree right) {
        root = new Node();
        // Figures a no char in the root.
        root.charData = '-';
        root.freq = sumFreq;
        if (left != null) {
            root.leftChild = left.root;
        } else {
            root.leftChild = null;
        }
        if (right != null) {
            root.rightChild = right.root;
        } else {
            root.rightChild = null;
        }


    }
    //------------------------------------------------------------------------------------------------------------------
    public void traverse(int traverseType, String thread, char targetChar) {
        switch(traverseType) {
            case 1:
                System.out.print("\nPreorder traversal: ");
                preOrder(root);
                break;
            case 2:
                inOrder(root, thread, targetChar);
                break;
            case 3:
                System.out.print("\nPostorder traversal: ");
                postOrder(root);
                break;
        }
        System.out.println();
    }                                                                 //
    //------------------------------------------------------------------------------------------------------------------
    private void preOrder(Node localRoot) {
        if (localRoot != null) {
            System.out.print(localRoot.charData + " ");
            preOrder(localRoot.leftChild);
            preOrder(localRoot.rightChild);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private void inOrder(Node localRoot, String thread, char targetChar) {
        if (localRoot.leftChild == null && localRoot.rightChild == null) { // It's character, let's test it against targetChar
            if (localRoot.charData == targetChar) {
                path = thread;
            }
        } else {
            inOrder(localRoot.leftChild, thread+"0",targetChar);
            inOrder(localRoot.rightChild, thread+"1",targetChar);
        }
    }                                                                  //
    //------------------------------------------------------------------------------------------------------------------
    private void postOrder(Node localRoot) {
        if (localRoot != null) {
            postOrder(localRoot.leftChild);
            postOrder(localRoot.rightChild);
            System.out.print(localRoot.charData + " ");
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public void displayTree() {
        Stack globalStack = new Stack();
        globalStack.push(root);
        int nBlanks = 64;
        boolean isRowEmpty = false;
        System.out.println(".........................................................................");
        while (isRowEmpty==false) {
            Stack localStack = new Stack();
            isRowEmpty = true;

            for (int j=0; j<nBlanks; j++) {                            //
                System.out.print(' ');
            }

            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.charData);
                    System.out.print(" "+ "("+ temp.freq + ")");
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);

                    if (temp.leftChild != null || temp.rightChild != null) {
                        isRowEmpty = false;
                    }
                } else {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j=0; j<nBlanks*2-2; j++) {
                    System.out.print(' ');
                }
            }                                                         // end while globalStack not empty
            System.out.println();
            nBlanks /= 2;
            while (localStack.isEmpty() == false) {
                globalStack.push(localStack.pop());
            }
        }                                                             // end while isRowEmpty is false
        System.out.println(".........................................................................");
    }                                                                 // end displayTree()
}                                                                     // end class Tree
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
class CharFreqComparator implements Comparator<Tree> {
    /**
     *
     * Class compares two characters frequencies of two given one-node(!) Trees.
     */
    @Override
    public int compare(Tree x, Tree y) {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x.root.freq < y.root.freq) {
            return -1;
        }
        if (x.root.freq > y.root.freq) {
            return 1;
        }
        return 0;
    }
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public class HuffmanCode {
    // The priority queue, where Tree objects arranged by it's character frequency.
    static PriorityQueue<Tree> priorityQueue = null;

    // The resulting Huffman Tree.
    static Tree HuffmanTree = null;

    // Two sets for matching characters and their "binary" code.
    static ArrayList<Character> alphabetSet = new ArrayList<>();
    static String [] binarySet = new String [28];

    // Program main entry point.
    public static void main(String[] args) throws IOException {
        System.out.println("******************************************************");
        System.out.println("Program accept an arbitrary text and convert it to the Huffman code.");
        System.out.println("Also it decodes the binary code back to the initial text.");
        System.out.println("Written by V.Fursov 2022");
        System.out.println("******************************************************");
        System.out.println("Enter 'a' to analyze text from 'textToEncode.txt' file and create Huffman Tree.");
        System.out.println("Enter 'e' to encode text from 'textToEncode.txt' file.");
        System.out.println("Enter 's' to show Huffman tree.");
        System.out.println("Enter 'c' to calculate code table and show it.");
        System.out.println("Enter 'd' to decode text from binary back to 'decodedText.txt'");

        while (true) {
            int choice = getChar();
            switch (choice) {
                case 'a':
                    // Create the priority queue containing one-node Trees.
                    priorityQueue = createPriorityQueue("/home/viktorfursov/IdeaProjects/HuffmanCode/textToEncode");
                    // Generate Huffman Tree based on priority queue.
                    HuffmanTree = createHuffmanTree(priorityQueue);
                    System.out.println("Analyzing is successful.");
                    break;
                case 's':
                    HuffmanTree.displayTree();
                    break;
                case 'c':
                    for (char i = 'a'; i <= 'z'; i++)
                        alphabetSet.add(i);
                    // Add ' ' symbol...
                    alphabetSet.add(' ');
                    // And new line symbol '+'(Yes, I know, that's '\n')
                    alphabetSet.add('+');
//                    for (int i=0; i< alphabetSet.size(); i++)
//                        System.out.println(alphabetSet.get(i));
                    for (int j=0; j<alphabetSet.size(); j++) {
                        binarySet[j] = calculateCode(alphabetSet.get(j));
                        HuffmanTree.path = null;
                    }

                    System.out.println("Huffman code table: ");
                    for (int i=0; i<binarySet.length; i++) {
                        if (binarySet[i] != null) {
                            System.out.println(alphabetSet.get(i) + " --> " + binarySet[i]);
                        }
                    }
                    break;
                case 'd':
                    decodeText("/home/viktorfursov/IdeaProjects/HuffmanCode/encodedText",
                            "/home/viktorfursov/IdeaProjects/HuffmanCode/decodedText");
                    System.out.println("Text was successfully decoded.");
                    break;
                case 'e':
                    encodeText("/home/viktorfursov/IdeaProjects/HuffmanCode/textToEncode",
                            "/home/viktorfursov/IdeaProjects/HuffmanCode/encodedText");
                    System.out.println("Text was encoded.");
                    break;
                default:
                    System.out.print("Invalid entry\n");
            }                                                                 // end switch
        }                                                                     // end while
    }
    //------------------------------------------------------------------------------------------------------------------
    public static String calculateCode(char ch) {
        String str = "";
        HuffmanTree.traverse(2,str, ch);
        str = HuffmanTree.path;
        return str;
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void decodeText(String input, String output) throws IOException{
        char c;
        String decodedText = "";
        // Read input file, and map binary block back to symbols.
        try {
            File file = new File(input);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String str = new String(data, "UTF-8");
            String [] ar = str.split(" ");

            for (int x=0; x< ar.length; x++) {
                for(int y=0; y< binarySet.length; y++) {
                    if (ar[x].equals(binarySet[y])) {
                        decodedText += alphabetSet.get(y);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
        // Write Huffman code into output file.
        try (FileWriter fileWriter = new FileWriter(output)) {
            fileWriter.write(decodedText);
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static void encodeText(String input, String output) throws IOException{
        String encodedText = "";
        // Read input file, and convert sybmols to Huffman code.
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            int content;
            while ((content = br.read()) != -1) {
                for (int x=0; x<alphabetSet.size(); x++) {
                    if ((char) content == alphabetSet.get(x)) {
                        encodedText += binarySet[x] + " ";
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
        // Write Huffman code into output file.
        try (FileWriter fileWriter = new FileWriter(output)) {
            fileWriter.write(encodedText);
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public static PriorityQueue<Tree> createPriorityQueue(String pathToTextFile) throws IOException{
        /**
         * This method accepts a block of text, analyzes it's
         * letter frequency, creates Tree object with two fields:
         * character itself and it's frequency, and form a priority
         * queue, according frequency. Written V.Fursov. February 2022.
         */
        // The comparator for adding Trees to resulting priority queue.
        Comparator<Tree> comparator = new CharFreqComparator();
        // Resulting priority queue.
        PriorityQueue<Tree> resPriorityQueue = new PriorityQueue<>(10,comparator);

        String s;
        String text = "";

        try (BufferedReader br = new BufferedReader(new FileReader(pathToTextFile))) {
            while((s = br.readLine()) != null) {
                text += s;
                text += "+"; //  A new line placeholder.
            }
            // Truncate last '/n' new line symbol.
            text = text.substring(0,text.length()-1);
        } catch (IOException e) {
            System.out.println("I/O Error: " + e);
        }

        // Now do frequency analyzing of the input text.
        int[] freq = new int[text.length()];
        int i, j;
        //Converts given string into character array
        char string[] = text.toCharArray();

        for(i = 0; i <text.length(); i++) {
            freq[i] = 1;
            for(j = i+1; j <text.length(); j++) {
                if(string[i] == string[j]) {
                    freq[i]++;

                    //Set string[j] to 0 to avoid printing visited character
                    string[j] = '0';
                }
            }
        }

        // Now create Tree object with each character and their corresponding frequency.
        // Then add it to resulting priority queue.
        for(i = 0; i <freq.length; i++) {
            if(string[i] != '0') {
                Tree tree = new Tree(string[i], freq[i]);
                resPriorityQueue.add(tree);
            }
        }
//        while(!resPriorityQueue.isEmpty()){
//            System.out.print(" "+resPriorityQueue.peek().root.charData);
//            System.out.println(" "+resPriorityQueue.poll().root.freq);
//        }
        return resPriorityQueue;
    }
    //------------------------------------------------------------------------------------------------------------------
    public static Tree createHuffmanTree(PriorityQueue<Tree> pqt) {
        while (pqt.size() != 1) {
            Tree tree1 = pqt.poll();
            Tree tree2 = pqt.poll();
            Tree tree = new Tree(tree1.root.freq + tree2.root.freq,tree1, tree2 );
            pqt.add(tree);
        }
        return pqt.poll();
    }
    //------------------------------------------------------------------------------------------------------------------
    public static String getString() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        return s;
    }
    //------------------------------------------------------------------------------------------------------------------
    public static char getChar() throws IOException {
        String s = getString();
        return s.charAt(0);
    }
    //------------------------------------------------------------------------------------------------------------------
    public static int getInt() throws IOException {
        String s = getString();
        return Integer.parseInt(s);
    }
    //------------------------------------------------------------------------------------------------------------------
}                                                                             // end class treeApp
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
