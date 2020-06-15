import java.io.*;
import java.util.*;

public class NLP {

    public static void processText() throws FileNotFoundException {
        InputStreamReader reader;
        BufferedReader br;
        List<Integer> potentials = new ArrayList<>();
        List<String> tokens = new ArrayList<>();

        try {
            reader = new InputStreamReader(new FileInputStream("warandpeace.txt"));
            br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                line = br.readLine();
                if (line != null) {
                    //Whitespace splitting of text into tokens and add all into result array
                    String[] result = line.split("—|\\s+");
                    tokens.addAll(Arrays.asList(result));
                }
            }

            //Check for capitalisation: if capitalised, add index to the potentials list
            for (int i = 0; i < tokens.size(); i++) {
                String token = tokens.get(i);
                if (token.length() > 0 && Character.isUpperCase(token.charAt(0))) {
                    potentials.add(i);
                }
            }

        } catch (IOException e) {
            System.out.println(e);
        }

        //Remove start of sentence capitalised tokens from the list of potentials
        potentials = removeStartOfSentence(tokens, potentials);

        //Assume potentials is now the true list of proper nouns
        String redacted = redactProperNouns(tokens, potentials);

        //Redirect output to write to text file
        PrintStream redactedFile = new PrintStream("warandpeace-redacted.txt");
        System.setOut(redactedFile);
        System.out.println(redacted);

    }

    public static List<Integer> removeStartOfSentence(List<String> tokens, List<Integer> potentials) {
        //Iterate through potentials and discount start of sentence capitals
        List<Integer> properNouns = new ArrayList<>();
        for (Integer p : potentials) {
            if (p == 0) {
                properNouns.add(p);
                continue;
            }
            int x = p - 1;
            String previous = tokens.get(x);
            String current = tokens.get(p);

            //Determine start of sentence by checking the last char of the previous token
            if (previous.length() > 0) {
                String lastChar = previous.substring(previous.length() - 1);
                try {
                    if (!lastChar.matches("[.!?\\-]")) {
                        properNouns.add(p);
                    }
                    /*
                    Check if token already appears in properNouns array list.
                    If it exists, add the token as is a verified proper noun which occurs at the start of a sentence
                     */
                    List<Integer> toAdd = new ArrayList<>();
                    for (Integer prop : properNouns) {
                        String text = tokens.get(prop);
                        if (text.equals(current)) {
                            toAdd.add(p);
                        }
                    }
                    properNouns.addAll(toAdd);

                } catch (NumberFormatException e) {
                    System.out.println("e");
                }
            }
        }
        removeI(tokens, properNouns);
        return properNouns;
    }

    //Remove "I" from proper nouns list
    public static void removeI(List<String> tokens, List<Integer> properNouns) {
        properNouns.removeIf(p -> tokens.get(p).equals("I"));
    }

    //Get tokens to redact via proper nouns list of indices and replace each char with an asterisk
    public static String redactProperNouns(List<String> tokens, List<Integer> properNouns) {
        String tokenToRedact;
        List<String> redactedTokens = new ArrayList<>(tokens);
        for (Integer p : properNouns) {
            tokenToRedact = tokens.get(p);
            if (!tokenToRedact.endsWith("[.!?\\-]")) {
                String asteriskString = "*".repeat(tokenToRedact.length());
                redactedTokens.set(p, asteriskString);
            }
        }
        return join(" ", redactedTokens);
    }

    //Reimplemented String.join method
    public static String join(CharSequence delimiter, Iterable<? extends CharSequence> redactedTokens) {
        Objects.requireNonNull(delimiter);
        Objects.requireNonNull(redactedTokens);
        StringJoiner joiner = new StringJoiner(delimiter);
        for (CharSequence cs : redactedTokens) {
            joiner.add(cs);
        }
        return joiner.toString();
    }

    public static void main(String[] args) {
        try {
            processText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


