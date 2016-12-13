#Change to location of your Java SE installation.

CLASSPATH=.

JAVAC=javac
JAVA=java

ORB_INITIAL_PORT=2810

export CLASSPATH=.
IDLJ :=idlj

IDLJ_FLAGS=-fall -td . -verbose

ORBD=orbd -ORBInitialPort ${ORB_INITIAL_PORT}

SERVERTOOL=servertool

build: stubs

stubs:
	$(IDLJ) $(IDLJ_FLAGS) App.idl; $(JAVAC) *.java;$(JAVAC) App/*.java

client:
	$(JAVA) AppClient $(ARG)

bank:
	$(JAVA) AppBank $^

test:
	$(JAVA) -ea LancerTest

runorbd:
	$(ORBD)

# Enter the following command in servertool to register server:
# (without the # sign)
# register -server AppInterBank -classpath . -applicationName interbankregister

servertool:
	$(SERVERTOOL) -ORBInitialPort $(ORB_INITIAL_PORT)

clean:
	rm -rf App;
	rm -rf *.class
