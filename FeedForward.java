import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class FeedForward
{
    /**
     * Application to recognize the number drawn in a series
     * of images provided in a file stored in a directory called numbers
     * 
     */
	public static void main (String args[]) throws IOException
    {
        final String imageFile = args[0];                                 // variable to store filename of the image from command line
        final String hiddenWeightsFile = args[1];                         // variable to store hidden weights file from command line
        final String outputWeightsFile = args[2];                         // variable to store output weights file from command line
        
        
        double[][] hiddenWeights = fileReader(hiddenWeightsFile);         // 2d array to store the hidden weights
        double[][] outputWeights = fileReader(outputWeightsFile);         // 2d array to store the final weights
        
        BufferedImage img = ImageIO.read(new File(imageFile));            // create a BufferedImage object based on the file name
        double[] dummy = null;                                            // dummy array for getting pixel data
        double[] X = img.getData().getPixels(0, 0, img.getWidth(), img.getHeight(), dummy);     // getting the pixel data from the picture and storing it in an array X
        for(int i = 0; i < X.length; i++)                               // for every value in the pixel data array
        {
            X[i] = X[i]/256;                                            // set the pixel value to be within 0 and 1
        }
        double[] hiddenLayer = nextLayer(X, hiddenWeights);             // run nextLayer method to generate the next layer from the weights and adjusted pixel data
        double[] outputLayer = nextLayer(hiddenLayer, outputWeights);   // run the same method to go from the hidden layer to the output layer
        int result = indexOfLargest(outputLayer);                       // determine the index of the number it is most likely to be
        System.out.println(result + "\t9");
    }
    
    /**
     * Reads the provided file row by row and
     * outputs it into an ArrayList
     * 
     * @param inputFile  the file to read from
     * @return the 2d array of values
     */
    public static double[][] fileReader (String inputFile) throws IOException
    {
        ArrayList<String[]> fileText = new ArrayList();                     // arraylist to store the string arrays of the weights file
        BufferedReader br = new BufferedReader(new FileReader(inputFile));  // create a bufferedreader object from the input file
        String line;                                                        // a variable t ostore the line data
        while((line = br.readLine()) != null)                               // until there are no more rows
        {
            String[] lineArray = line.split(" ");                           // store a string array containing the values split by a space
            fileText.add(lineArray);                                        // add the array to the arraylist
        }
        double[][] fileNumArray = new double[fileText.size()][fileText.get(0).length];  // double array to store the double values
        for(int i = 0; i < fileText.size(); i++)                            // for every sub array in the array
        {
            for(int j = 0; j < fileText.get(0).length; j++)                 // for every value in the sub array
            {
                fileNumArray[i][j] = Double.parseDouble(fileText.get(i)[j]);    // convert it into a double and store it in the new array
            }
        }
        
        return fileNumArray;                                                // return the converted double array
    }
    
    /**
     * Finds the index of the largest
     * value in an array using the linear search
     * algorithm
     * 
     * @param array the array to search
     * @return the index of the largest attribute
     */
    public static int indexOfLargest (double[] array)
    {
        int greatest = 0;                                                   // variable to store the index of the greatest value
        for(int i = 1; i < array.length; i++)                               // for every value in the array
        {
            if(array[greatest] < array[i])                                  // if the value at the current index is greater the the value
            {                                                               // at the old index
                greatest = i;                                               // set the greatest index to the current one
            }
        }
        return greatest;                                                    // return the greatest index
    }
    
    /**
     * Converts from the current layer
     * to the next layer based on the weights and
     * current layer
     * 
     * @param currentLayer the current layer
     * @param weights the weights for the next values
     * @return the next layer
     */
    public static double[] nextLayer (double[] currentLayer, double[][] weights)
    {
        double[] outputLayer = new double[weights.length];                  // array to store the generated layer of values
        for(int i = 0; i < weights.length; i++)                             // for every set of weights
        {
            for(int j = 0; j < weights[0].length-1; j++)                    // for every weight
            {
                outputLayer[i] += currentLayer[j]*weights[i][j];            // add the product of the current pixel times the weight
            }                                                               // and add it to the current position in the layer
            outputLayer[i] += weights[i][weights[i].length-1];                 // add the bias after all of the products have been added
            outputLayer[i] = function(outputLayer[i]);                      // compute the result of the function using the number generated
        }
        return outputLayer;                                                 // return the new layer
    }
    
    /**
     * Computes the result of the given function
     * 
     * @param input the number to put through the function
     * @return the result of the function
     */
    public static double function (double input)
    {
        double output = 1.0/(1 + Math.pow(Math.E, -1*input));               // the function provided
        return output;                                                      // return the result
    }
}

