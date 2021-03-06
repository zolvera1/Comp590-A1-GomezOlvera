package Huffman;
import io.InputStreamBitSource;
import io.InsufficientBitsLeftException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class HuffDecode {

    public static void main(String[] args) throws IOException, InsufficientBitsLeftException {

            String input = "data/compressed.dat";
            String output = "data/uncompress.txt";
            FileInputStream fis = new FileInputStream(input);
            InputStreamBitSource _bitsource =  new InputStreamBitSource(fis);
            System.out.println("Decoding file " + input);

            List<SymbolWithCodeLength> symbols_with_length = new ArrayList<SymbolWithCodeLength>();
            int numSymbols;
            for(int i = 0; i < 256; i++) {
                //decode next symbol and write out to file
                int length = _bitsource.next(8);
               symbols_with_length.add(new SymbolWithCodeLength(i,length));
            }
            //sort!
            symbols_with_length.sort(null);
            //time to use the tree.
            CodeTree tree = new CodeTree(symbols_with_length);
            numSymbols = _bitsource.next(32);
            int[] symCounts = new int[256];


            try{
                FileOutputStream fos = new FileOutputStream(output);
            for(int i = 0; i != numSymbols; i++) {
                //tp help with entropy
                int sym = tree.decode(_bitsource);
                symCounts[sym]++;
                fos.write(sym);
            }
            
            fos.flush();
            fos.close();
            fis.close();
            System.out.println("Finished!");
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(InsufficientBitsLeftException e) {
            e.printStackTrace();
        }
    }
}
