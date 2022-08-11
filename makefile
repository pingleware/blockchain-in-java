all: SignedBlockchain.jar

clean:
	del SignedBlockchain.jar
	del blockchain.jar
	del blockchain.mf
	del *.class
	cd contracts && del *.class && cd ..
	
Block.class: Block.java
	javac Block.java

Blockchain.class: Blockchain.java
	javac Blockchain.java

GFG.class: GFG.java 
	javac GFG.java

Transaction.class: Transaction.java 
	javac Transaction.java

Database.class: Database.java
	javac Database.java

Wallet.class: Wallet.java IWallet.java
	javac Wallet.java

Common.class: Common.java
	javac Common.java

Server.class: Server.java
	javac Server.java

IRemoteMethods.class: IRemoteMethods.java 
	javac IRemoteMethods.java

RemoteMethods.class: RemoteMethods.java
	javac RemoteMethods.java

contracts/EIP20.class: contracts/EIP20.java
	javac contracts/EIP20.java

contracts/EIP20Listener.class: contracts/EIP20Listener.java
	javac contracts/EIP20Listener.java

contracts/EIP165.class: contracts/EIP165.java
	javac contracts/EIP165.java

contracts/EIP721.class: contracts/EIP721.java
	javac contracts/EIP721.java

contracts/EIP721Listener.class: contracts/EIP721Listener.java
	javac contracts/EIP721Listener.java

contracts/EIP721Metadata.class: contracts/EIP721Metadata.java
	javac contracts/EIP721Metadata.java

contracts/EIP721TokenReceiver.class: contracts/EIP721TokenReceiver.java
	javac contracts/EIP721TokenReceiver.java

blockchain.mf: lib/sqlite-jdbc-3.39.2.0.jar lib/json-20220320.jar
	@echo Manifest-Version: 1.0 > blockchain.mf
	@echo Main-Class: Server >> blockchain.mf
	@echo Class-Path: . contracts lib/sqlite-jdbc-3.39.2.0.jar lib/json-20220320.jar >> blockchain.mf

blockchain.jar: Block.class Blockchain.class GFG.class Transaction.class Database.class Wallet.class Server.class blockchain.mf RemoteMethods.class contracts/EIP20.class contracts/EIP20Listener.class contracts/EIP721TokenReceiver.class contracts/EIP721Metadata.class contracts/EIP721Listener.class contracts/EIP721.class contracts/EIP165.class Common.class
	jar -cvfm blockchain.jar blockchain.mf *.class contracts/*.class

blockchain.keystore: 
	keytool -genkey -v -keystore blockchain.keystore -alias blockchain -keyalg RSA -keysize 2048 -validity 10000

SignedBlockchain.jar: blockchain.jar blockchain.keystore
	jarsigner -verbose -sigalg SHA1withRSA -tsa http://timestamp.sectigo.com?td=sha256 -digestalg SHA1 -keystore blockchain.keystore -signedjar SignedBlockchain.jar blockchain.jar blockchain
