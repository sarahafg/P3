import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.Collections;
import java.util.AbstractCollection;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;

import javax.swing.JPanel;

/**
 * Decomposer class.
 * Implements Decomposer.
 * @author Sarah Fakhry
 */
public class Decomposor extends JPanel
{
  /**
   * Geting a set of neighboring regions.
   * @param ds
   * @param root
   * @return list of adjacent regions
   */
  private TreeSet<Integer> getNeightborSets(DisjointSets<Pixel> ds, int root)
  {
    for (Pixel roots : ds.get(root)) {
      getNeightbors(roots);
      for (Pixel roots2 : getNeightbors(roots)) {
        if (getNeightborSets(ds, root) != null) {
          ds.find(root);
        }
      }
    }
    return null; 
  }
  
  /**
   * Computing the similarity between the two regions given.
   * @param ds
   * @param R1
   * @param R2
   * @return the sum
   */
  private Similarity getSimilarity(DisjointSets<Pixel> ds, int R1, int R2)
  {
    int x = ds.find(R1);       
    int y = ds.find(R2); 
    // Computing average color of both regions
    Color avg1 = computeAverageColor(ds.get(x));
    Color avg2 = computeAverageColor(ds.get(y));
    for (Pixel root1 : ds.get(R1)) {
      for (Pixel root2 : ds.get(R2)) {
        // Computing sum of color differences
        int sum = getDifference(getColor(root1), getColor(root2));
        return new Similarity(sum, getPixel(x), getPixel(y));
      }
    }
    return null;
  }

