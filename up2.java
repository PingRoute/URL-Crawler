import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
public class up2 {
	static LinkedList<String> l = new LinkedList<String>();
	static int position = 0;
	static HashSet<String> r = new HashSet<String>();
	static LinkedList<String> b = new LinkedList<String>();
	public static void fetch(String urlName) {
		try {
			position = l.indexOf(urlName) + 1;
			URL url = new URL(urlName);
			if(url.getAuthority().indexOf("www.cs.purdue.edu")!=-1 && url.getHost().indexOf("www.cs.purdue.edu")!=-1 && url.getDefaultPort()==80 && url.getProtocol().indexOf("http")!=-1){
				URLConnection connection = url.openConnection();
				connection.connect();
				if(connection.getContentType().indexOf("html")!=-1 && connection.getHeaderField(null).indexOf("HTTP")!=-1){
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
						position = l.indexOf(urlName);
						l.remove(urlName);
					}
					String patternString = "<a\\s+href\\s*=\\s*(\"[^\"]*\")\\s*>";
					Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
					Matcher matcher = pattern.matcher(sb);
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
							String t2 = test.getAuthority();
							if(t2.indexOf("www.cs.purdue.edu")!=-1&&!l.contains(match)&&!r.contains(match)&&!b.contains(match)) {
								//System.out.println(match);
								l.add(match);
							}
						}
					}
				}else {
					System.out.println("Not html format!!! " + urlName);
					if(!b.contains(urlName))
					b.add(urlName);
					position = l.indexOf(urlName);
					l.remove(urlName);
				}
			}else {
				System.out.println("Unsafe URL!!! " + urlName);
				r.add(urlName);
				position = l.indexOf(urlName);
				l.remove(urlName);
			}
		}catch(MalformedURLException ue) {
			System.out.println("Malformed URL!!! " + urlName);
			r.add(urlName);
			position = l.indexOf(urlName);
			l.remove(urlName);

		}catch(IOException ie) {
			System.out.println("Expired URL!!! " + urlName);
			r.add(urlName);
			position = l.indexOf(urlName);
			l.remove(urlName);
		}catch(Exception e) {
			System.out.println("Unexpect Error!!! " + urlName);
			r.add(urlName);
			position = l.indexOf(urlName);
			l.remove(urlName);
		}
	}
	public static void main(String[] args) {
		up2 u = new up2();
		u.fetch("http://www.cs.purdue.edu");
		for(String x : l)
			System.out.println(x);
		System.out.println(l.size());
		/*
		while(position < 2000) {
			System.out.println(position + ": " + l.get(position));
			u.fetch(l.get(position));
		}
		*/
	}
}


