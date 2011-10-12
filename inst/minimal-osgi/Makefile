.PHONY: bundle.jar

bundle.jar:
	javac -cp felix.jar Activator.java && \
	jar cfm bundle.jar manifest.mf Activator.class
