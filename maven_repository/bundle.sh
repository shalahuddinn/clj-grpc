#! /bin/bash

USAGE(){
    println "Usage: `basename $0` -g GroupId -a ArtifactId  -f file [-voh] args\n\n";

    println "Options:";
    println "   h   Print usage";
    println "   v   Version (Defaults to 1.0)";
    println "   o   Output zip file name (Defaults to GroupId.ArtifactId.zip)";


}
println(){

    echo "$1";
}


VERSION="1.0";

# Parse command line options.
while getopts hv:o:g:a:f: OPT; do
    case "$OPT" in
        h)
            USAGE
            exit 0
            ;;
        v)
          VERSION=$OPTARG;
            ;;
    g)
          GROUP_ID=$OPTARG;
            ;;
    a)
          ARTIFACT_ID=$OPTARG;
            ;;
    f)
            INPUT_FILE=$OPTARG
            ;;
        o)
            OUTPUT_FILE=$OPTARG
            ;;
        \?)
            # getopts issues an error message
        echo "Error: " >&2;
            USAGE
            exit 1
            ;;
    esac
done

if [ -z "${OUTPUT_FILE}" ]; then
    OUTPUT_FILE="$GROUP_ID.$ARTIFACT_ID.zip";
fi



# Remove the switches we parsed above.
shift `expr $OPTIND - 1`

if [ -z "${ARTIFACT_ID}" ]; then
    echo "Error: You must specify an artifact id."
fi

if [ -z "${GROUP_ID}" ]; then
    echo "Error: You must specify an group id."
fi

if [ -z "${INPUT_FILE}" ]; then
    echo "Error: You must specify an group id."
fi

if [ ! -f "${INPUT_FILE}" ];
then
     echo "Error: Input file '$INPUT_FILE' does not exist."
fi

# Create a temp directory which we will use as our 'local repository'
TEMPDIR=$(mktemp -dt "build-maven-dep.XXXXXXX")

TEMPDIR_SUB="$GROUP_ID.$ARTIFACT_ID";
TEMP_REPO_LOC="$TEMPDIR/$TEMPDIR_SUB";
mkdir -p $TEMP_REPO_LOC;

mvn install:install-file -DlocalRepositoryPath=$TEMP_REPO_LOC -DgroupId=$GROUP_ID -DartifactId=$ARTIFACT_ID -Dversion=$VERSION -Dfile=$INPUT_FILE -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true

CUR_DIR=$(pwd);

# Enter the temp repository we created which is now populated.
cd $TEMP_REPO_LOC;

PACKAGE_STRUC="$GROUP_ID.$ARTIFACT_ID";

# Dive down into directory structure until we get to the *.xml files.
IFS='. ' read -ra ADDR <<< $PACKAGE_STRUC
for i in "${ADDR[@]}"; do
   println "Moving into: $i";
   cd $i;
   println "Now  in $(pwd)";
done

# Rename the files to what maven expects.
mv maven-metadata-local.xml maven-metadata.xml
mv maven-metadata-local.xml.md5 maven-metadata.xml.md5
mv maven-metadata-local.xml.sha1 maven-metadata.xml.sha1

# Zip up our results.
cd $TEMP_REPO_LOC;
cd ..;
zip -r $OUTPUT_FILE $TEMPDIR_SUB
mv $OUTPUT_FILE $CUR_DIR

# Return back to our original directory and remove the temp directory
cd $CUR_DIR;
rm -Rf $TEMPDIR;

# EOF