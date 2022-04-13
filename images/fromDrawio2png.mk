SOURCES = $(wildcard *.drawio)
OBJECTS = $(SOURCES:.drawio=.png)

all: ${OBJECTS}

%.png: %.drawio
	drawio --export --format png --output=$@ $<

clean:
	rm -f ${OBJECTS}
