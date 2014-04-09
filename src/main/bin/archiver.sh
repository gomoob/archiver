
CURRENT_DIR=$(dirname $(readlink -f "$0"))

CLASSPATH="$CURRENT_DIR/../conf/*"
CLASSPATH="$CLASSPATH:$CURRENT_DIR/../lib/*"

java -cp $CLASSPATH com.gomoob.archiver.Archiver $*
