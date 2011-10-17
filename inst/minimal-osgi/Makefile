.PHONY: run clean

run: bundle.jar Main.class
	java -cp felix.jar:bundle.jar:REngine.jar:. Main

Main.class: Main.java
	javac -cp bundle.jar:REngine.jar Main.java

Activator.class: Activator.java
	javac -cp felix.jar:REngine.jar Activator.java

bundle.jar: Activator.class manifest.mf
	jar cfm bundle.jar manifest.mf REngine.jar Activator.class

clean:
	rm -vf bundle.jar *.class
