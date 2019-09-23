
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class ImageAnalysis {

	JFrame frame;
	JLabel lbIm1;
	BufferedImage imgOne;
	int width = 1920;
	int height = 1080;
    Set<Integer> points = new HashSet<>();
	/** Read Image RGB
	 *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
	 */
	private void readImageRGB(int width, int height, String imgPath, BufferedImage img)
	{
		try
		{
			int frameLength = width*height*3;

			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);

			long len = frameLength;
			byte[] bytes = new byte[(int) len];

			raf.read(bytes);

			int ind = 0;
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					ind++;
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
    private void randome_remove(BufferedImage img,double percent)
    {
        Random r = new Random();
        points.clear();
        for(int i=0 ; i<1920*1080*percent ;  i++)
            {
                int ran = r.nextInt(1920*1080);
                while(points.contains(ran))
                {
                    ran = r.nextInt(1920*1080);
                }
                points.add(ran);
                int rany = ran/1920;
                int ranx = ran%1920;
                img.setRGB(ranx,rany,0xff000000);
            }
    }
    private int cal_difference(BufferedImage img,BufferedImage img_backup)
    {
        int difference=0;
        int count=0;
        int rgb_red=0;
        int rgb_green=0;
        int rgb_blue=0;
        for(int y = 0; y < height; y++)
        {
            for(int x = 0; x < width; x++)
            {
                if(points.contains(y*1920+x))
                {
                    count=0;
                    
                    for(int xx=-1;xx<2;xx++)
                    {
                        for(int yy=-1;yy<2;yy++)
                        {
                            if(xx==0&&yy==0)
                                continue;
                            int newx=xx+x;
                            int newy=xx+y;
                            if(newx>0 &&newx<1920&&newy>0&&newy<1080&&(!points.contains(y*1920+x)))
                            {
                                count++;
                                Color rgb1=new Color(img.getRGB(newx,newy));
                                rgb_red+=rgb1.getRed();
                                rgb_green+=rgb1.getGreen();
                                rgb_blue+=rgb1.getBlue();
                            }
                        }
                    }
                    Color rgb2=new Color(img_backup.getRGB(x,y));
                    if(count!=0)
                        difference+=(Math.abs(rgb_red/count-rgb2.getRed())+Math.abs(rgb_green/count-rgb2.getGreen())+Math.abs(rgb_blue/count-rgb2.getBlue()));
                    else
                        difference+=(rgb2.getRed()+rgb2.getGreen()+rgb2.getBlue());
                }
            }
        }
        return difference;
    
    }

	public void showIms(String[] args){

		// Read a parameter from command line
		String param1 = args[1];
		System.out.println("The second parameter was: " + param1);

		// Read in the specified image
		imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		readImageRGB(width, height, args[0], imgOne);
        BufferedImage imgOne_backup;
        imgOne_backup = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        readImageRGB(width, height, args[0], imgOne_backup);
        int difference=0;
        double percent=0;
        FileWriter fw = null;
        try
        {
            fw = new FileWriter("test.txt");
            for(int i=0;i<51;i++)
            {
                percent=i*0.01;
                BufferedImage img;
                img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                randome_remove(img,percent);
                difference=cal_difference(img,imgOne_backup);
                System.out.println(i);
                fw.write(difference+",");
     
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(fw!=null)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        

        
		// Use label to display the image
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);

		lbIm1 = new JLabel(new ImageIcon(imgOne));

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		frame.getContentPane().add(lbIm1, c);

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		ImageAnalysis ren = new ImageAnalysis();
		ren.showIms(args);
	}

}
