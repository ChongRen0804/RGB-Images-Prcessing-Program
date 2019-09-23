
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


public class ImageDisplay {

	JFrame frame;
	JLabel lbIm1;
	BufferedImage imgOne;
	int width = 1920;
	int height = 1080;
    double prev_mx=0;
    double prev_my=0;

	/** Read Image RGB
	 *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
	 */
	private void readImageRGB(int width, int height, String imgPath, BufferedImage img, BufferedImage img2)
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
                    img2.setRGB(x,y,pix);
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
    
    private void scaleImageRGB(double degree,BufferedImage img_origion,BufferedImage img_new)
    {
        for(int y = 0; y < (int)(this.height*degree); y++)
        {
            for(int x = 0; x < (int)(this.width*degree); x++)
            {
                int orig_x=(int)Math.floor(x/degree);
                int orig_y=(int)Math.floor(y/degree);
                int pix=img_origion.getRGB(orig_x,orig_y);
                img_new.setRGB(x,y,pix);
            }
        }
    }
    private void scaleImageRGB2(double degree,BufferedImage img_origion,BufferedImage img_new)
    {
        for(int y = 0; y < (int)(this.height*degree); y++)
        {
            for(int x = 0; x < (int)(this.width*degree); x++)
            {
                int orig_x=(int)Math.floor(x/degree);
                double u=Math.floor(x/degree * 100) / 100- orig_x;
                int orig_y=(int)Math.floor(y/degree);
                double v=Math.floor(y/degree * 100) / 100- orig_y;
                //double pix=(1-u)*(1-v)*img_origion.getRGB(orig_x,orig_y)+(1-u)*v*img_origion.getRGB(orig_x,orig_y+1)+(1-v)*u*img_origion.getRGB(orig_x+1,orig_y)+u*v*img_origion.getRGB(orig_x+1,orig_y+1);
                Color rgb1=new Color(img_origion.getRGB(orig_x,orig_y));
                
                double pix_red=(1-u)*(1-v)*rgb1.getRed();
                double pix_green=(1-u)*(1-v)*rgb1.getGreen();
                double pix_blue=(1-u)*(1-v)*rgb1.getBlue();
                if(y+1<1080)
                {
                    Color rgb2=new Color(img_origion.getRGB(orig_x,orig_y+1));
                    pix_red+=(1-u)*v*rgb2.getRed();
                    pix_green+=(1-u)*v*rgb2.getGreen();
                    pix_blue+=(1-u)*v*rgb2.getBlue();
                }
                else
                {
                    pix_red+=(1-u)*v*rgb1.getRed();
                    pix_green+=(1-u)*v*rgb1.getGreen();
                    pix_blue+=(1-u)*v*rgb1.getBlue();
                }
                if(x+1<1920)
                {
                    Color rgb3=new Color(img_origion.getRGB(orig_x+1,orig_y));
                    pix_red+=(1-v)*u*rgb3.getRed();
                    pix_green+=(1-v)*u*rgb3.getGreen();
                    pix_blue+=(1-v)*u*rgb3.getBlue();
                }
                else
                {
                    pix_red+=(1-v)*u*rgb1.getRed();
                    pix_green+=(1-v)*u*rgb1.getGreen();
                    pix_blue+=(1-v)*u*rgb1.getBlue();
                }
                if(y+1<1080 &&x+1<1920)
                {
                    Color rgb4=new Color(img_origion.getRGB(orig_x+1,orig_y+1));
                    pix_red+=u*v*rgb4.getRed();
                    pix_green+=u*v*rgb4.getGreen();
                    pix_blue+=u*v*rgb4.getBlue();
                }
                else
                {
                    pix_red+=u*v*rgb1.getRed();
                    pix_green+=u*v*rgb1.getGreen();
                    pix_blue+=u*v*rgb1.getBlue();
                }
                double pix = 0xff000000 | (((int)pix_red & 0xff) << 16) | (((int)pix_green & 0xff) << 8) | ((int)pix_blue & 0xff);
                img_new.setRGB(x,y,(int)pix);
            }
        }
    }
    
    

