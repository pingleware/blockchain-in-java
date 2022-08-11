# Blockchain in Java
Building a blockchain with smart contracts using Java instead of Ethereum and Solidity. Both Java and Solidity create bytecode and ABI, instead of invooking an ethereum virtual machine to execute the smart contracts written in Solidity, the Java blockchain invokes a Java virtual machine or JVM to execute the smart cotnracts written in Java.

Java is matured language and is deployed in many financial instituions, in addition Java has the large community of libraries or packages to implement any real life scenario on the Web3 stack but using Java. In addition, Java supports API consumption, so oracles will not be needed.

# Goal
To develop a cost effective alternative trading system for small issuers (less than $75 million dollars public float) performing a S-1 direct public offering (DPO). No unregistered securities will be permitted to be traded on the public mainnet of this network as identiifed in blockchain.json.

Private nodes are permitted and investors should use due diligence before trading on any private node!

# Java History
Java, originally developed by Sun Microsoystems as a write once, run anywhere (WORA), as long as the target system supports Java.

Teh complete history at https://www.geeksforgeeks.org/the-complete-history-of-java-programming-language/

# Building
To build requires Java JDK to be installed on your development machine.

## Compile

    javac *.java

## Create an Executable JAR File

    jar -cvfm blockchain.jar blockchain.mf *.class

## Run an executable JAR from the command line

    java -jar blockchain.jar

## Creating a keystore for signing

    keytool -genkey -v -keystore blockchain.keystore -alias blockchain -keyalg RSA -keysize 2048 -validity 10000

## Export public key from keystore for verifying

    keytool -export -alias blockchain -keystore blockchain.keystore -file blockchain.pem

## Signing a JAR file

    jarsigner -verbose -sigalg SHA1withRSA -tsa http://timestamp.sectigo.com?td=sha256 -digestalg SHA1 -keystore blockchain.keystore -signedjar SignedBlockchain.jar blockchain.jar blockchain
    
## Run an signed executable JAR from the command line
This will start the blockchain as an RMI server

    java  -jar SignedBlockchain.jar

## Verify a signed JAR file

    jarsigner -verify -verbose -certs SignedBlockchain.jar

## Starting Client
The client code is located in client/Client.java and uses RMI.

    java Client

## Builkding the Client

    cd client && javac -cp ../ Client.java && del ../Client.class && move Client.class .. && cd .. 
    
# Registration of a permissible node
To prevent disruptive players from connecting with this blockchain, a public registration process is created. To participate, clone (fork) this repository, make a new entry in the blockchain.json file with your static ip address of your node, then commit and publish a change request. You are required to be registered with the SEC as an ATS to participate in the network.

The blockchain.json is currently being populated with existing approved ATS providers from https://www.sec.gov/foia/docs/atslist. The presence of an ATS in this file does not indicated that ATS has made any indication or promise to participate in this network when production is live?

PressPage Entertainment Inc the parent of RedeeCash will register this network as an ATS with the SEC after development is completed.

