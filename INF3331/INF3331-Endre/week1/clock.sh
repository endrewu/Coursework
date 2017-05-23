control_c() {
	echo bye bye
	exit
}

while : 
do
	trap control_c SIGINT
	echo $(date)
done