  /**
   * Iteratively merging two adjacent regions with most similar colors 
   * until the number of regions is K.
   * @param K
   * @return merged adjacent regions
   */
  public void segment(int K) //K is the number of desired segments
  {
    if (K < 2)
    {
      throw new IllegalArgumentException(new String("! Error: K should be greater than 1, current K="+K));
    }
    int width = this.image.getWidth();
    int height = this.image.getHeight();

    ArrayList<Pixel> temp = new ArrayList<>();
    DisjointSets<Pixel> temp1 = new DisjointSets<>(temp);
    PriorityQueue<Similarity> queue = new PriorityQueue<>();
    // Adding new pixel to arraylist
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        temp.add(new Pixel(i, j));
      }
    }
    // Adding similaritites to priority queue
    for (int index = 0; index < temp.size(); index++) {
      TreeSet<Integer> neighbors = getNeightborSets(temp1, getID(temp.get(index)));
      for (int index1 = 0; index1 < neighbors.size(); index1++) {
        Similarity sim = getSimilarity(temp1, neighbors.first(), neighbors.last());
        queue.add(sim);
      }
    }
    while (ds.getNumSets() > K) {
      int p1 = ds.find(width);
      int p2 = ds.find(height);
      // Checking if regions not are identical
      if (p1 != p2) {
        Similarity sim = getSimilarity(ds, p1, p2);
        // Adding roots back into queue
        queue.add(sim);
      }
      // Checking if regions are identical
      if (p1 == p2) {
        // Union of regions
        ds.union(p1, p2);
      }
    }
    //Hint: the algorithm is not fast and you are processing many pixels
    //      (e.g., 10,000 pixel for a small 100 by 100 image)
    //      output a "." every 100 unions so you get some progress updates.
  }

    //-----------------------------------------------------------------------
    //
    //
    // Todo: Read and provide comments, but do not change the following code
    //
    //
    //-----------------------------------------------------------------------

    //
    //Data
    //
    public BufferedImage image;       //this is the 2D array of RGB pixels
    private String img_filename;      //input image filename without .jpg or .png
    private DisjointSets<Pixel> ds;   //the disjoint set

    //
    // constructor, read image from file
    //
    public Decomposor(String imgfile)
    {
      File imageFile = new File(imgfile);
      try
      {
        this.image = ImageIO.read(imageFile);
      }
      catch(IOException e)
      {
        System.err.println("! Error: Failed to read "+imgfile+", error msg: "+e);
        return;
      }
      this.img_filename=imgfile.substring(0, imgfile.lastIndexOf('.')); //remember the filename
    }


    //
    // 3 private classes below
    //
    private class Pair<T>
    {
      public Pair(T p_, T q_){this.p=p_;this.q=q_;}
      T p, q;
    }

    //a pixel is a 2D coordinate (w,h) in an image
    private class Pixel extends Pair<Integer>{ public Pixel(int w, int h){ super(w,h); } } //aliasing Pixel

    //this class represents the similarity between the colors of two adjacent pixels or regions
    private class Similarity implements Comparable<Similarity>
    {
      public Similarity(int d, Pixel p, Pixel q)
      {
        this.distance=d;
        this.pixels=new Pair<Pixel>(p,q);
      }

      public int compareTo( Similarity other )
      {
        //remove ambiguity~ update: 11/28/2017
        int diff=this.distance - other.distance;
        if(diff!=0) return diff;
        diff=getID(this.pixels.p) - getID(other.pixels.p);
        if(diff!=0) return diff;
        return getID(this.pixels.q) - getID(other.pixels.q);
      }

      //a pair of ajacent pixels or regions (represented by the "root" pixels)
      public Pair<Pixel> pixels;

      //distance between the color of two pixels or two regions,
      //smaller distance indicates higher similarity
      public int distance;
    }

    //
    // helper functions
    //

    //convert a pixel to an ID
    private int getID(Pixel pixel)
    {
      return this.image.getWidth()*pixel.q+pixel.p;
    }

    //convert ID back to pixel
    private Pixel getPixel(int id)
    {
      int h= id/this.image.getWidth();
      int w= id-this.image.getWidth()*h;

      if(h<0 || h>=this.image.getHeight() || w<0 || w>=this.image.getWidth())
        throw new ArrayIndexOutOfBoundsException();

      return new Pixel(w,h);
    }

  	private Color getColor(Pixel p) {
  		return new Color(image.getRGB(p.p, p.q));
  	}

    //compute the average color pf a collection of pixels
    private Color computeAverageColor(AbstractCollection<Pixel> pixels)
    {
      int r=0, g=0, b=0;
      for(Pixel p : pixels)
      {
        Color c = new Color(image.getRGB(p.p, p.q));
        r+=c.getRed();
        g+=c.getGreen();
        b+=c.getBlue();
      }
      return new Color(r/pixels.size(),g/pixels.size(),b/pixels.size());
    }

    private int getDifference(Color c1, Color c2)
    {
      int r = (int)(c1.getRed()-c2.getRed());
      int g = (int)(c1.getGreen()-c2.getGreen());
      int b = (int)(c1.getBlue()-c2.getBlue());

      return r*r+g*g+b*b;
    }

    //8-neighbors of a given pixel
    private ArrayList<Pixel> getNeightbors(Pixel pixel)
    {
      ArrayList<Pixel> neighbors = new ArrayList<Pixel>();

      for(int i=-1;i<=1;i++)
      {
        int n_w=pixel.p+i;
        if(n_w<0 || n_w==this.image.getWidth()) continue;
        for(int j=-1;j<=1;j++)
        {
          int n_h=pixel.q+j;
          if(n_h<0 || n_h==this.image.getHeight()) continue;
          if(i==0 && j==0) continue;
          neighbors.add( new Pixel(n_w, n_h) );
        }//end for j
      }//end for i

      return neighbors;
    }

    //Output results
    public void outputResults(int K)
    {
        //collect all sets from union-find datastructure
        int region_counter=1;
        ArrayList<Pair<Integer>> sorted_regions = new ArrayList<Pair<Integer>>();

        int width = this.image.getWidth();
        int height = this.image.getHeight();
        for(int h=0; h<height; h++){
          for(int w=0; w<width; w++){
              int id=getID(new Pixel(w,h));
              int setid=ds.find(id);
              if(id!=setid) continue;
              sorted_regions.add(new Pair<Integer>(ds.get(setid).size(),setid));
          }//end for w
        }//end for h

        //sort the regions
        Collections.sort(sorted_regions, new Comparator<Pair<Integer>>(){
          @Override
          public int compare(Pair<Integer> a, Pair<Integer> b) {
              if(a.p!=b.p) return b.p-a.p;
              else return b.q-a.q;
          }
        });

        //recolor and output region info
        for( Pair<Integer> R : sorted_regions )
        {
          int setid=R.q;
          Set<Pixel> set_pixels = ds.get(setid);

          //compute the average color
          Color avg_color=computeAverageColor(set_pixels);

          System.out.println("region "+(region_counter++)+" size= "+set_pixels.size()+" color="+avg_color);//+" setid="+setid);
          for(Pixel set_pixel : set_pixels)
          {
            image.setRGB(set_pixel.p,set_pixel.q, avg_color.getRGB());
          }//end for exach pixel in the set
        }//end for R

        //save output image (this.image) to a png file
        String out_filename = img_filename+"_seg_"+K+".png";
        try
        {
          File ouptut = new File(out_filename);
          ImageIO.write(this.image, "png", ouptut);
          System.err.println("- Saved result to "+out_filename);
        }
        catch (Exception e) {
          System.err.println("! Error: Failed to save image to "+out_filename);
        }
    }

    //
    // JPanel function
    //
    public void paint(Graphics g)
    {
      g.drawImage(this.image, 0, 0,this);
    }
}

//bd4f0c8da4c44278947c5d1a79d71d1f
