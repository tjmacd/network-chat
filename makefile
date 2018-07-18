all:
	javac -d bin/ src/*.java 

run:
	xterm -e java -cp bin/ Server 4000 &
	java -cp bin/ Client &
	java -cp bin/ Client &

clean:
	rm -rf bin/
