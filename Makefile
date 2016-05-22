GUI_DIR = gui
MORPH_DIR = src
BUILD_DIR = build

.PHONY: all bundle clean

all:
	# Cleaning previous bundle
	make clean
	# Creating necessary folders
	mkdir $(BUILD_DIR)
	mkdir $(BUILD_DIR)/segments
	mkdir $(BUILD_DIR)/output
	# Building the GUI
	cd $(GUI_DIR); make; jar cfe gui.jar Main *.class; mv gui.jar ../$(BUILD_DIR)
	# Building morph
	cd $(MORPH_DIR); make; mv morph ../$(BUILD_DIR)

bundle:
	

clean:
	rm -rf $(BUILD_DIR)