#! /bin/bash 

if [ $# -eq 0 ]; then
	echo $0: error, no argument specified, for help launch with the argument -h
	exit
elif [ $# -gt 1 ]; then
	echo $0: error, too many arguments specified, for help launch with the argument -h
	exit
fi

if [ "$1" == "-h" -o "$1" == "-help" ]; then
	echo To run include path to compressed files, example  \$ ./decompress.sh \"$HOME\"
	exit
fi

dir=$1

if [[ -z "$dir" ]]; then
	echo $0: error, mandatory argument missing, for help launch with the argument -h
	exit
fi

cd $dir

files=$(find -name '*.gz')

for file in $files
do
	echo decompressing $file
	gzip -d $file
done