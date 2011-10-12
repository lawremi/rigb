run: bundle.jar Main.class
	java -cp felix.jar:bundle.jar:. Main

Main.class: Main.java
	javac -cp bundle.jar Main.java

Activator.class: Activator.java
	javac -cp felix.jar Activator.java

bundle.jar: Activator.class manifest.mf
	jar cfm bundle.jar manifest.mf Activator.class
