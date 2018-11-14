#include<stdio.h>
#include<string.h>
#include <stdlib.h>
#include <android/log.h>
#include "com_edu_edumediaplayer_playback_MP3Reader.h"
#include "minimp3.h"

mp3_decoder_t mp3;
mp3_info_t info;
int fd, pcm;
void *file_data;
char* raw_buf;
unsigned char *stream_pos;
signed short sample_buf[MP3_MAX_SAMPLES_PER_FRAME];
int bytes_left;
int frame_size;
int total_count = 0;


/*
 * Class:     com_edu_edumediaplayer_playback_MP3Reader
 * Method:    readHeader
 * Signature: (Ljava/lang/String;)Lcom/edu/edumediaplayer/playback/SongFileInfo;
 */
JNIEXPORT jobject JNICALL Java_com_edu_edumediaplayer_playback_MP3Reader_readHeader
  (JNIEnv *env, jclass cls_in, jstring file_name){

        // return class result //TODO: should return first chunk of data
        jclass cls = (*env)->FindClass(env, "com/edu/edumediaplayer/playback/SongFileInfo");
        jmethodID constructor = (*env)->GetMethodID(env, cls, "<init>", "(IIIIIII)V");
        const char *file_name_native = (*env)->GetStringUTFChars(env, file_name, 0);

        // opening the file
        FILE *fd = fopen(file_name_native, "r");
        if (fd == NULL) {
            __android_log_print(ANDROID_LOG_INFO, "Native", "Error: Failed to read file %s", file_name_native);
            return NULL;
        }

        // reading file
        fseek(fd, 0, SEEK_END);
        bytes_left = ftell(fd);
        fseek(fd, 0, SEEK_SET);
        raw_buf = malloc(bytes_left);
        if (raw_buf==NULL) {
            __android_log_print(ANDROID_LOG_INFO, "Native", "Error: Could not allocate enough memory to read file");
            return (*env)->NewObject(env, cls, constructor, 0, 0, 0, 0, 0, 0, -2);
        }
        fread(raw_buf, 1, bytes_left, fd);
        file_data = raw_buf;
        stream_pos = (unsigned char *) file_data;
        bytes_left -= 100;

        mp3 = mp3_create();
        frame_size = mp3_decode(mp3, stream_pos, bytes_left, sample_buf, &info);

        if (!frame_size || info.audio_bytes==-1) {
            __android_log_print(ANDROID_LOG_INFO, "Native", "Error: Not a valid MP3 file");
            return (*env)->NewObject(env, cls, constructor, 0, 0, 0, 0, 0, 0, -1);
        }

        //TODO: hardcoded 16 bits, whatchout
        jobject object = (*env)->NewObject(env, cls, constructor, 1, 2, info.channels, info.sample_rate, 16, frame_size, 0);

        return object;

  }


  /*
   * Class:     com_edu_edumediaplayer_playback_MP3Reader
   * Method:    readPcm
   * Signature: (Ljava/lang/String;)[S
   */
  JNIEXPORT jshortArray JNICALL Java_com_edu_edumediaplayer_playback_MP3Reader_readPcm
    (JNIEnv *env, jclass cls_in, jstring file_name){


        jbyteArray result;



        if ((bytes_left >= 0) && (frame_size > 0)) {

            stream_pos += frame_size;
            bytes_left -= frame_size;
            frame_size = mp3_decode(mp3, stream_pos, bytes_left, sample_buf, NULL);

            total_count += info.audio_bytes/sizeof(signed short);
            __android_log_print(ANDROID_LOG_INFO, "Native", "info %d", total_count);
            result=(*env)->NewShortArray(env, info.audio_bytes/sizeof(signed short));
            (*env)->SetShortArrayRegion(env, result, 0, info.audio_bytes/sizeof(signed short), sample_buf);



        } else {

            result=(*env)->NewShortArray(env, 0);
            free(raw_buf);
        }

//      result=(*env)->NewShortArray(env, 1);

        return result;

    }