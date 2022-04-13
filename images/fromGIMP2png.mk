SOURCES = $(wildcard *.xcf)
OBJECTS = $(SOURCES:.xcf=.png)

all: ${OBJECTS}

%.png: %.xcf
	xcf2png -f $<

clean:
	rm -f ${OBJECTS}
