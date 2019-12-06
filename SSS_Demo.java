import java.awt.Point;
import java.awt.image.BufferedImage;

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
				int alpha = (rgb>>24)&0xff;
				int red   = ( rgb>>16)&0xff;
				int green  = ( rgb >>8)&0xff;
				int blue =  rgb & 0xff;
				// get avg 
				int av =(red+ green+blue)/3;
				if( av>250)
				{
					av=250;
				}
				// rebuild color
				rgb = (255<<24)|(avg<<16)|(avg<<8)|(avg);
				img.setRGB(wcnt, y, rgb);
			}
		}
	}
	static void GenerateShares( BufferedImage Secret,  int shareNum, BufferedImage share)
	{
		 
		/* 
			s(i,j) =I(i*k)
		
		*/
		// formula k=2
		int imght    =Secret.getHeight();
		int imgWidth = Secret.getWidth();		

		int maxval = Math.floor(imght/k);
		for( int j=0;j<maxval;j++)
		{
			for( int i=0;i<maxval;i++)
			{
				int s      = Secret.getRGB((i*k +1), j) & 0xff;// since it is a greyscale image just use blue
				int coeff  = sharenum * (Secret.getRGB((i*k +1), j) & 0xff);
				int newval = (s + coeff)%p;
				int rgb = (255<<24)|(newval<<16)|(newval<<8)|(newval);
				share.setRGB(i, j, rgb);
			}
		}
	}







	public static void main(String[] args) {
		// load an image
		BufferedImage inputImg =null;
		try
		{
			File inputFile =new FIle( args[0]);
			inputImg= ImageIO.read(inputfile);

		}catch(IOException ioe)
		{
			
			System.out.println(ioe);
			exit(1);
		}
		// make greyscale
		






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