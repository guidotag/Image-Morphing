GUI_DIR = gui
MORPH_DIR = src
BUILD_DIR = build
JAR = gui.jar
BIN = morph

.PHONY: all bundle clean

all:
	# Cleaning previous build
	make clean
	# Creating necessary folders
	mkdir $(BUILD_DIR)
	mkdir $(BUILD_DIR)/segments
	mkdir $(BUILD_DIR)/output
	# Building the GUI
	cd $(GUI_DIR); make; jar cfe $(JAR) Main *.class
	mv $(GUI_DIR)/$(JAR) $(BUILD_DIR)
	# Building morph
	cd $(MORPH_DIR); make
	mv $(MORPH_DIR)/$(BIN) $(BUILD_DIR)

bundle:
	

clean:
	rm -rf $(BUILD_DIR)