import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
public class SSS_Demo {
	static final int k=2;
	static final int p = 251;
	public static void GreyScaleImgMx250(BufferedImage img)
	{
		int imght    =img.getHeight();
		int imgWidth = img.getWidth();

		int hcnt =0;
		int wcnt =0;
		for (;hcnt<imght; ++hcnt )
		{
			for ( ;wcnt<imgWidth;++wcnt)
			{
				int rgb = img.getRGB(wcnt, hcnt);
				//break into componets using bit manipulations similar to stegnaography hmwk
				//int alpha = (rgb>>24)&0xff; not needed will be 255 for greyscale
				int red   = ( rgb>>16)&0xff;
				int green  = ( rgb >>8)&0xff;
				int blue =  rgb & 0xff;
				// get avg 
				int average =(red+ green+blue)/3;
				if( average>250)
				{
					average=250;
				}
				// rebuild color
				rgb = (255<<24)|(average<<16)|(average<<8)|(average);
				img.setRGB(wcnt, hcnt, rgb);
			}
		}
	}
	static void GenerateShares( BufferedImage Secret,  int shareNum, BufferedImage share)
	{
		 
		/* 
			s(i,j) =I(i*k)
		
		*/
		// formula k=2
		// were using squre images
		int imght    =Secret.getHeight();
		//int imgWidth = Secret.getWidth();		

		int maxval = (int)Math.floor(imght/k);
		for( int j=0;j<maxval;j++)
		{
			for( int i=0;i<maxval;i++)
			{
				int s      = Secret.getRGB((i*k +1), j) & 0xff;// since it is a greyscale image just use blue
				int coeff  = shareNum * (Secret.getRGB((i*k +1), j) & 0xff);
				int newval = (s + coeff)%p;
				int rgb = (255<<24)|(newval<<16)|(newval<<8)|(newval);
				share.setRGB(i, j, rgb);
			}
		}
	}

	void RebuildImage( BufferedImage [] shares,  BufferedImage reconstructed)
	{
		int hghtWdth = shares[0].getWidth();
		for( int h=0; h<hghtWdth;++h)
		{
			for ( int w=0;w<hghtWdth;++w)
			{
				SSS s = new SSS(2,2,p);
				Point[] parr = new Point[2];
				for(int cntSh =0;cntSh<shares.length;++cntSh)
				{
					int im1val = shares[cntSh].getRGB(w, h);
					parr[cntSh] =new Point(cntSh+1,     im1val);					
				}
				int recval =s.reconstruct(parr, p);
				//call function with point Array
				reconstructed.setRGB(w, h, recval);
			}
		}
	}

	static void   SubtractImagesGS(BufferedImage img1, BufferedImage img2)
	{
		BufferedImage newimg = new BufferedImage( img1.getWidth(),img2.getHeight(),BufferedImage.TYPE_INT_RGB );
		for(int hcnt = 0; hcnt < img1.getHeight(); ++hcnt)
		{
			for(int wcnt =0; wcnt < img1.getHeight(); ++ wcnt)
			{
				int px1 = img1.getRGB(wcnt,hcnt);
				int px2 =img1.getRGB(wcnt,hcnt);
				int diff = getPixelDifferenceGS(px1,px2);

				newimg.setRGB(wcnt,hcnt,diff);



			}
		}

		ImageIO.write(newimg,"bmp", new File(""));





	}
	/***
	 *  gets the greyscale pixel difference 
	 * @param px1 main pixel
	 * @param px2 pixel to subtract
	 * @return piexel value
	 */
	public static int getPixelDifferenceGS( int px1, int px2)
	{
		int p1Blue = px1  & 0xFF;
		int p2Blue = px2  & 0xFF;
		int diff1chan = Math.abs(p1Blue-p2Blue);
		int diff = (255 << 24) | (diff1chan << 16) | (diff1chan << 8) | diff1chan;
		return diff;
	}


	public static void main(String[] args) {
		// load an image

		BufferedImage inputImg =null;
		try
		{
			File inputFile =new File( args[0]);
			inputImg= ImageIO.read(inputFile);

		}catch(IOException ioe)
		{
			
			System.out.println(ioe);
			System.exit(1);
		}
		// make greyscale
		GreyScaleImgMx250(inputImg);
		int maxval = (int)Math.floor(inputImg.getWidth()/k);

		BufferedImage S1 = new BufferedImage(maxval,maxval, BufferedImage.TYPE_INT_RGB);
		BufferedImage S2 = new BufferedImage(maxval,maxval, BufferedImage.TYPE_INT_RGB);




		int k = 2;
		
		int n = 5;
		
		int prime = 251;
		
		int secret = 11;
		
		SSS secretSharing = new SSS(k, n, prime);
		
		
		// shares generated using (k,n) SSSS
		int [] shares = secretSharing.shareGen(secret);
		
		
		// shares converted to (x,y) form
		Point[] shareCoords = secretSharing.intToPoints(shares);
		
		// Picking k shares out of n to be used for reconstruction
		Point[] reconstShares = new Point[k];
		
		for(int i=0; i<k; i++) {
			
			reconstShares[i] = shareCoords[i];
			
		}
		
		// reconstructed value
		int reconstructedShare = secretSharing.reconstruct(reconstShares, 251);
		
		System.out.println(reconstructedShare);
		
	}
	
	
}