all: SignedBlockchain.jar

clean:
	del SignedBlockchain.jar
	del blockchain.jar
	del blockchain.mf
	del *.class
	
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

Wallet.class: Wallet.java
	javac Wallet.java

Server.class: Server.java
	javac Server.java

blockchain.mf: lib/sqlite-jdbc-3.39.2.0.jar lib/json-20220320.jar
	@echo Manifest-Version: 1.0 > blockchain.mf
	@echo Main-Class: Server >> blockchain.mf
	@echo Class-Path: . lib/sqlite-jdbc-3.39.2.0.jar lib/json-20220320.jar >> blockchain.mf

blockchain.jar: Block.class Blockchain.class GFG.class Transaction.class Database.class Wallet.class Server.class blockchain.mf
	jar -cvfm blockchain.jar blockchain.mf *.class

blockchain.keystore: 
	keytool -genkey -v -keystore blockchain.keystore -alias blockchain -keyalg RSA -keysize 2048 -validity 10000

SignedBlockchain.jar: blockchain.jar blockchain.keystore
	jarsigner -verbose -sigalg SHA1withRSA -tsa http://timestamp.sectigo.com?td=sha256 -digestalg SHA1 -keystore blockchain.keystore -signedjar SignedBlockchain.jar blockchain.jar blockchain
