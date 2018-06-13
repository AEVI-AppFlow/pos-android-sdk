#!/bin/bash

LOCAL_KEYSTORE_FOLDER="keystores"
GRADLE_PROPERTIES_FILE="gradle.properties"
OUTPUT_PROPS=${GRADLE_PROPERTIES_FILE}

mkdir -p "$LOCAL_KEYSTORE_FOLDER"

echo "Getting keystores from S3"
aws s3 cp "s3://keystores.aevi/aeviPlatform.jks" "$LOCAL_KEYSTORE_FOLDER" --region eu-west-1
aws s3 cp "s3://keystores.aevi/aeviReleaseKeystore.jks" "$LOCAL_KEYSTORE_FOLDER" --region eu-west-1

echo "Create gradle properties file"
echo "org.gradle.jvmargs=-Xmx2048m -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8" > ${OUTPUT_PROPS}
echo "platform_keystore_path=$PWD/$LOCAL_KEYSTORE_FOLDER/aeviPlatform.jks" > ${OUTPUT_PROPS}
echo "keystore_path=$PWD/$LOCAL_KEYSTORE_FOLDER/aeviReleaseKeystore.jks" >> ${OUTPUT_PROPS}
echo "platform_key_password=$AEVI_PLATFORM_KEY_PASSWORD" >> ${OUTPUT_PROPS}
echo "platform_store_password=$AEVI_PLATFORM_STORE_PASSWORD" >> ${OUTPUT_PROPS}
echo "keystore_key_password=$AEVI_KEYSTORE_KEY_PASSWORD" >> ${OUTPUT_PROPS}
echo "aws_accessid=$AEVI_ARTIFACTORY_AWS_ACCESS_ID" >> ${OUTPUT_PROPS}
echo "aws_accesskey=$AEVI_ARTIFACTORY_AWS_SECRET" >> ${OUTPUT_PROPS}
echo "bintrayUser=$BINTRAY_USER" >> ${OUTPUT_PROPS}
echo "bintrayKey=$BINTRAY_KEY" >> ${OUTPUT_PROPS}