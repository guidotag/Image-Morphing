GUI = gui
MORPH = src

.PHONY: all bundle clean

all:
	# Cleaning previous bundle...
	make clean
	# Creating necessary folders...
	mkdir bundle
	mkdir bundle/segments
	mkdir bundle/output
	# Building the GUI...
	cd $(GUI); make; jar cfe gui.jar Main *.class; mv gui.jar ../bundle
	# Building morph...
	cd $(MORPH); make; mv morph ../bundle

bundle:
	

clean:
	rm -rf bundle