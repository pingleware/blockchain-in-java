/**
 * SERVER.java
 * 
 * The main entry point which cretes a Web and RPC server for starting the blockchain and providing endpoints for blockchain operations
 * 
 * IMPORTANT:
 * To ensure integrity is maintained, the first task is to verify the jar file is signed using the correct keystore by using the public key?
 */
import java.security.*;
import java.util.*;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Server {

    private static ArrayList<String> ipAddress;

    public static void main (String [] args){
        try {
            // 1. Verify the node is registered validating IP and node name gainst github.com public repository?
            // if not registered, thhrow an exception and end the program

            ipAddress = new ArrayList<String>();

            String atsList = sendGET("https://raw.githubusercontent.com/pingleware/blockchain-in-java/main/blockchain.json");
            String items[] = atsList.split("\"([0-9]|[0-9][0-9]|[0-9][0-9][0-9])\":");
            for (int i=0; i<items.length; i++) {
                String item[] = items[i].split("\"ip\":");
                for (int j=1; j<item.length; j++) {
                    String ip[] = item[j].split(",");
                    String _ipAddress = ip[0].replace("\"","").substring(1);
                    ipAddress.add(_ipAddress); 
                }
            }

            //System.out.println(getPublicIpAddress());
            String localIPAddress = getPublicIpAddress();
            boolean found = false;
            int total = ipAddress.size();
            for (int k=0; k < total; k++) {
                String _ip = ipAddress.get(k);
                if (stringCompare(_ip,localIPAddress) != 0) {
                    found = true;
                    System.out.println("FOUND!");
                    break;
                }
            }
            if (found == false) {
                throw new Exception("Not authorized as a public node?");
            }

            // Start JSON RPC Server

            // Start REST API Server
           
        } catch (IOException | ProviderException exception){
           System.err.println("JavaServer: " + exception);
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public static int stringCompare(String str1, String str2)
    {
  
        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);
  
        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);
  
            if (str1_ch != str2_ch) {
                return str1_ch - str2_ch;
            }
        }
  
        // Edge case for strings like
        // String 1="Geeks" and String 2="Geeksforgeeks"
        if (l1 != l2) {
            return l1 - l2;
        }
  
        // If none of the above conditions is true,
        // it implies both the strings are equal
        else {
            return 0;
        }
    }
    
    private static String getPublicIpAddress() {
        String ipAddress = null;
        try {
            String response = sendGET("http://ip-api.com/json/");
            String items[] = response.split("\"query\":");
            ipAddress = items[1].substring(1,items[1].length()-2);
        } catch (NoClassDefFoundError | Exception e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    private static String sendGET(String url) throws IOException {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		int responseCode = con.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
            return response.toString();
		} else {
            return null;
		}
	}
}