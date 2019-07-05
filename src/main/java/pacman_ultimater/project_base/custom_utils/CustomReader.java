package pacman_ultimater.project_base.custom_utils;

import java.io.*;

/**
 * Wrapper around bufferedReader implementing peek method.
 */
public class CustomReader
{
    private final BufferedReader br;

    public CustomReader(InputStream mapStream)
    {
        InputStreamReader isr = new InputStreamReader(mapStream);
        this.br = new BufferedReader(isr);
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
