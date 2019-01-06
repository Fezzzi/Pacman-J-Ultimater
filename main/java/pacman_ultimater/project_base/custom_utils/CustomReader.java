package pacman_ultimater.project_base.custom_utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CustomReader {
    private BufferedReader br;

    public CustomReader(String path) throws IOException{
        FileReader fr = new FileReader(path);
        this.br = new BufferedReader(fr);
    }

    public int read() throws IOException{
        return this.br.read();
    }

    public String readLine() throws IOException{
        return this.br.readLine();
    }

    public int peek() throws IOException{
        this.br.mark(1);
        int peekedChar = this.br.read();
        this.br.reset();
        return peekedChar;
    }
}
