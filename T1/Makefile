all: clean_out output clean

output: MulticastPeer.class
	java MulticastPeer 224.0.0.42

MulticastPeer.class: MulticastPeer.java
	javac MulticastPeer.java

clean_out:
	rm -rf output
	rm -rf *.class

clean:
	rm -rf *.class
