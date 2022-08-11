import java.io.*;
import java.util.Scanner;  
import java.util.ArrayList;
import java.net.URL;
import java.sql.*;
import java.net.HttpURLConnection;
import java.io.IOException;

public class RemoteMethods implements IRemoteMethods {  
    private static ArrayList<String> ipAddress = new ArrayList<String>();

    // Implementing the interface method 
    public void printMsg() {  
       System.out.println("This is an example RMI program");  
    }  
    public void loadValidATS() {
        try {
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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
    public String getPublicIpAddress() {
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
    public Wallet addNewWallet(String passPhrase) {
        return new Wallet("",passPhrase);
    }
    public String saveToken(String name,String symbol,int decimals,double totalSupply) {
        String contents = "";

        try {
            File myObj = new File("contracts/" + name + ".java");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              contents += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }  
        System.out.println(contents);      
        Block block = new Block(contents);
        String sql = "INSERT INTO contracts (address,bytecode,block_timestamp,block_number,block_hash) VALUES ('"+block.hash+"','"+contents+"',"+block.timeStamp+","+block.nonce+",'"+block.hash+"');";
        System.out.println(sql);
        Database.insert(sql);

        sql = "INSERT INTO tokens (address,symbol,name,decimals,total_supply,block_timestamp,block_hash) VALUES ('"+block.hash+"','"+symbol+"','"+name+"',"+decimals+","+totalSupply+",'"+block.timeStamp+"','"+block.hash+"');";
        Database.insert(sql);

        return block.hash;
    }

    public String getExchangeRates(String currency) {
        try {
            return sendGET("https://open.er-api.com/v6/latest/" + currency);
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
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

    private byte[] loadClassFromDatabase(String address)  {

        byte[] buffer;
        int nextValue = 0;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        try {
            String sql = "SELECT * FROM contracts WHERE address='"+address+"';";
            ResultSet rs = Database.query(sql);
            String sourceCode = rs.getString("bytecode");
            /*
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            JavaSourceFromString jsfs = new JavaSourceFromString( "CustomProcessor", sourceCode);
            Iterable<? extends JavaFileObject> fileObjects = Arrays.asList( jsfs);
            List<String> options = new ArrayList<String>();
            options.add("-d");
            options.add( compilationPath);
            options.add( "-classpath");
            URLClassLoader urlClassLoader =
                     (URLClassLoader)Thread.currentThread().getContextClassLoader();
            StringBuilder sb = new StringBuilder();
            for (URL url : urlClassLoader.getURLs()) {
                sb.append(url.getFile()).append(File.pathSeparator);
            }
            sb.append( compilationPath);
            options.add(sb.toString());
            
            StringWriter output = new StringWriter();
            boolean success = compiler.getTask( output, null, null, options, null, fileObjects).call(); 
            if( success) {
                logger.info( LOG_PREFIX + "Class has been successfully compiled");
            } else {
                throw new Exception( "Compilation failed :" + output);
            }     
            */       
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (Database.connection != null) {
                    Database.connection.close();
                }
            } catch(SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }    

    private static void saveClass(String className, String _owner, double _amount) {
        String contents = "";

        try {
            File myObj = new File("contracts/" + className + ".java");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              contents += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }  
        System.out.println(contents);      
        Block block = new Block(contents);
        String sql = "INSERT INTO contracts (address,bytecode,block_timestamp,block_number,block_hash) VALUES ('"+block.hash+"','"+contents+"',"+block.timeStamp+","+block.nonce+",'"+block.hash+"');";
        System.out.println(sql);
        Database.insert(sql);

        String symbol = "TK";
        String name = "Token";
        int decimals = 2;
        double totalSupply = 20000;
        sql = "INSERT INTO tokens (address,symbol,name,decimals,total_supply,block_timestamp,block_hash) VALUES ('"+block.hash+"','"+symbol+"','"+name+"',"+decimals+","+totalSupply+",'"+block.timeStamp+"','"+block.hash+"');";
        Database.insert(sql);

        /*
            Class myClass = Class.forName(className);
            Object token = myClass.getDeclaredConstructors().clone();
            ConvertObject convertObject = new ConvertObject();
            byte[] byteArrayObject = convertObject.getByteArrayObject(token.getClass());            
            
            */
        /*
        byte[] buffer;
        int nextValue = 0;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            Class myClass = Class.forName(className);
            InputStream inputStream = myClass.getResourceAsStream("contracts/" + className + ".class");
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
            buffer = byteStream.toByteArray();
            Block block = new Block(buffer.toString());
            String sql = "INSERT INTO contracts (address,bytecode,block_timestamp,block_number,block_hash) VALUES ('"+block.hash+"','"+buffer.toString()+"',"+block.timeStamp+","+block.nonce+",'"+block.hash+"');";
            Database.insert(sql);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        */
    }
} 