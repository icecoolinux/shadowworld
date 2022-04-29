#include "com_icecool_worldinshadow_ActividadOpenAL.h"

#include <stdio.h>
#include <AL/al.h>
#include <AL/alc.h>

typedef struct {
 char  riff[4];//'RIFF'
 unsigned int riffSize;
 char  wave[4];//'WAVE'
 char  fmt[4];//'fmt '
 unsigned int fmtSize;
 unsigned short format;
 unsigned short channels;
 unsigned int samplesPerSec;
 unsigned int bytesPerSec;
 unsigned short blockAlign;
 unsigned short bitsPerSample;
 char  data[4];//'data'
 unsigned int dataSize;
}BasicWAVEHeader;

//WARNING: This Doesn't Check To See If These Pointers Are Valid
char* readWAV(char* filename,BasicWAVEHeader* header, int* error){
 char* buffer = 0;
 FILE* file = fopen(filename,"rb");
 if (!file) {
 *error = -4;
 return 0;
 }

 if (fread(header,sizeof(BasicWAVEHeader),1,file)){
 if (!(//these things *must* be valid with this basic header
 memcmp("RIFF",header->riff,4) ||
 memcmp("WAVE",header->wave,4) ||
 memcmp("fmt ",header->fmt,4)  ||
 memcmp("data",header->data,4)
 )){

 buffer = (char*)malloc(header->dataSize);
 if (buffer){
 if (fread(buffer,header->dataSize,1,file)){
 fclose(file);
 return buffer;
 }
 else *error = -5;
 free(buffer);
 }
 else *error = -6;
 }
 else *error = -7;
 }
 else *error = -8;
 
 fclose(file);
 return 0;
}

ALuint createBufferFromWave(char* data,BasicWAVEHeader header){

 ALuint buffer = 0;
 ALuint format = 0;
 switch (header.bitsPerSample){
 case 8:
 format = (header.channels == 1) ? AL_FORMAT_MONO8 : AL_FORMAT_STEREO8;
 break;
 case 16:
 format = (header.channels == 1) ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16;
 break;
 default:
 return 0;
 }

 alGenBuffers(1,&buffer);
 alBufferData(buffer,format,data,header.dataSize,header.samplesPerSec);
 return buffer;
}



//Numero de buffers.
#define NUM_BUFFERS 40

//Numero de emisores.
#define NUM_EMISORES 40

//Receptor, posicion, velocidad y orientacion.
ALfloat receptorPos[]={0.0,0.0,4.0};
ALfloat receptorVel[]={0.0,0.0,0.0};
ALfloat receptorOri[]={0.0,0.0,1.0, 0.0,1.0,0.0}; 

//Emisores.
struct Emisor{
	ALuint id;
	ALfloat* pos;
	ALfloat* vel;
};
struct Emisor emisores[NUM_EMISORES];

//Buffers.
ALuint buffer[NUM_BUFFERS];

//Datos de OpenAL.
ALCdevice* device = 0;
ALCcontext* context = 0;
const ALint context_attribs[] = { ALC_FREQUENCY, 22050, 0 };
 
 

  
  
  
  
  
JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_initOpenAL
  (JNIEnv * env, jobject obj){
  // Initialization
 device = alcOpenDevice(0);
 context = alcCreateContext(device, context_attribs);
 alcMakeContextCurrent(context);
 }
 
JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_releaseOpenAL
  (JNIEnv * env, jobject obj){
  // Cleaning up
 alcMakeContextCurrent(0);
 alcDestroyContext(context);
 alcCloseDevice(device);
 }
 
 
JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_cargarBuffer
  (JNIEnv * env, jobject obj, jint idBuffer, jstring filename){

 const char* fnameptr = (*env)->GetStringUTFChars(env, filename, NULL);
 BasicWAVEHeader header;
 int error;
 char* data = readWAV(fnameptr,&header,&error);
 if (data){
 //Now We've Got A Wave In Memory, Time To Turn It Into A Usable Buffer
 buffer[idBuffer] = createBufferFromWave(data,header);
 if (!buffer[idBuffer]){
 free(data);
 return -2;
 }

 } else {
 return error;
 }



 }
 
JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_liberarBuffer
  (JNIEnv * env, jobject obj, jint idBuffer){
  // Release audio buffer
 alDeleteBuffers(1, &buffer[idBuffer]);
 }
 
JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_crearFuente
  (JNIEnv * env, jobject obj, jint idFuente, jint idBuffer, jint loop){
 
 emisores[idFuente].pos = (ALfloat*)malloc(sizeof(ALfloat)*3);
 emisores[idFuente].pos[0] = 0;
 emisores[idFuente].pos[1] = 0;
 emisores[idFuente].pos[2] = 0;
 emisores[idFuente].vel = (ALfloat*)malloc(sizeof(ALfloat)*3);
 emisores[idFuente].vel[0] = 0;
 emisores[idFuente].vel[1] = 0;
 emisores[idFuente].vel[2] = 0;
 
  // Create source from buffer and play it
 emisores[idFuente].id = 0;
 alGenSources(1, &emisores[idFuente].id );
 alSourcei(emisores[idFuente].id, AL_BUFFER, buffer[idBuffer]);
 alSourcef(emisores[idFuente].id,AL_PITCH,1.0f);
alSourcef(emisores[idFuente].id,AL_GAIN,1.0f);
 if(loop == 1) alSourcei(emisores[idFuente].id,AL_LOOPING,AL_TRUE);
 else alSourcei(emisores[idFuente].id,AL_LOOPING,AL_FALSE);
 }
 
JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_playFuente
  (JNIEnv * env, jobject obj, jint idFuente){
 // Play source
 alSourcePlay(emisores[idFuente].id);
 }
 
 JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_stopFuente
  (JNIEnv * env, jobject obj, jint idFuente){
  
  alSourceStop(emisores[idFuente].id);
  }
  
 JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_pauseFuente
  (JNIEnv * env, jobject obj, jint idFuente){
  
  alSourcePause(emisores[idFuente].id);
 }
  
 
JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_liberarFuente
  (JNIEnv * env, jobject obj, jint idFuente){
 // Release source
 alDeleteSources(1, &emisores[idFuente].id);
 free(emisores[idFuente].pos);
 free(emisores[idFuente].vel);
 }

 JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_estaReproduciendo
  (JNIEnv * env, jobject obj, jint idFuente){
  
 int        sourceState = AL_PLAYING;
 alGetSourcei(emisores[idFuente].id, AL_SOURCE_STATE, &sourceState);
 
 if(sourceState == AL_PLAYING) return 1;
 else return 0;
 
  }

JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_setReceptor
  (JNIEnv * env, jobject obj, jfloat posx, jfloat posy, jfloat posz, jfloat velx, jfloat vely, jfloat velz, jfloat dirx, jfloat diry, jfloat dirz, jfloat upx, jfloat upy, jfloat upz){
receptorPos[0] = posx;
receptorPos[1] = posy;
receptorPos[2] = posz;
receptorVel[0] = velx;
receptorVel[1] = vely;
receptorVel[2] = velz;
receptorOri[0] = dirx;
receptorOri[1] = diry;
receptorOri[2] = dirz;
receptorOri[3] = upx;
receptorOri[4] = upy;
receptorOri[5] = upz;
alListenerfv(AL_POSITION,receptorPos);
alListenerfv(AL_VELOCITY,receptorVel);
alListenerfv(AL_ORIENTATION,receptorOri);
}

JNIEXPORT jint JNICALL Java_com_icecool_worldinshadow_ActividadOpenAL_setEmisor
  (JNIEnv * env, jobject obj, jint idEmisor, jfloat posx, jfloat posy, jfloat posz, jfloat velx, jfloat vely, jfloat velz){
emisores[idEmisor].pos[0] = posx;
emisores[idEmisor].pos[1] = posy;
emisores[idEmisor].pos[2] = posz;
emisores[idEmisor].vel[0] = velx;
emisores[idEmisor].vel[1] = vely;
emisores[idEmisor].vel[2] = velz;
alSourcefv(emisores[idEmisor].id,AL_POSITION,emisores[idEmisor].pos);
alSourcefv(emisores[idEmisor].id,AL_VELOCITY,emisores[idEmisor].vel);
}
