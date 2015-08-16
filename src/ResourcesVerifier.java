import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


public class ResourcesVerifier {
	public ResourcesVerifier()	throws Exception	{
        File fIntro = new File("intro.wmv");
        InputStream fBackground = this.getClass().getResourceAsStream("Af.class");

        verifyStream( new FileInputStream(fIntro), 2456590662097688589L);
        verifyStream( fBackground, -5931057676022197920L);
	}
	private void verifyStream(InputStream is, long key)	throws Exception	{
		long result = 0;
		long P = 1000000000 + 7;
		long mul = 1;
		int n = 0;
		while	( (n = is.read()) != -1 )	{
			result += n * mul;
			mul *= P;
		}
	//	System.out.println(result);
		if	( result != key )
			throw new Exception(
				new String(
					new char [] {76, 111, 115, 32, 97, 114, 99, 104, 105, 118, 111, 115, 32, 111, 114, 105, 103, 105, 110, 97, 108, 101, 115, 32, 110, 111, 32, 100, 101, 98, 101, 110, 32, 115, 101, 114, 32, 109, 111, 118, 105, 100, 111, 115}
				)
				//	"Los archivos originales no deben ser movidos"
			);
	}
}
