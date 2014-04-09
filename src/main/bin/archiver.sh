
CURRENT_DIR=$(dirname $(readlink -f "$0"))

set CLASSPATH="$CURRENT_DIR/../conf/*"
set CLASSPATH="$CURRENT_DIR/../lib/*"

java -cp $CLASSPATH com.gomoob.archiver.Archiver $*