	public void showIms(String[] args){

		// Read a parameter from command line
		String param1 = args[3];
        double degree= Double.parseDouble(args[2]);
		//System.out.println("The second parameter was: " + param1);
        
		// Read in the specified image
		imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgOne_backup;
        imgOne_backup=new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage imgTwo;
        imgTwo = new BufferedImage((int)(width*degree), (int)(height*degree),BufferedImage.TYPE_INT_RGB);
		readImageRGB(width, height, args[0], imgOne,imgOne_backup);
        if(Integer.parseInt(args[1])==1)
        {
            if(Integer.parseInt(args[3])==0)
            {
                scaleImageRGB(degree,imgOne,imgTwo);
                //System.out.println("00000000000000000");
            }
            else
            {
                scaleImageRGB2(degree,imgOne,imgTwo);
                //System.out.println("11111111111111111");
            }

          
        }

    
		// Use label to display the image
		frame = new JFrame();
		GridBagLayout gLayout = new GridBagLayout();
		frame.getContentPane().setLayout(gLayout);
        if(Integer.parseInt(args[1])==1)
            lbIm1 = new JLabel(new ImageIcon(imgTwo));
        else
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
        if(Integer.parseInt(args[1])==2)
        {
            BufferedImage imgThree;
            imgThree = new BufferedImage((int)(width*degree), (int)(height*degree),BufferedImage.TYPE_INT_RGB);
            if(Integer.parseInt(args[3])==0)
            {
                scaleImageRGB(degree,imgOne,imgThree);
                //System.out.println("00000000000000000");
            }
            else
            {
                scaleImageRGB2(degree,imgOne,imgThree);
                //System.out.println("11111111111111111");
            }
            while(true)
            {
                PointerInfo pinfo = MouseInfo.getPointerInfo();
                Point p = pinfo.getLocation();
                double mx = p.getX();
                double my = p.getY();
                if(mx==prev_mx &&my==prev_my)
                    continue;
                prev_mx=mx;
                prev_my=my;
                //System.out.println("x="+mx+"y="+my);
                for(int y = 0; y < 1080; y++)
                {
                    for(int x =0; x < 1920; x++)
                    {
                        int rgb=imgOne_backup.getRGB(x,y);
                        Color color = new Color(rgb);
                        int changeValue=-100;
                        int r = color.getRed() + changeValue;
                        int g = color.getGreen() + changeValue;
                        int b = color.getBlue() + changeValue;
                        if (r > 255)
                        {
                            r = 255;
                        } else if (r < 0)
                        {
                            r = 0;
                        }
                        
                        if (g > 255)
                        {
                            g = 255;
                        } else if (g < 0)
                        {
                            g = 0;
                        }
                        
                        if (b > 255)
                        {
                            b = 255;
                        } else if (b < 0)
                        {
                            b = 0;
                        }
                        color = new Color(r, g, b);
                        imgOne.setRGB(x,y,color.getRGB());
                        
                    }
                }
                for(int y = -100; y < 100; y++)
                {
                    for(int x =20; x < 220; x++)
                    {
                        int newx=(int)mx+x;
                        int newy=(int)my+y;
                        int centerx=(int)mx+120;
                        int centery=(int)my;
                        int newcenterx=(int)(centerx*degree);
                        int newcentery=(int)(centery*degree);
                        
                        if(0<newx && newx<1920 && 0<newy && newy<1080)
                        {
                            if(Math.pow((newx-centerx),2)+Math.pow((newy-centery),2)<10000)
                            {
                                int pix=imgThree.getRGB(newx-centerx+newcenterx,newy-centery+newcentery);
                                imgOne.setRGB(newx,newy,pix);
                            }
                            
                        }
                        
                    }
                }
           
                frame.repaint(1);
            }
        }
	}

	public static void main(String[] args) {
		ImageDisplay ren = new ImageDisplay();
		ren.showIms(args);
	}

}
