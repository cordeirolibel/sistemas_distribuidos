all: clean_out output clean

output: MulticastPeer.class Main.class
	java Main 224.0.0.42

MulticastPeer.class: MulticastPeer.java
	javac MulticastPeer.java
	javac -d . MulticastPeer.java

Main.class: Main.java
		javac Main.java

clean_out:
	rm -rf output
	rm -rf *.class

clean:
	rm -rf *.class
