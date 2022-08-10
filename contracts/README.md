# Smart Contracts in Java
Originally smart contracts were written in he solidity language, but since this blockchain is written in Java, so should the smart contracts.

Initially, the desire was to load the compiled bytecode of the java class and keep in the database just as ethereum does, but there was a problem when trying to instantiate the bytecode of unsigned objects from within a signed JAR, it didn't work and threw an exception!

Java has the ability to compile the source code via JIT, therefore the java code is save to the database referenced by a contract address, and then instantitating the object will involve performing a compiilation and setting the common static parameters after instantiation.

See more at https://www.logicbig.com/tutorials/core-java-tutorial/java-se-compiler-api/compiler-api-string-source.html
