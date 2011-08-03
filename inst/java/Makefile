CLASSPATH := .:$(shell echo ../inst/java/*.jar | tr ' ' ':')
CLASSES := $(patsubst %.java,%.class,$(wildcard *.java))
JAR := ../inst/java/rigb.jar

.PHONY: clean

%.class : %.java
	javac -classpath $(CLASSPATH) $<

all: $(JAR)

$(JAR): $(CLASSES)
	jar cvf $@ *.class

clean:
	rm -vf *.class
