package org.example;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is a parser for STL files. Currently, normals specified in the
 * file are ignored and recalculated under the assumption that the coordinates
 * are provided in right-handed coordinate space (counter-clockwise).
 * @author CCHall
 * @author Andrew Goh
 *
 *  * -reversion: mar 2017 Andrew
 * updated logic to handle binary STL files with "solid" in the header
 */
public class STLParser {
    /**
     * Parses an STL file, attempting to automatically detect whether the file
     * is an ASCII or binary STL file
     * @param filepath The file to parse
     * @return A list of triangles representing all the triangles in the STL
     * file.
     * @throws IOException Thrown if there was a problem reading the file
     * (typically means the file does not exist or is not a file).
     * @throws IllegalArgumentException Thrown if the STL is not properly
     * formatted
     */
    public static List<Triangle> parseSTLFile(Path filepath) throws IOException{
        byte[] allBytes = Files.readAllBytes(filepath);
        // determine if it is ASCII or binary STL

        //some binary STL files has "solid" in the first 80 chars
        //this breaks logic that determines if a file is ascii based on it
        //simply beginning with "solid"
        boolean isASCIISTL = false;

        //read the first 512 chars or fewer
        String buf = readblock(allBytes, 0, 512);
        StringBuffer sb = new StringBuffer();
        int inl = readline(buf, sb, 0);
        String line = sb.toString();
        StringTokenizer st = new StringTokenizer(line);
        String token = st.nextToken();
        if(token.equals("solid")) { //start with "solid"
            if(inl>-1) { //found new line for next line
                sb = new StringBuffer();
                inl = readline(buf, sb, inl+1); //read next line
                line = sb.toString();
                st = new StringTokenizer(line);
                token = st.nextToken();
                if(token.equals("endsolid"))
                    isASCIISTL = true; //empty ascii file
                else if(token.equals("facet")) {
                    isASCIISTL = true; //ascii file
                } else if (isbinaryfile(allBytes))
                    isASCIISTL = false; //binary file
            } else { //no linefeed
                if (isbinaryfile(allBytes))
                    isASCIISTL = false; //binary file
            }
        } else {//does not starts with "solid"
            if (isbinaryfile(allBytes))
                isASCIISTL = false; //binary file
        }

        // read file to array of triangles
        List<Triangle> mesh;
        if(isASCIISTL){
            Charset charset = Charset.forName("UTF-8");
            mesh = readASCII(charset.decode(ByteBuffer.wrap(allBytes)).toString().toLowerCase());
        } else {
            mesh = readBinary(allBytes);
        }
        return mesh;
    }


    public static String readblock(byte[] allBytes, int offset, int length) {
        if(allBytes.length-offset<length) length = allBytes.length-offset;
        Charset charset = Charset.forName("UTF-8");
        CharBuffer decode = charset.decode(ByteBuffer.wrap(allBytes, offset, length));
        return decode.toString().toLowerCase();
    }

    public static int readline(String buf, StringBuffer sb, int offset) {
        int il = buf.indexOf('\n', offset);
        if(il>-1)
            sb.append(buf.substring(offset, il-1));
        else
            sb.append(buf.substring(offset));
        return il;
    }

    public static boolean isbinaryfile(byte[] allBytes) throws IllegalArgumentException {
        if (allBytes.length<84)
            throw new IllegalArgumentException("invalid binary file, length<84");
        int numtriangles = byteatoint(Arrays.copyOfRange(allBytes, 80, 84));
        if (allBytes.length >= 84 + numtriangles * 50)
            return true; //is binary file
        else {
            String msg = "invalid binary file, num triangles does not match length specs";
            throw new IllegalArgumentException(msg);
        }
    }

    // little endian
    public static int byteatoint(byte[] bytes) {
        assert (bytes.length == 4);
        int r = 0;
        r = bytes[0] & 0xff;
        r |= (bytes[1] & 0xff) << 8;
        r |= (bytes[2] & 0xff) << 16;
        r |= (bytes[3] & 0xff) << 24 ;
        return r;
    }



