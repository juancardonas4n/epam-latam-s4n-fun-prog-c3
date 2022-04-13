SOURCES = $(wildcard *.dia)
OBJECTS = $(SOURCES:.dia=.png)

all: ${OBJECTS}

%.png: %.dia
	dia -t png -s 1024x $<

clean:
	rm -f ${OBJECTS}
