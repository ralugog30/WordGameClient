import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class WordGameClient {
    public static void main(String[] args) {
        try {


            Socket socket = new Socket("127.0.0.1", 7777); // Connect to the server
            System.out.println("Connected to the server...");

            BufferedReader clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter clientOut = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

            System.out.print("Enter the first word: ");
            String firstWord = userInput.readLine();
            clientOut.println(firstWord);

            boolean gameover = false;

            while (!gameover) {
                String data = clientIn.readLine();
                System.out.println("Received from server: " + data);

                if (data.startsWith("Game Over")) {
                    System.out.println("Client won!");
                    gameover = true;
                    break;
                }

                String[] invalidEndings = {"nt", "ee", "ct", "rb", "rt", "ns", "rd", "lt", "ft", "ps", "ng", "im", "nd", "nz", "lm", "nj"};
                boolean verify = true;

                for (String i : invalidEndings) {
                    if (data.endsWith(i)) {
                        verify = false;
                        break;
                    }
                }

                if (!verify) {
                    System.out.println("Server won!");
                    clientOut.println("Game Over");
                    gameover = true;
                    break;
                } else {
                    System.out.print("Enter a word that starts with the last two letters: ");
                    String userWord = userInput.readLine();
                    if (!userWord.substring(0, 2).equals(data.substring(data.length() - 2))) {

                        System.out.println("Change the word");
                        System.out.print("Write a word that ends with the last two letters of the given word: ");
                        userWord = userInput.readLine();
                    }
                    clientOut.println(userWord);
                }
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