    /**
     * Reads an STL ASCII file content provided as a String
     * @param content ASCII STL
     * @return A list of triangles representing all of the triangles in the STL
     * file.
     * @throws IllegalArgumentException Thrown if the STL is not properly
     * formatted
     */
    public static List<Triangle> readASCII(String content) {
        Logger.getLogger(STLParser.class.getName()).log(Level.FINEST,"Parsing ASCII STL format");
        // string is lowercase
        ArrayList<Triangle> triangles = new ArrayList<>();

        int position = 0;
        scan:
        {
            while (position < content.length() & position >= 0) {
                position = content.indexOf("facet", position);
                if (position < 0) {
                    break scan;
                }
                try {
                    Vector3[] vertices = new Vector3[3];
                    for (int v = 0; v < vertices.length; v++) {
                        position = content.indexOf("vertex", position) + "vertex".length();
                        while (Character.isWhitespace(content.charAt(position))) {
                            position++;
                        }
                        int nextSpace;
                        double[] vals = new double[3];
                        for (int d = 0; d < vals.length; d++) {
                            nextSpace = position + 1;
                            while (!Character.isWhitespace(content.charAt(nextSpace))) {
                                nextSpace++;
                            }
                            String value = content.substring(position, nextSpace);
                            vals[d] = Double.parseDouble(value);
                            position = nextSpace;
                            while (Character.isWhitespace(content.charAt(position))) {
                                position++;
                            }
                        }
                        vertices[v] = new Vector3(vals[0], vals[1], vals[2]);
                    }
                    position = content.indexOf("endfacet", position)+"endfacet".length();
                    triangles.add(new Triangle(vertices[0], vertices[1], vertices[2]));
                } catch (Exception ex) {
                    int back = position - 128;
                    if (back < 0) {
                        back = 0;
                    }
                    int forward = position + 128;
                    if (position > content.length()) {
                        forward = content.length();
                    }
                    throw new IllegalArgumentException("Malformed STL syntax near \"" + content.substring(back, forward) + "\"", ex);
                }
            }
        }

        return triangles;
    }


    /**
     * Parses binary STL file content provided as a byte array
     * @param allBytes binary STL
     * @return A list of triangles representing all of the triangles in the STL
     * file.
     * @throws IllegalArgumentException Thrown if the STL is not properly
     * formatted
     */
    public static List<Triangle> readBinary(byte[] allBytes) {
        Logger.getLogger(STLParser.class.getName()).log(Level.FINEST,"Parsing binary STL format");
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(allBytes));
        ArrayList<Triangle> triangles = new ArrayList<>();
        try{
            // skip the header
            byte[] header = new byte[80];
            in.read(header);
            // get number triangles (not really needed)
            // WARNING: STL FILES ARE SMALL-ENDIAN
            int numberTriangles = Integer.reverseBytes(in.readInt());
            triangles.ensureCapacity(numberTriangles);
            // read triangles
            try{
                while(in.available() > 0 ){
                    float[] nvec = new float[3];
                    for(int i = 0; i < nvec.length; i++){
                        nvec[i] = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                    }
                    Vector3 normal = new Vector3(nvec[0],nvec[1],nvec[2]); // not used (yet)
                    Vector3[] vertices = new Vector3[3];
                    for (int v = 0; v < vertices.length; v++) {
                        float[] vals = new float[3];
                        for (int d = 0; d < vals.length; d++) {
                            vals[d] = Float.intBitsToFloat(Integer.reverseBytes(in.readInt()));
                        }
                        vertices[v] = new Vector3(vals[0], vals[1], vals[2]);
                    }
                    short attribute = Short.reverseBytes(in.readShort()); // not used (yet)
                    triangles.add(new Triangle( vertices[0], vertices[1], vertices[2]));
                }
            }catch(Exception ex){
                throw new IllegalArgumentException("Malformed STL binary at triangle number " + (triangles.size()+1), ex);
            }
        }catch(IOException ex){
            // IO exceptions are impossible with byte array input streams,
            // but still need to be caught
            Logger.getLogger(STLParser.class.getName()).log(Level.SEVERE, "HOLY SHIT! A ByteArrayInputStream threw an exception!", ex);
        }
        return triangles;
    }

}