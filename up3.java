import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
class CC implements Runnable {
	String nextUrl;
	up3 u;
	CC(String nextUrl, up3 u) {
		this.nextUrl = nextUrl;
		this.u = u;
	}
	public void run() {
		System.out.println(nextUrl);
		u.fetch(nextUrl);
	}
}
public class up3 {
	static LinkedHashMap<String, LinkedList<String>> s = new LinkedHashMap<String, LinkedList<String>>();
    static HashSet<String> r = new HashSet<String>();
    static HashSet<String> b = new HashSet<String>();
	static up3 u = new up3();
	public static void fetch(String urlName) {
		try {
			URL url = new URL(urlName);
			if(url.getAuthority().indexOf("www.cs.purdue.edu")!=-1 && url.getHost().indexOf("www.cs.purdue.edu")!=-1 && url.getDefaultPort()==80 && url.getProtocol().indexOf("http")!=-1) {
				URLConnection connection = url.openConnection();
				connection.connect();
				if(connection.getContentType().indexOf("html")!=-1 && connection.getHeaderField(null).indexOf("HTTP")!=-1) {
					int o = 0;
					Scanner in = new Scanner(connection.getInputStream());
					StringBuilder sb = new StringBuilder();
					while(in.hasNextLine()) {
						sb.append(in.nextLine());
						o++;
					}
					if(o<8){
						System.out.println("Empty html!!! " + urlName);
						r.add(urlName);
					}
					String patternString = "<a\\s+href\\s*=\\s*(\"[^\"]*\")\\s*>";
					Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(sb);
					LinkedList<String> l = new LinkedList<String>();
					while(matcher.find()) {
						int start = matcher.start(1);
						int end = matcher.end(1);
						String match = sb.substring(start+1, end-1);
						if(match.indexOf("mailto:")==-1 && !match.contains("#")) {
							if(match.indexOf("://")==-1) {
								if(match.startsWith("/"))
									match = url.getProtocol() + "://" + url.getAuthority() + match;
								else
									match = url.getProtocol() + "://" + url.getAuthority() + "/" + match;
							}
							URL test = new URL(match);
							if(test.getAuthority().indexOf("www.cs.purdue.edu")!=-1 && test.getHost().indexOf("www.cs.purdue.edu")!=-1 && test.getDefaultPort()==80 && test.getProtocol().indexOf("http")!=-1) {
								if(!l.contains(match)&&!r.contains(match)&&!b.contains(match)) {
									l.add(match);
									new Thread(new CC(match, u)).start();
								}
							}
						}
					}
					if(l.size()>0) 
						s.put(urlName, l);
					else 
						System.out.println("No further URL!!! " + urlName);
				}else {
					System.out.println("Not html format!!! " + urlName);
					b.add(urlName);
				}
			}else {
				System.out.println("Unsafe URL!!! " + urlName);
				r.add(urlName);
			}
		}catch(MalformedURLException ue) {
			System.out.println("Malformed URL!!! " + urlName);
			r.add(urlName);
		}catch(IOException ie) {
			System.out.println("Expired URL!!! " + urlName);
			r.add(urlName);
		}catch(Exception e) {
			System.out.println("Unexpect Error!!! " + urlName);
			r.add(urlName);
		}
	}
	public static void main(String[] args) {
	    fetch("http://www.cs.purdue.edu");
	}
}
