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

Server.class: Server.java
	javac Server.java

blockchain.mf:
	@echo Manifest-Version: 1.0 > blockchain.mf
	@echo Main-Class: Server >> blockchain.mf
	@echo Class-Path: . lib/* >> blockchain.mf

blockchain.jar: Block.class Blockchain.class GFG.class Transaction.class Server.class blockchain.mf
	jar -cvfm blockchain.jar blockchain.mf *.class

blockchain.keystore: 
	keytool -genkey -v -keystore blockchain.keystore -alias blockchain -keyalg RSA -keysize 2048 -validity 10000

SignedBlockchain.jar: blockchain.jar blockchain.keystore
	jarsigner -verbose -sigalg SHA1withRSA -tsa http://timestamp.sectigo.com?td=sha256 -digestalg SHA1 -keystore blockchain.keystore -signedjar SignedBlockchain.jar blockchain.jar blockchain
