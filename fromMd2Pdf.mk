SOURCES = $(wildcard C*V[0-9][0-9].md)
OBJECTS = $(SOURCES:.md=.pdf)

all: ${OBJECTS}

%.pdf: %.md
	pandoc -t beamer --output=$@ $<

clean:
	rm -f ${OBJECTS}
