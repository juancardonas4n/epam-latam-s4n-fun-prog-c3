#!/usr/bin/bash

function usage() {
    echo "Usage: $0 [ -d DIR_GET_GRADING ] [ -g ]" 1>&2
    echo "    -d DIR_GET_GRADING : directory where app is installed" 1>&2
    echo "    -g : debug this script" 1>&2
    exit 0
}

debug=false
currDir=$(pwd)
dirGetGrading="../../projects/getGrading"

while getopts ":dgh" options
do
    case "${options}" in
        d)
            dirGetGrading=${OPTARG}
            ;;
        g)
            debug=true
            ;;
        h)
            usage
            ;;
        *)
            usage
            ;;
    esac
done

if [ -f "captureFlag.sh" ]
then
    if [ -f "captureFlag" ]
    then
        rm captureFlag
    fi
    shc -f captureFlag.sh -o captureFlag
    if [ -f "captureFlag.sh.x.c" ]
    then
        mv captureFlag.sh.x.c captureFlag.c
    fi
else
    echo "This script must be executed on a directory where a bash script 'captureFlag.sh' exits"
    exit 1
fi

tmpDirGetGrading=$(mktemp -d -t tmpGetGrading.XXXXX)
newGetGrading=$tmpDirGetGrading/getGrading

mkdir $newGetGrading

cp $dirGetGrading/build.sbt $newGetGrading/build.sbt
cp -R $dirGetGrading/src $newGetGrading/src
cat <<EOF > $newGetGrading/Makefile
all: project_code capture_Flag

CODE_DIR = captureFlag

.PHONY: project_code

project_code:
	\$(MAKE) -C \$(CODE_DIR)

capture_Flag:
	captureFlag/captureFlag

EOF
mkdir $newGetGrading/captureFlag
cp captureFlag.c $newGetGrading/captureFlag
cat <<EOF > $newGetGrading/captureFlag/Makefile
all: captureFlag

captureFlag: captureFlag.c

erase:
	rm -f captureFlag
EOF

cd $newGetGrading
pushd src/main/scala/com/epam/getgrading
sed -i 's/.*\/\/ Replace with/                                        \| /' CommandParser.scala
popd

cd ..

zip -r /tmp/getGrading.zip getGrading 

if [ "$debug" = true ]
then
    echo "You must erase manually the directory $tmpDirGetGrading"
else
    cd $currDir
    rm -R $tmpDirGetGrading
fi
