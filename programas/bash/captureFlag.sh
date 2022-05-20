#!/usr/bin/sh

function usage() {
    echo "Usage: $0 captureFlag [-t]"
    exit 0
}

function exec_test() {
    testInput=$2
    testOutput=$3
    finalOutput=$4
    echo "Running test $1"
    cat $testInput | sbt 2>&1 > $testOutput
    cat $testOutput | grep "%100" | sed 's/Current *Course *Grade.*$//g' | sed 's/^.*Grade: //g' | sed 's/,/./g' | sed 's/ *//g' | cat - >> $finalOutput
    rm $testInput $testOutput
}

noTest="No"

while getopts "t" options; do
    case "${options}" in
        t)
            noTest="Yes"
            ;;
        *)
            usage
            ;;
    esac
done

echo "Compiling"
sbt compile 2>&1 >/dev/null
if [ ! $? -eq 0 ]
then
    echo "Error: No compile"
    exit 1
fi

testDir=$(mktemp -d -t captureFlag.XXXXX)

cat <<EOF > $testDir/test01
run
add "Course A" 10 ( "Nota 1" : 35, "Nota 2" : 46, "Nota 3" : 17, "Nota 4" : 18, "Nota 5" : 39 )
grade "Course A:Nota 1" 30
grade "Course A:Nota 2" 36
grade "Course A:Nota 3" 12
grade "Course A:Nota 4" 18
grade "Course A:Nota 5" 18
exit
exit
EOF

cat <<EOF > $testDir/test02
run
add "Course B" 20 ( "Eval 1" : 21, "Eval 2" : 32, "Eval 3" : 36, "Eval 4" : 40 )
grade "Course B:Eval 1" 18
grade "Course B:Eval 2" 16
grade "Course B:Eval 3" 15
grade "Course B:Eval 4" 17
exit
exit
EOF

cat <<EOF > $testDir/test03
run
add "Course C" 10 ( "Nota 1" : 90, "Nota 2" : 100, "Nota 3" : 130 )
grade "Course C:Nota 1" 30
grade "Course C:Nota 2" 36
grade "Course C:Nota 3" 12
exit
exit
EOF

cat <<EOF > $testDir/test04
run
add "CD" 10 ( "N1" : 30, "N2" : 40, "N3" : 50, "N4" : 60, "N5" : 70, "N6" : 80)
grade "CD:N1" 15
grade "CD:N2" 20
grade "CD:N3" 25
grade "CD:N4" 30
grade "CD:N5" 35
grade "CD:N6" 40
exit
exit
EOF

cat <<EOF > $testDir/test05
run
add "CE" 70 ( "E1" : 34, "E2" : 35, "E3" : 40, "E4" : 34, "E5" : 40 )
grade "CE:E1" 32
grade "CE:E2" 35
grade "CE:E3" 39
grade "CE:E4" 30
grade "CE:E5" 38
exit
exit
EOF

expectedOutput=${testDir}/expectedOutput

cat <<EOF > ${expectedOutput}
3.68
2.56
1.22
2.50
4.75
EOF
realOutput=${testDir}/testOuput

for i in $(seq -f "%02g" 1 5)
do
    t="${testDir}/test${i}"
    to="${testDir}/test${i}Output"
    exec_test "$i" "$t" "$to" "$realOutput"
done

if [ "$noTest" == "Yes" ]
then
    cat $realOutput
    exit 0
fi

diff $realOutput $expectedOutput 2>&1 >/dev/null

if [ $? -eq 0 ]
then
    echo "All context is real"
else
    echo "New expectations are comming soon!"
fi
