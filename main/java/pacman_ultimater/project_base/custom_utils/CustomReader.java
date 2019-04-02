package pacman_ultimater.project_base.custom_utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Wrapper around bufferedReader implementing peek method.
 */
public class CustomReader
{
    private BufferedReader br;

    public CustomReader(String path) throws IOException{
        FileReader fr = new FileReader(path);
        this.br = new BufferedReader(fr);
    }

    public int read()
        throws IOException
    {
        return this.br.read();
    }

    public String readLine()
        throws IOException
    {
        return this.br.readLine();
    }

    /**
     * Method providing way to look ahead on the next character that is to be processed by read() method.
     *
     * @return Next charater waiting on the input.
     * @throws IOException To be handled by caller.
     */
    public int peek()
        throws IOException
    {
        this.br.mark(1);
        int peekedChar = this.br.read();
        this.br.reset();
        return peekedChar;
    }

    public void closeReader()
        throws IOException
    {
        br.close();
    }
}
