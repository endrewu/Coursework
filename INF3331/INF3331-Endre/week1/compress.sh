if [[ $# -eq 0 ]]; then
	echo $0: error, no arguments specified, for help launch with the argument -h
	exit
elif [ $# -gt 2 ]; then
	echo $0: error, too many arguments specified, for help launch with the argument -h
	exit
fi

if [ "$1" == "-h" -o "$1" == "-help" ]; then
	echo To run include path to files you wish to compress and minimum size in kB, example  \$ ./compress.sh \"$HOME\" 30
	exit
fi

dir=$1
size=$2
size+="k"

if [[ -z "$dir" || -z "$size" ]]; then
	echo $0: error, mandatory arguments missing, for help launch with the arguments -h
	exit
fi

cd $dir
files=$(find -name '*' -type f -size +$size)
for file in $files
do
	echo $file
	gzip $file
done