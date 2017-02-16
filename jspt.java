import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
public class jspt {
	public static String[] getURLs(String url) throws IOException{
		String[] urls=null;
		String absHref;
		int i=0;
		int count=0;
		int size;
		try{
			Jsoup.connect(url).followRedirects(true);
			Document doc=Jsoup.connect(url).get();

			Element link =doc.select("a").get(0);
			size=doc.select("a").size();
			urls=new String[size];

			while(i<size){
				link=doc.select("a").get(i) ;
				absHref=link.attr("abs:href");
				if(absHref.indexOf("www.cs.purdue.edu")!=-1 && absHref.indexOf("mailto:")==-1 && !absHref.contains("#")){
					urls[count]=absHref;
					count++;

				}
				i++;
			}
		}catch(IndexOutOfBoundsException e){
				System.out.println("No <a> tag was found.Url: "+url);

			}catch(SocketTimeoutException e2){
				System.out.println("Socket Timeout. Url: "+url);
			}
			return urls;
		}
	public static void main(String[] args) {
         jspt j = new jspt();
		 int i = 0;
		 try {
		 String[] s = jspt.getURLs("http://www.cs.purdue.edu");
		 for(String x : s) {
			System.out.println(x);
				if(x!=null)
				i++;
		 }
		 System.out.println(i);
		 }catch(IOException ie) {
			 ie.printStackTrace();
		 }
	}
 }





